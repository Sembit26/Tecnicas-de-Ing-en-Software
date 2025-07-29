package com.tingeso.descuento_por_cliente_frecuente.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoPorClienteFrecuente {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int minVisitas;
    private int maxVisitas;
    private double descuento;
}
