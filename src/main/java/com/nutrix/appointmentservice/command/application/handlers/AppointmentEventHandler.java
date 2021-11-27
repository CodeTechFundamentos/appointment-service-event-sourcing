package com.nutrix.appointmentservice.command.application.handlers;

import com.nutrix.appointmentservice.command.infra.Appointment;
import com.nutrix.appointmentservice.command.infra.IAppointmentRepository;
import events.AppointmentCreatedEvent;
import events.AppointmentDeletedEvent;
import events.AppointmentUpdatedEvent;
import org.axonframework.config.ProcessingGroup;
import org.axonframework.eventhandling.EventHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@ProcessingGroup("appointment")
public class AppointmentEventHandler {
    private final IAppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentEventHandler(IAppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @EventHandler
    public void on(AppointmentCreatedEvent event) {
        appointmentRepository.save(new Appointment(
                event.getId(),
                event.getPatientId(),
                event.getNutritionistId(),
                event.getCreatedAt(),
                event.getLastModification(),
                event.getAppointmentDate(),
                event.getNutritionistNotes()
        ));
    }

    @EventHandler
    public void on(AppointmentUpdatedEvent event) {
        appointmentRepository.save(new Appointment(
                event.getId(),
                event.getPatientId(),
                event.getNutritionistId(),
                event.getCreatedAt(),
                event.getLastModification(),
                event.getAppointmentDate(),
                event.getNutritionistNotes()
        ));
    }

    @EventHandler
    public void on(AppointmentDeletedEvent event) {
        appointmentRepository.deleteById(event.getId());
    }
}
