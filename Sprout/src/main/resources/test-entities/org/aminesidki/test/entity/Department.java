package org.aminesidki.test.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;

@SproutLightDTO
@Entity
@Getter @Setter
public class Department {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    @SproutLDF
    private String building;
    @SproutLDF
    private LocalDate establishedDate;
}