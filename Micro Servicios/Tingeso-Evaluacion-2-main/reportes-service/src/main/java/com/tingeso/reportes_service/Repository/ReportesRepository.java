package com.tingeso.reportes_service.Repository;

import com.tingeso.reportes_service.Entity.Reportes;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportesRepository extends JpaRepository<Reportes, Long> {
    // Aquí podrías agregar consultas personalizadas más adelante si quieres
}
