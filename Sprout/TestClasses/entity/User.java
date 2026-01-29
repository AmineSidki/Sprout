package org.AmineSidki.demo.admin.entity;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@Getter @Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Helper helper;
    private String email;

    private Map<String,List<String>> test;

    @OneToMany(mappedBy = "owner")
    private List<Project> projects;
}