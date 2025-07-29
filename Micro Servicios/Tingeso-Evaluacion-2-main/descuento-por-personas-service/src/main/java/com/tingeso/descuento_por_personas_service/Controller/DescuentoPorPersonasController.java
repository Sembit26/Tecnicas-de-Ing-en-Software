package com.tingeso.descuento_por_personas_service.Controller;

import com.tingeso.descuento_por_personas_service.Entity.DescuentoPorPersonas;
import com.tingeso.descuento_por_personas_service.Service.DescuentoPorPersonasService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/descuentoPorNumPersonas")
public class DescuentoPorPersonasController {

    @Autowired
    private DescuentoPorPersonasService service;

    // Obtener todos
    @GetMapping("/getAll")
    public List<DescuentoPorPersonas> getAll() {
        return service.obtenerTodos();
    }

    // Obtener por ID
    @GetMapping("/getById/{id}")
    public ResponseEntity<DescuentoPorPersonas> getById(@PathVariable int id) {
        Optional<DescuentoPorPersonas> descuento = service.obtenerPorId(id);
        return descuento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Crear nuevo
    @PostMapping("/create")
    public DescuentoPorPersonas create(@RequestBody DescuentoPorPersonas descuento) {
        return service.guardar(descuento);
    }

    // Actualizar
    @PutMapping("/update/{id}")
    public ResponseEntity<DescuentoPorPersonas> update(@PathVariable int id, @RequestBody DescuentoPorPersonas nuevoDescuento) {
        Optional<DescuentoPorPersonas> existente = service.obtenerPorId(id);
        if (existente.isPresent()) {
            DescuentoPorPersonas descuento = existente.get();
            descuento.setMinPersonas(nuevoDescuento.getMinPersonas());
            descuento.setMaxPersonas(nuevoDescuento.getMaxPersonas());
            descuento.setDescuento(nuevoDescuento.getDescuento());
            DescuentoPorPersonas actualizado = service.guardar(descuento);
            return ResponseEntity.ok(actualizado);
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> delete(@PathVariable int id) {
        Optional<DescuentoPorPersonas> existente = service.obtenerPorId(id);
        if (existente.isPresent()) {
            service.eliminar(id);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    // Obtener descuento por número de personas (rango)
    @GetMapping("/buscarDescuento/{numPersonas}")
    public ResponseEntity<DescuentoPorPersonas> getDescuentoPorPersonas(@PathVariable int numPersonas) {
        return service.obtenerDescuento(numPersonas)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
