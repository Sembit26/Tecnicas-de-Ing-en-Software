package com.tingeso.rack_semanal_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private int id;
    private String nombreCliente;
    private LocalDate fechaInicio;
    private LocalTime horaInicio;
    private LocalTime horaFin;
}
