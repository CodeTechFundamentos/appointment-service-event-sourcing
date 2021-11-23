package com.nutrix.appointmentservice.query.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

import java.util.Date;

@Value
public class CreateDietModel {
    @ApiModelProperty(notes = "Diet name",name="name",required=true,example = "Dieta Proteica")
    private String name;
    @ApiModelProperty(notes = "Diet description",name="description",required=true,example = " La cantidad de proteína que se recomienda en una dieta hiperproteica oscila entre 1,3 y 2 g de proteína por kg de peso corporal al día")
    private String description;
    @ApiModelProperty(notes = "Diet created date",name="createdAt",required=true,example = "2021-11-23T04:25:17.917+00:00")
    private Date createdAt;
    @ApiModelProperty(notes = "Appointment updated date",name="lastModification",required=true,example = "2021-11-23T04:25:17.917+00:00")
    private Date lastModification;
}
