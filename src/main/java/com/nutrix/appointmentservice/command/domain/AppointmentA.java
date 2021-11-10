package com.nutrix.appointmentservice.command.domain;

import com.nutrix.appointmentservice.command.application.Notification;
import com.nutrix.appointmentservice.query.models.UpdateAppointmentModel;
import command.CreateAppointmentC;
import command.DeleteAppointmentC;
import command.UpdateAppointmentC;
import events.AppointmentCreatedEvent;
import events.AppointmentDeletedEvent;
import events.AppointmentUpdatedEvent;
import lombok.NoArgsConstructor;
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

    @CommandHandler
    public void on(UpdateAppointmentC updateAppointmentC) {
        Notification notification = validateUpdateAppointment(updateAppointmentC);
        if(notification.hasErrors())
            throw new IllegalArgumentException(notification.errorMessage());
        AppointmentUpdatedEvent event = new AppointmentUpdatedEvent(
                updateAppointmentC.getId(),
                updateAppointmentC.getPatientId(),
                updateAppointmentC.getNutritionistId(),
                updateAppointmentC.getCreatedAt(),
                updateAppointmentC.getLastModification(),
                updateAppointmentC.getAppointmentDate(),
                updateAppointmentC.getNutritionistNotes()
        );
        apply(event);
    }

    @CommandHandler
    public void on(DeleteAppointmentC deleteAppointmentC) {
        Notification notification = validateDeleteAppointment(deleteAppointmentC);
        if(notification.hasErrors())
            throw new IllegalArgumentException(notification.errorMessage());
        AppointmentDeletedEvent event = new AppointmentDeletedEvent(deleteAppointmentC.getId());
        apply(event);
    }

    private Notification validateAppointment(CreateAppointmentC createAppointmentC) {
        Notification notification = new Notification();
        validateAppointmentId(createAppointmentC.getId(), notification);
        return notification;
    }

    private Notification validateUpdateAppointment(UpdateAppointmentC updateAppointmentC) {
        Notification notification = new Notification();
        validateAppointmentId(updateAppointmentC.getId(), notification);
        return notification;
    }

    private Notification validateDeleteAppointment(DeleteAppointmentC deleteAppointmentC) {
        Notification notification = new Notification();
        validateAppointmentId(deleteAppointmentC.getId(), notification);
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

    @EventSourcingHandler
    public void on(AppointmentUpdatedEvent appointmentUpdatedEvent) {
        this.id = appointmentUpdatedEvent.getId();
        this.nutritionistId = appointmentUpdatedEvent.getNutritionistId();
        this.patientId = appointmentUpdatedEvent.getPatientId();
        this.appointmentDate = appointmentUpdatedEvent.getAppointmentDate();
        this.nutritionistNotes = appointmentUpdatedEvent.getNutritionistNotes();
    }

    @EventSourcingHandler
    public void on(AppointmentDeletedEvent appointmentDeletedEvent) {
        this.id = appointmentDeletedEvent.getId();
    }

}
