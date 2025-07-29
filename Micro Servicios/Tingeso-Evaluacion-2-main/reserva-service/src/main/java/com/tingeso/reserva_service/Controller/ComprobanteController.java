package com.tingeso.reserva_service.Controller;

import com.tingeso.reserva_service.DTO.ComprobanteDTO;
import com.tingeso.reserva_service.Entity.Comprobante;
import com.tingeso.reserva_service.Entity.DetallePagoPorPersona;
import com.tingeso.reserva_service.Service.ComprobanteService;
import lombok.Data;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/comprobante")
public class ComprobanteController {

    @Autowired
    private ComprobanteService comprobanteService;

    // Obtener todos los comprobantes
    @GetMapping("/getAll")
    public List<Comprobante> getAll() {
        return comprobanteService.getAll();
    }

    // Obtener comprobante por ID
    @GetMapping("/getById/{id}")
    public Comprobante getById(@PathVariable Long id) {
        return comprobanteService.getById(id);
    }

    // Eliminar comprobante por ID
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable Long id) {
        comprobanteService.delete(id);
    }

    // Crear comprobante
    @PostMapping("/create")
    public Comprobante crearComprobante(@RequestBody ComprobanteDTO request) {
        return comprobanteService.crearComprobante(
                request.getTarifa(),
                request.getDescuentoCumpleaneros(),
                request.getMaxCumpleanerosConDescuento(),
                request.getDescuentoPorCantidadDePersonas(),
                request.getDescuentoPorFrecuenciaCliente(),
                request.getNombreCliente(),
                request.getCorreoCliente(),
                request.getNombreCorreo(),
                request.getCorreosCumpleaneros()
        );
    }

    @GetMapping(value = "/formateado/{id}", produces = MediaType.TEXT_PLAIN_VALUE)
    public ResponseEntity<String> obtenerComprobanteFormateado(@PathVariable Long id) {
        Comprobante comprobante = comprobanteService.getById(id);

        if (comprobante == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Comprobante no encontrado");
        }

        String formatoTabla = comprobanteService.formatearComprobante(comprobante);
        return ResponseEntity.ok(formatoTabla);
    }

}
