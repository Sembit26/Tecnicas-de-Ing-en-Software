package com.tingeso.descuento_por_cliente_frecuente.Service;

import com.tingeso.descuento_por_cliente_frecuente.Entity.DescuentoPorClienteFrecuente;
import com.tingeso.descuento_por_cliente_frecuente.Repository.DescuentoPorClienteFrecuenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DescuentoPorClienteFrecuenteService {

    @Autowired
    private DescuentoPorClienteFrecuenteRepository repository;

    public DescuentoPorClienteFrecuente guardar(DescuentoPorClienteFrecuente descuento) {
        List<DescuentoPorClienteFrecuente> existentes = repository
                .findRangosSuperpuestos(descuento.getMinVisitas(), descuento.getMaxVisitas());

        if (!existentes.isEmpty()) {
            throw new IllegalArgumentException("Ya existe un descuento para el rango especificado");
        }

        return repository.save(descuento);
    }

    public List<DescuentoPorClienteFrecuente> obtenerTodos() {
        return repository.findAll();
    }

    public Optional<DescuentoPorClienteFrecuente> obtenerPorId(int id) {
        return repository.findById(id);
    }

    public void eliminar(int id) {
        repository.deleteById(id);
    }

    public Optional<DescuentoPorClienteFrecuente> obtenerDescuento(int visitas) {
        return repository.findByMinVisitasLessThanEqualAndMaxVisitasGreaterThanEqual(visitas, visitas);
    }
}
