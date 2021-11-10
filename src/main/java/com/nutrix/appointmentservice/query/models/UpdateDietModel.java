package com.nutrix.appointmentservice.query.models;

import lombok.Value;

import java.util.Date;

@Value
public class UpdateDietModel {
    private String name;
    private String description;
    private Date createdAt;
    private Date lastModification;
}
