package com.nutrix.appointmentservice.command.infra;

import org.springframework.data.jpa.repository.JpaRepository;

public interface IAppointmentRepository extends JpaRepository<Appointment, String> {
}
