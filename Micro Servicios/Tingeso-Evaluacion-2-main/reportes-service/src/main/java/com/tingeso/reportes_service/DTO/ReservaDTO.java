package com.tingeso.reportes_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private int id;
    private int idUsuario;
    private int num_vueltas_tiempo_maximo;
    private int num_personas;
    private double precio_regular;
    private int duracion_total;
    private LocalDateTime fechaHora;
    private String nombreCliente;
    private LocalDate fechaInicio;
    private LocalTime horaInicio;
    private LocalTime horaFin;
    private ComprobanteDTO comprobante; // Nuevo campo agregado
}
