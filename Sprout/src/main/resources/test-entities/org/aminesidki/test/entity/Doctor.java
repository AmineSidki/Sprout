package org.aminesidki.test.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter @Setter
public class Doctor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String fullName;
    private String specialization;

    @ManyToOne
    @JoinColumn(name = "department_id")
    private Department department;
}