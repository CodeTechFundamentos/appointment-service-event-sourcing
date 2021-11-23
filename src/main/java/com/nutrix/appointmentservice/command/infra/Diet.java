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
@Table(name="Diet")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Diet {

    @Id
    @ApiModelProperty(notes = "Diet Id",name="dietId",required=true,example = "04e19ea0-d9e4-4fa2-8cc8-6b7adc47bb71")
    private String id;
    @Column(name="name")
    @ApiModelProperty(notes = "Diet name",name="name",required=true,example = "Dieta Proteica")
    private String name;
    @Column(name="description")
    @ApiModelProperty(notes = "Diet description",name="description",required=true,example = " La cantidad de proteína que se recomienda en una dieta hiperproteica oscila entre 1,3 y 2 g de proteína por kg de peso corporal al día")
    private String description;
    @Column(name="createdAt")
    @ApiModelProperty(notes = "Diet created date",name="createdAt",required=true,example = "2021-11-23T04:25:17.917+00:00")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name="lastModification")
    @ApiModelProperty(notes = "Appointment updated date",name="lastModification",required=true,example = "2021-11-23T04:25:17.917+00:00")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModification;
}
