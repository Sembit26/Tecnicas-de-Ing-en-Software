package com.tingeso.reserva_service.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoPorPersonas {
    private int minPersonas;
    private int maxPersonas;
    private double descuento;
}
