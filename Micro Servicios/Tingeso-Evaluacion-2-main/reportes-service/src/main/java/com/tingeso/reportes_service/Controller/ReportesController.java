package com.tingeso.reportes_service.Controller;

import com.tingeso.reportes_service.DTO.ReservaDTO;
import com.tingeso.reportes_service.Entity.Reportes;
import com.tingeso.reportes_service.Service.ReportesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/reportes")
public class ReportesController {

    @Autowired
    private ReportesService reportesService;

    @GetMapping
    public ResponseEntity<List<Reportes>> getTodos() {
        return ResponseEntity.ok(reportesService.obtenerTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reportes> getPorId(@PathVariable Long id) {
        Optional<Reportes> reporteOpt = reportesService.obtenerPorId(id);
        return reporteOpt.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Reportes> crear(@RequestBody Reportes reporte) {
        Reportes guardado = reportesService.guardar(reporte);
        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reportes> actualizar(@PathVariable Long id, @RequestBody Reportes reporte) {
        if (!reportesService.obtenerPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        reporte.setId(id);
        Reportes actualizado = reportesService.guardar(reporte);
        return ResponseEntity.ok(actualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        if (!reportesService.obtenerPorId(id).isPresent()) {
            return ResponseEntity.notFound().build();
        }
        reportesService.eliminar(id);
        return ResponseEntity.noContent().build();
    }


    @GetMapping("/getReservasPorRangoMeses")
    public ResponseEntity<List<ReservaDTO>> obtenerReservasPorRangoDeMeses(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        List<ReservaDTO> reservas = reportesService.obtenerReservasPorRangoDeMeses(fechaInicio, fechaFin);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping("/ingresos-por-vueltas")
    public ResponseEntity<Map<String, Map<String, Double>>> obtenerReporteIngresosPorVueltas(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        Map<String, Map<String, Double>> reporte = reportesService.generarReporteIngresosPorVueltas(fechaInicio, fechaFin);
        return ResponseEntity.ok(reporte);
    }

    @GetMapping("/ingresos-por-grupo")
    public Map<String, Map<String, Double>> obtenerIngresosPorGrupo(
            @RequestParam("fechaInicio") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam("fechaFin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaFin) {
        return reportesService.generarReporteIngresosPorGrupoDePersonas(fechaInicio, fechaFin);
    }
}
