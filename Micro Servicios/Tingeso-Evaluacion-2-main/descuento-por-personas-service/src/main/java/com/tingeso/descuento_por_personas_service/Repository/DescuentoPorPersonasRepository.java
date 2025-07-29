package com.tingeso.descuento_por_personas_service.Repository;

import com.tingeso.descuento_por_personas_service.Entity.DescuentoPorPersonas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DescuentoPorPersonasRepository extends JpaRepository<DescuentoPorPersonas, Integer> {

    @Query("SELECT d FROM DescuentoPorPersonas d WHERE " +
            "(:min BETWEEN d.minPersonas AND d.maxPersonas) OR " +
            "(:max BETWEEN d.minPersonas AND d.maxPersonas) OR " +
            "(d.minPersonas BETWEEN :min AND :max)")
    List<DescuentoPorPersonas> findRangosSuperpuestos(@Param("min") int min, @Param("max") int max);

    Optional<DescuentoPorPersonas> findByMinPersonasLessThanEqualAndMaxPersonasGreaterThanEqual(int min, int max);
}
