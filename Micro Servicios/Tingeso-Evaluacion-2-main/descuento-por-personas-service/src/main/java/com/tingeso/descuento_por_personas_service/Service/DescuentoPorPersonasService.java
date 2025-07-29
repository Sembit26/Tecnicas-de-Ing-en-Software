package com.tingeso.descuento_por_personas_service.Service;

import com.tingeso.descuento_por_personas_service.Entity.DescuentoPorPersonas;
import com.tingeso.descuento_por_personas_service.Repository.DescuentoPorPersonasRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DescuentoPorPersonasService {

    @Autowired
    private DescuentoPorPersonasRepository repository;

    // Obtener todos
    public List<DescuentoPorPersonas> obtenerTodos() {
        return repository.findAll();
    }

    // Obtener por ID
    public Optional<DescuentoPorPersonas> obtenerPorId(int id) {
        return repository.findById(id);
    }

    public DescuentoPorPersonas guardar(DescuentoPorPersonas descuento) {
        List<DescuentoPorPersonas> existentes = repository.findRangosSuperpuestos(
                descuento.getMinPersonas(), descuento.getMaxPersonas());

        if (!existentes.isEmpty()) {
            throw new IllegalArgumentException("Ya existe descuento para ese rango de numero de personas.");
        }

        return repository.save(descuento);
    }


    // Eliminar por ID
    public void eliminar(int id) {
        repository.deleteById(id);
    }

    // Obtener descuento por rango numPersonas
    public Optional<DescuentoPorPersonas> obtenerDescuento(int numPersonas) {
        return repository.findByMinPersonasLessThanEqualAndMaxPersonasGreaterThanEqual(numPersonas, numPersonas);
    }
}
