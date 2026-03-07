package org.aminesidki.test.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter @Setter
public class MedicalRecord {
    @Id
    private UUID recordUuid; // Different ID type (UUID)

    private String diagnosis;
    private String treatmentPlan;

    @OneToOne
    @JoinColumn(name = "patient_id")
    private Patient patient;
}