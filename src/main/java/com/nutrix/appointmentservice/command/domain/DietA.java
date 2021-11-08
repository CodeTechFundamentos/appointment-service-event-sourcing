package com.nutrix.appointmentservice.command.domain;

import com.nutrix.appointmentservice.command.application.Notification;
import command.CreateDietC;
import events.DietCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.Date;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

@Aggregate
public class DietA {

    @AggregateIdentifier
    private String id;

    private String name;
    private String description;
    private Date createdAt;
    private Date lastModification;

    public DietA() {
    }

    @CommandHandler
    public DietA(CreateDietC createDietC) {
        Notification notification = validateDiet((createDietC));
        if(notification.hasErrors())
            throw new IllegalArgumentException(notification.errorMessage());
        DietCreatedEvent event = new DietCreatedEvent(
                createDietC.getId(),
                createDietC.getName(),
                createDietC.getDescription(),
                createDietC.getCreatedAt(),
                createDietC.getLastModification()
        );
        apply(event);
    }

    private Notification validateDiet(CreateDietC createDietC) {
        Notification notification = new Notification();
        validateDietId(createDietC.getId(), notification);
        return notification;
    }

    private void validateDietId(String dietId, Notification notification) {
        if (dietId == null) {
            notification.addError("Diet id is missing");
        }
    }

    //Event Sourcing Handlers

    @EventSourcingHandler
    public void on(DietCreatedEvent dietCreatedEvent) {
        this.id = dietCreatedEvent.getId();
        this.name = dietCreatedEvent.getName();
        this.description = dietCreatedEvent.getDescription();
        this.createdAt = dietCreatedEvent.getCreatedAt();
        this.lastModification = dietCreatedEvent.getLastModification();
    }
}
