package com.tingeso.reserva_service.DTO;

import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComprobanteDTO {
    private double tarifa;  // tarifa base para cada persona
    private double descuentoCumpleaneros;
    private int maxCumpleanerosConDescuento;
    private double descuentoPorCantidadDePersonas;
    private double descuentoPorFrecuenciaCliente;

    private String nombreCliente;
    private String correoCliente;

    private Map<String, String> nombreCorreo; // nombre -> correo
    private List<String> correosCumpleaneros;
}
