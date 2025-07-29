package com.tingeso.rack_semanal_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HorariosDisponiblesDTO {
    private LocalDate fecha;
    private List<String> horariosDisponibles;
}
