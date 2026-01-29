package org.AmineSidki.demo.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Getter @Setter
public class Project {
    @Id
    private UUID id;

    private String name;
    private String description;

    @ManyToOne
    private User owner;
}