package com.tingeso.tarifa_dias_especiales.Repository;

import com.tingeso.tarifa_dias_especiales.Entity.TarifaDiasEspeciales;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TarifaDiasEspecialesRepository extends JpaRepository<TarifaDiasEspeciales, Integer> {
    List<TarifaDiasEspeciales> findByNumVueltasTiempoMax(int numVueltasTiempoMax);
}
