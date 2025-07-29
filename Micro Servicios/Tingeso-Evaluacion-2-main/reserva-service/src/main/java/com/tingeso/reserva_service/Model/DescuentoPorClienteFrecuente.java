package com.tingeso.reserva_service.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DescuentoPorClienteFrecuente {
    private int minVisitas;
    private int maxVisitas;
    private double descuento;
}
