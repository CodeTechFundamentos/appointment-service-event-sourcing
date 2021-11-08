package com.nutrix.appointmentservice.query.models;

import lombok.Value;

import java.util.Date;

@Value
public class CreateAppointmentModel {
    private String id;
    private Integer patientId;
    private Integer nutritionistId;
    private Date createdAt;
    private Date lastModification;
    private Date appointmentDate;
    private String nutritionistNotes;
}
