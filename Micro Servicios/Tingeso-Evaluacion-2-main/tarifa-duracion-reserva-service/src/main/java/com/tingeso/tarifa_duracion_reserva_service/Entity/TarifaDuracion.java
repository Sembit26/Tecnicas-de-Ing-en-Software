package com.tingeso.tarifa_duracion_reserva_service.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TarifaDuracion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int numVueltasTiempoMax; //número de vueltas o tiempo maximo (ej: 10 vueltas/15 minutos)
    private double tarifa; //precio base
    private int duracion_total; //duracion total de la reserva (ej: para 10 vueltas, duracion maxima son 30 minutos
}
