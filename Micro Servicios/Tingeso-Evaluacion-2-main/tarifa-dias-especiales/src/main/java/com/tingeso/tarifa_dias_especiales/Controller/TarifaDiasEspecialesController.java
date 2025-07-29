package com.tingeso.tarifa_dias_especiales.Controller;

import com.tingeso.tarifa_dias_especiales.Entity.TarifaDiasEspeciales;
import com.tingeso.tarifa_dias_especiales.Service.TarifaDiasEspecialesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarifasDiasEspeciales")
public class TarifaDiasEspecialesController {

    @Autowired
    private TarifaDiasEspecialesService service;

    @PostMapping("/create")
    public ResponseEntity<TarifaDiasEspeciales> crear(@RequestBody TarifaDiasEspeciales tarifa) {
        return ResponseEntity.ok(service.guardarTarifa(tarifa));
    }

    @GetMapping("/getAll")
    public List<TarifaDiasEspeciales> listarTodas() {
        return service.obtenerTodas();
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<TarifaDiasEspeciales> obtenerPorId(@PathVariable int id) {
        Optional<TarifaDiasEspeciales> tarifa = service.obtenerPorId(id);
        return tarifa.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/buscarTarifaDiaEspecial")
    public List<TarifaDiasEspeciales> buscarPorDiaYVueltas(@RequestParam int numVueltas_TiempoMax) {
        return service.buscarPorNumVueltas(numVueltas_TiempoMax);
    }

    //Este es el que se ocupa al final, no el anterior
    @GetMapping("/buscarTarifaPorVueltasYPersonas")
    public ResponseEntity<TarifaDiasEspeciales> buscarPorVueltasYPersonas(
            @RequestParam int numVueltas,
            @RequestParam int cantidadCumpleaneros) {

        Optional<TarifaDiasEspeciales> resultado = service.obtenerTarifaPorVueltasYPersonas(numVueltas, cantidadCumpleaneros);

        return resultado.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/esDiaEspecial")
    public ResponseEntity<Boolean> verificarDiaEspecial(@RequestParam String fecha) {
        try {
            LocalDate date = LocalDate.parse(fecha);
            boolean esEspecial = service.esDiaEspecial(date);
            return ResponseEntity.ok(esEspecial);
        } catch (DateTimeParseException e) {
            return ResponseEntity.badRequest().body(false);
        }
    }


    @PutMapping("/update/{id}")
    public ResponseEntity<TarifaDiasEspeciales> actualizar(@PathVariable int id, @RequestBody TarifaDiasEspeciales nuevaTarifa) {
        TarifaDiasEspeciales actualizada = service.actualizarTarifa(id, nuevaTarifa);
        if (actualizada != null) {
            return ResponseEntity.ok(actualizada);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        boolean eliminado = service.eliminarTarifa(id);
        if (eliminado) {
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
