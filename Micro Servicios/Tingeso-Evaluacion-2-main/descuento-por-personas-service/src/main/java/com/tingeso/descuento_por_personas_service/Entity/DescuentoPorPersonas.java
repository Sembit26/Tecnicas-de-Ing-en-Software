package com.tingeso.descuento_por_personas_service.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoPorPersonas {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int minPersonas;
    private int maxPersonas;
    private double descuento;
}
