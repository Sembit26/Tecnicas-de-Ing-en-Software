package com.tingeso.descuento_por_cliente_frecuente.Repository;

import com.tingeso.descuento_por_cliente_frecuente.Entity.DescuentoPorClienteFrecuente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface DescuentoPorClienteFrecuenteRepository extends JpaRepository<DescuentoPorClienteFrecuente, Integer> {
    Optional<DescuentoPorClienteFrecuente> findByMinVisitasLessThanEqualAndMaxVisitasGreaterThanEqual(int visitas, int visitas2);

    @Query("SELECT d FROM DescuentoPorClienteFrecuente d WHERE " +
            "(:minVisitas BETWEEN d.minVisitas AND d.maxVisitas) OR " +
            "(:maxVisitas BETWEEN d.minVisitas AND d.maxVisitas) OR " +
            "(d.minVisitas BETWEEN :minVisitas AND :maxVisitas)")
    List<DescuentoPorClienteFrecuente> findRangosSuperpuestos(@Param("minVisitas") int min, @Param("maxVisitas") int max);

}
