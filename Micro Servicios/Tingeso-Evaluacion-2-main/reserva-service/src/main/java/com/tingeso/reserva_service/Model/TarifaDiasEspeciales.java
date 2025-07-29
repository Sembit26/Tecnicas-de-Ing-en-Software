package com.tingeso.reserva_service.Model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifaDiasEspeciales {
    private int numVueltasTiempoMax;
    private int duracion_total; // en minutos
    private double tarifa; // precio base para esta configuración

    // Reglas de descuento para cumpleañeros
    private int minPersonas;
    private int maxPersonas;
    private int maxCumpleanerosConDescuento; //cantidad de cumpleañeros a los que se le aplica el descuento
    private double descuentoCumpleaneros; // 0.5 para 50%
}
