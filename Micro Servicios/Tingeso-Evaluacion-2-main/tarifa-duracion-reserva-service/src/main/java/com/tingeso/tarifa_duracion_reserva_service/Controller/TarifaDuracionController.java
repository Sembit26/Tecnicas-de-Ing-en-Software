package com.tingeso.tarifa_duracion_reserva_service.Controller;

import com.tingeso.tarifa_duracion_reserva_service.Entity.TarifaDuracion;
import com.tingeso.tarifa_duracion_reserva_service.Service.TarifaDuracionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/tarifasDuracion")
public class TarifaDuracionController {

    @Autowired
    private TarifaDuracionService service;

    @GetMapping("/getAll")
    public List<TarifaDuracion> obtenerTodas() {
        return service.obtenerTodas();
    }

    @GetMapping("/getById/{id}")
    public Optional<TarifaDuracion> obtenerPorId(@PathVariable int id) {
        return service.obtenerPorId(id);
    }

    @PostMapping("/create")
    public TarifaDuracion crear(@RequestBody TarifaDuracion tarifaDuracion) {
        return service.guardar(tarifaDuracion);
    }

    @PutMapping("/update/{id}")
    public TarifaDuracion actualizar(@PathVariable int id, @RequestBody TarifaDuracion nuevaTarifa) {
        Optional<TarifaDuracion> actual = service.obtenerPorId(id);
        if (actual.isPresent()) {
            TarifaDuracion td = actual.get();
            td.setNumVueltasTiempoMax(nuevaTarifa.getNumVueltasTiempoMax());
            td.setTarifa(nuevaTarifa.getTarifa());
            td.setDuracion_total(nuevaTarifa.getDuracion_total());
            return service.guardar(td);
        } else {
            return null;
        }
    }

    @DeleteMapping("/delete/{id}")
    public void eliminar(@PathVariable int id) {
        service.eliminar(id);
    }

    @GetMapping("/buscarPorVueltas")
    public TarifaDuracion buscarPorVueltas(@RequestParam int numVueltas) {
        return service.buscarPorNumVueltas(numVueltas);
    }
}
