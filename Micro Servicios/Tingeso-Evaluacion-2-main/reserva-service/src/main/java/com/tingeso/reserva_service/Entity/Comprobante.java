package com.tingeso.reserva_service.Entity;

import java.util.List;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Comprobante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "comprobante_id")  // clave foránea en DetallePagoPorPersona
    private List<DetallePagoPorPersona> detallesPago;

    private double precio_final; // precio final del grupo (sin IVA)
    private double iva; // valor del IVA total
    private double monto_total_iva; // precio total con IVA
}
