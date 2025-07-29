package com.tingeso.reserva_service.Entity;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reserva {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private int idUsuario;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "comprobante_id")
    private Comprobante comprobante;

    private int num_vueltas_tiempo_maximo;
    private int num_personas; //Cantidad de personas para las que se generó la reserva
    private double precio_regular;
    private int duracion_total;
    private LocalDateTime fechaHora; // Fecha en la que se generó la reserva
    private String nombreCliente;

    private LocalDate fechaInicio; //fecha de inicio de reserva
    private LocalTime horaInicio; // hora de inicio de reserva
    private LocalTime horaFin; // hora de fin de la reserva

}
