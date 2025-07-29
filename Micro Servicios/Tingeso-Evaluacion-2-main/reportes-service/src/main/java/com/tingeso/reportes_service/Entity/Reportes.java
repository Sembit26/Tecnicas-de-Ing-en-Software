package com.tingeso.reportes_service.Entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Reportes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String mes; // Ejemplo: "2025-06"

    private String tipoReporte; // Ejemplo: "IngresosPorVueltas"

    private LocalDateTime fechaCreacion;
}
