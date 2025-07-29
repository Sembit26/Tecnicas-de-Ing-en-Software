package com.tingeso.reserva_service.DTO;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReservaDTO {
    private int idUsuario;
    private int numVueltasTiempoMaximo;
    private int numPersonas;
    private int numFrecuenciaCliente;
    private String nombreCliente;
    private String correoCliente;
    private LocalDate fechaInicio;
    private LocalTime horaInicio;
    private Map<String, String> nombreCorreo;
    private List<String> correosCumpleaneros;
}
