package com.tingeso.reserva_service.Repository;

import com.tingeso.reserva_service.Entity.DetallePagoPorPersona;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DetallePagoPorPersonaRepository extends JpaRepository<DetallePagoPorPersona, Long> {
}
