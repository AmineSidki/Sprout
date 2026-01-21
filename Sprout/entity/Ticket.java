package org.aminesidki.morogo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID uuid;

    @ManyToOne
    private Evenement event;

    private String clientName;
    private String clientEmail;
    private String clientPhone;
    private String clientCIN;


    @ManyToOne
    private Utilisateur user;
}
