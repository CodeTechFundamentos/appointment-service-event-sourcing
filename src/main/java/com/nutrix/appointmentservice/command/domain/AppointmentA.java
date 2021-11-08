package com.nutrix.appointmentservice.command.domain;

import com.nutrix.appointmentservice.command.application.Notification;
import command.CreateAppointmentC;
import events.AppointmentCreatedEvent;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.spring.stereotype.Aggregate;
import static org.axonframework.modelling.command.AggregateLifecycle.apply;

import java.util.Date;

@Aggregate
public class AppointmentA {

    @AggregateIdentifier
    private String id;

    private Integer patientId;
    private Integer nutritionistId;
    private Date createdAt;
    private Date lastModification;
    private Date appointmentDate;
    private String nutritionistNotes;

    public AppointmentA(){
    }

    @CommandHandler
    public AppointmentA(CreateAppointmentC createAppointmentC) {
        Notification notification = validateAppointment(createAppointmentC);
        if(notification.hasErrors())
            throw new IllegalArgumentException(notification.errorMessage());
        AppointmentCreatedEvent event = new AppointmentCreatedEvent(
                createAppointmentC.getId(),
                createAppointmentC.getPatientId(),
                createAppointmentC.getNutritionistId(),
                createAppointmentC.getCreatedAt(),
                createAppointmentC.getLastModification(),
                createAppointmentC.getAppointmentDate(),
                createAppointmentC.getNutritionistNotes()
        );
        apply(event);
    }

    private Notification validateAppointment(CreateAppointmentC createAppointmentC) {
        Notification notification = new Notification();
        validateAppointmentId(createAppointmentC.getId(), notification);
        return notification;
    }

    private void validateAppointmentId(String appointmentId, Notification notification) {
        if(appointmentId == null) {
            notification.addError("Appointment id is missing");
        }
    }

    //Event Sourcing Handlers

    @EventSourcingHandler
    public void on(AppointmentCreatedEvent appointmentCreatedEvent) {
        this.id = appointmentCreatedEvent.getId();
        this.nutritionistId = appointmentCreatedEvent.getNutritionistId();
        this.patientId = appointmentCreatedEvent.getPatientId();
        this.appointmentDate = appointmentCreatedEvent.getAppointmentDate();
        this.nutritionistNotes = appointmentCreatedEvent.getNutritionistNotes();
    }

}
