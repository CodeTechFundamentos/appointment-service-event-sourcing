package com.nutrix.appointmentservice.command.infra;

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
    private String id;
    @Column(name="name")
    private String name;
    @Column(name="description")
    private String description;
    @Column(name="createdAt")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;
    @Column(name="lastModification")
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModification;
}
