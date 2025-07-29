package com.tingeso.tarifa_dias_especiales.Entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifaDiasEspeciales {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int numVueltasTiempoMax;
    private int duracion_total; // en minutos
    private double tarifa; // precio base para esta configuración

    // Reglas de descuento para cumpleañeros
    private int minPersonas;
    private int maxPersonas;
    private int maxCumpleanerosConDescuento; //cantidad de cumpleañeros a los que se le aplica el descuento
    private double descuentoCumpleaneros; // 0.5 para 50%
}

