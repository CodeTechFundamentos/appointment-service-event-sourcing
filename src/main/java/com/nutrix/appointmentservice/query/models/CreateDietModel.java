package com.nutrix.appointmentservice.query.models;

import lombok.Value;

import java.util.Date;

@Value
public class CreateDietModel {
    private String id;
    private String name;
    private String description;
    private Date createdAt;
    private Date lastModification;
}
