package com.tingeso.rack_semanal_service.Repository;

import com.tingeso.rack_semanal_service.Entity.RackSemanal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RackSemanalRepository extends JpaRepository<RackSemanal, Integer> {
}
