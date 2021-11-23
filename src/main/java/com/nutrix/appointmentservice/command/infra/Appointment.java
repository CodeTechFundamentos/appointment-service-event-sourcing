package com.nutrix.appointmentservice.command.infra;

import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.*;
import javax.persistence.Table;
import java.util.Date;

@Entity
@Table(name="Appointment")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Appointment {

    @Id
    @ApiModelProperty(notes = "Appointment Id",name="appointmentId",required=true,example = "04e19ea0-d9e4-4fa2-8cc8-6b7adc47bb71")
    private String id;
    @Column(name="patientId")
    @ApiModelProperty(notes = "Patient Id",name="patientId",required=true,example = "10")
    private Integer patientId;
    @Column(name="nutritionistId")
    @ApiModelProperty(notes = "Nutritionist Id",name="nutritionistId",required=true,example = "8")
    private Integer nutritionistId;
    @Column(name="createdAt")
    @ApiModelProperty(notes = "Appointment created date",name="createdAt",required=true,example = "2021-11-23T04:25:17.917+00:00")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name="lastModification")
    @ApiModelProperty(notes = "Appointment updated date",name="lastModification",required=true,example = "2021-11-23T04:25:17.917+00:00")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModification;
    @Column(name="appointmentDate")
    @ApiModelProperty(notes = "Appointment date",name="appointmentDate",required=true,example = "2021-11-23T04:25:17.917+00:00")
    private Date appointmentDate;
    @Column(name="nutritionistNotes")
    @ApiModelProperty(notes = "Appointment date",name="appointmentDate",required=true,example = "El paciente debe realizar los tratamientos propuestos")
    private String nutritionistNotes;
}
