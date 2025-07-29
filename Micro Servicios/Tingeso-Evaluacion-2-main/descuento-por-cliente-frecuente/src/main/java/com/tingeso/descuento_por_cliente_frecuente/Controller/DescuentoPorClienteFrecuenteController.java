package com.tingeso.descuento_por_cliente_frecuente.Controller;

import com.tingeso.descuento_por_cliente_frecuente.Entity.DescuentoPorClienteFrecuente;
import com.tingeso.descuento_por_cliente_frecuente.Service.DescuentoPorClienteFrecuenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/descuentoPorClienteFrecuente")
public class DescuentoPorClienteFrecuenteController {

    @Autowired
    private DescuentoPorClienteFrecuenteService service;

    @PostMapping("/create")
    public ResponseEntity<DescuentoPorClienteFrecuente> crear(@RequestBody DescuentoPorClienteFrecuente descuento) {
        return ResponseEntity.ok(service.guardar(descuento));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<DescuentoPorClienteFrecuente>> obtenerTodos() {
        return ResponseEntity.ok(service.obtenerTodos());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<DescuentoPorClienteFrecuente> obtenerPorId(@PathVariable int id) {
        return service.obtenerPorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/obtenerPorFrecuencia/{frecuencia}")
    public ResponseEntity<DescuentoPorClienteFrecuente> obtenerPorFrecuencia(@PathVariable int frecuencia) {
        return service.obtenerDescuento(frecuencia)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<DescuentoPorClienteFrecuente> actualizar(@PathVariable int id, @RequestBody DescuentoPorClienteFrecuente nuevoDescuento) {
        Optional<DescuentoPorClienteFrecuente> actual = service.obtenerPorId(id);
        if (actual.isPresent()) {
            DescuentoPorClienteFrecuente d = actual.get();
            d.setMinVisitas(nuevoDescuento.getMinVisitas());
            d.setMaxVisitas(nuevoDescuento.getMaxVisitas());
            d.setDescuento(nuevoDescuento.getDescuento());
            return ResponseEntity.ok(service.guardar(d));
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable int id) {
        if (service.obtenerPorId(id).isPresent()) {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
