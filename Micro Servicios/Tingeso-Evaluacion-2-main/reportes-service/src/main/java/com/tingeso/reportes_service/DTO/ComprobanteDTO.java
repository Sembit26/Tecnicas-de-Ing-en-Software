package com.tingeso.reportes_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ComprobanteDTO {
    private double precio_final;
    private double iva;
    private double monto_total_iva;
}
