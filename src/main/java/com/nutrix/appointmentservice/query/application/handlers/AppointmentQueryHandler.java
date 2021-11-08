package com.nutrix.appointmentservice.query.application.handlers;

import com.nutrix.appointmentservice.command.infra.Appointment;
import com.nutrix.appointmentservice.command.infra.IAppointmentRepository;
import org.axonframework.queryhandling.QueryHandler;
import org.springframework.beans.factory.annotation.Autowired;
import com.nutrix.appointmentservice.query.models.*;
import org.springframework.stereotype.Component;
import queries.GetAppointmentsQuery;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class AppointmentQueryHandler {
    private final IAppointmentRepository appointmentRepository;

    @Autowired
    public AppointmentQueryHandler(IAppointmentRepository appointmentRepository) {
        this.appointmentRepository = appointmentRepository;
    }

    @QueryHandler
    public List<CreateAppointmentModel> handle(GetAppointmentsQuery query) {
        List<Appointment> appointments = appointmentRepository.findAll();

        List<CreateAppointmentModel> appointmentModels =
                appointments.stream()
                        .map(appointment -> new CreateAppointmentModel(
                                appointment.getId(),
                                appointment.getPatientId(),
                                appointment.getNutritionistId(),
                                appointment.getCreatedAt(),
                                appointment.getLastModification(),
                                appointment.getAppointmentDate(),
                                appointment.getNutritionistNotes()
                        )).collect(Collectors.toList());
        return appointmentModels;
    }
}
