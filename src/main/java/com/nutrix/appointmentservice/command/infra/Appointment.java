package com.nutrix.appointmentservice.command.infra;

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
    private String id;
    @Column(name="patientId")
    private Integer patientId;
    @Column(name="nutritionistId")
    private Integer nutritionistId;
    @Column(name="createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name="lastModification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModification;
    @Column(name="appointmentDate")
    private Date appointmentDate;
    @Column(name="nutritionistNotes")
    private String nutritionistNotes;
}
