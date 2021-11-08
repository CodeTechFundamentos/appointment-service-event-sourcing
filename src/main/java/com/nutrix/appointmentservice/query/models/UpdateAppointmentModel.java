package com.nutrix.appointmentservice.query.models;

import lombok.Value;

import java.util.Date;

@Value
public class UpdateAppointmentModel {
    private Date lastModification;
    private Date appointmentDate;
    private String nutritionistNotes;
}
