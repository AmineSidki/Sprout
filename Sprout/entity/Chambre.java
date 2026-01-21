package org.aminesidki.morogo.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@IdClass(ChambreId.class)
@Entity
@Getter
@Setter
@NoArgsConstructor
public class Chambre {
    @Id
    private String number;

    @Id
    @ManyToOne
    private Hotel hotel;

    private Long DPN; //Dollar per night
}
