package org.aminesidki.morogo.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ChambreId implements Serializable {
    private Hotel hotel;
    private String number;
}
