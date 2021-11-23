package com.nutrix.appointmentservice.query.models;

import io.swagger.annotations.ApiModelProperty;
import lombok.Value;

import java.util.Date;

@Value
public class CreateAppointmentModel {
    @ApiModelProperty(notes = "Patient Id",name="patientId",required=true,example = "10")
    private Integer patientId;
    @ApiModelProperty(notes = "Nutritionist Id",name="nutritionistId",required=true,example = "8")
    private Integer nutritionistId;
    @ApiModelProperty(notes = "Appointment created date",name="createdAt",required=true,example = "2021-11-23T04:25:17.917+00:00")
    private Date createdAt;
    @ApiModelProperty(notes = "Appointment updated date",name="lastModification",required=true,example = "2021-11-23T04:25:17.917+00:00")
    private Date lastModification;
    @ApiModelProperty(notes = "Appointment date",name="appointmentDate",required=true,example = "2021-11-23T04:25:17.917+00:00")
    private Date appointmentDate;
    @ApiModelProperty(notes = "Appointment date",name="appointmentDate",required=true,example = "El paciente debe realizar los tratamientos propuestos")
    private String nutritionistNotes;
}
