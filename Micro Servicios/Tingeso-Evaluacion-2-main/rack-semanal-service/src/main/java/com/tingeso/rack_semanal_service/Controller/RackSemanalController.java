package com.tingeso.rack_semanal_service.Controller;

import com.tingeso.rack_semanal_service.DTO.HorarioOcupadoDTO;
import com.tingeso.rack_semanal_service.DTO.HorariosDisponiblesDTO;
import com.tingeso.rack_semanal_service.DTO.ReservaDTO;
import com.tingeso.rack_semanal_service.Entity.RackSemanal;
import com.tingeso.rack_semanal_service.Service.RackSemanalService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/rackSemanal")
public class RackSemanalController {

    @Autowired
    private RackSemanalService rackSemanalService;

    @PostMapping("/create")
    public RackSemanal create(@RequestBody RackSemanal rackSemanal) {
        return rackSemanalService.save(rackSemanal);
    }

    @GetMapping
    public List<RackSemanal> getAll() {
        return rackSemanalService.findAll();
    }

    @GetMapping("/{id}")
    public Optional<RackSemanal> getById(@PathVariable int id) {
        return rackSemanalService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable int id) {
        rackSemanalService.deleteById(id);
    }

    @GetMapping("/horariosOcupados")
    public List<HorarioOcupadoDTO> getTodosHorariosOcupados() {
        return rackSemanalService.obtenerTodosLosHorariosOcupados();
    }

    // Endpoint para obtener horarios disponibles de los próximos 2 meses
    @GetMapping("/horariosDisponibles")
    public List<HorariosDisponiblesDTO> getHorariosDisponiblesProximosDosMeses() {
        LocalDate hoy = LocalDate.now();
        return rackSemanalService.obtenerHorariosDisponiblesProximosDosMeses(hoy);
    }

    @GetMapping("/obtenerReservaPorFechaYHora")
    public Optional<ReservaDTO> obtenerReserva(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fechaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaInicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime horaFin
    ) {
        return rackSemanalService.obtenerReservaPorFechaHoraInicioYHoraFin(fechaInicio, horaInicio, horaFin);
    }

}
