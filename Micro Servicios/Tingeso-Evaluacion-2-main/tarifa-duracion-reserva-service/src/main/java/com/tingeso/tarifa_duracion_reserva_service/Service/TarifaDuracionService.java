package com.tingeso.tarifa_duracion_reserva_service.Service;

import com.tingeso.tarifa_duracion_reserva_service.Entity.TarifaDuracion;
import com.tingeso.tarifa_duracion_reserva_service.Repository.TarifaDuracionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TarifaDuracionService {

    @Autowired
    private TarifaDuracionRepository repository;

    public List<TarifaDuracion> obtenerTodas() {
        return repository.findAll();
    }

    public Optional<TarifaDuracion> obtenerPorId(int id) {
        return repository.findById(id);
    }

    public TarifaDuracion guardar(TarifaDuracion tarifaDuracion) {
        return repository.save(tarifaDuracion);
    }

    public void eliminar(int id) {
        repository.deleteById(id);
    }

    public TarifaDuracion buscarPorNumVueltas(int numVueltas) {
        return repository.findByNumVueltasTiempoMax(numVueltas);
    }

}
