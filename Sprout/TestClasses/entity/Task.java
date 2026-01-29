package org.AmineSidki.demo.admin.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long taskId;

    private String title;
    private LocalDateTime dueDate;
    private Integer priority;
}