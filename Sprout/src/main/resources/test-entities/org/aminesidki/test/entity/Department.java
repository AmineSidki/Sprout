package org.aminesidki.test.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@SproutProjection
@Entity
@Getter @Setter
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @SproutLargeDataField
    private String building;
    @SproutLargeDataField
    private LocalDate establishedDate;
}