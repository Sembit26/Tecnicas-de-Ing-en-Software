package com.tingeso.reserva_service.Repository;

import com.tingeso.reserva_service.Entity.Comprobante;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComprobanteRepository extends JpaRepository<Comprobante, Long> {
}
