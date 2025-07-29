package com.tingeso.reportes_service.Service;

import com.tingeso.reportes_service.DTO.ComprobanteDTO;
import com.tingeso.reportes_service.DTO.ReservaDTO;
import com.tingeso.reportes_service.Entity.Reportes;
import com.tingeso.reportes_service.Repository.ReportesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ReportesService {

    @Autowired
    private ReportesRepository reportesRepository;

    @Autowired
    private RestTemplate restTemplate;

    public List<Reportes> obtenerTodos() {
        return reportesRepository.findAll();
    }

    public Optional<Reportes> obtenerPorId(Long id) {
        return reportesRepository.findById(id);
    }

    public Reportes guardar(Reportes reporte) {
        return reportesRepository.save(reporte);
    }

    public void eliminar(Long id) {
        reportesRepository.deleteById(id);
    }

    public List<ReservaDTO> obtenerReservasPorRangoDeMeses(LocalDate fechaInicio, LocalDate fechaFin) {
        if (fechaInicio.isAfter(fechaFin)) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser posterior a la fecha de fin.");
        }

        // Ajustar fechas al primer día del mes y al último día del mes
        LocalDate inicioMes = fechaInicio.withDayOfMonth(1);
        LocalDate finMes = fechaFin.withDayOfMonth(fechaFin.lengthOfMonth());

        // Llamar al microservicio de reservas
        String url = "http://reserva-service/api/reservas/getAll";
        ResponseEntity<ReservaDTO[]> response = restTemplate.getForEntity(url, ReservaDTO[].class);
        ReservaDTO[] reservasArray = response.getBody();

        if (reservasArray == null) {
            return Collections.emptyList();
        }

        // Filtrar las reservas que están dentro del rango de fechas
        return Arrays.stream(reservasArray)
                .filter(reserva -> !reserva.getFechaInicio().isBefore(inicioMes) && !reserva.getFechaInicio().isAfter(finMes))
                .collect(Collectors.toList());
    }

    public Map<String, List<ReservaDTO>> agruparReservasPorMesYAnio(List<ReservaDTO> reservas) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM");
        return reservas.stream()
                .collect(Collectors.groupingBy(
                        reserva -> reserva.getFechaInicio().format(formatter),
                        LinkedHashMap::new,
                        Collectors.toList()
                ));
    }

    public Map<String, Map<String, Double>> generarReporteIngresosPorVueltas(LocalDate fechaInicio, LocalDate fechaFin) {
        List<ReservaDTO> reservas = obtenerReservasPorRangoDeMeses(fechaInicio, fechaFin);
        Map<String, List<ReservaDTO>> reservasAgrupadas = agruparReservasPorMesYAnio(reservas);

        Map<String, Map<String, Double>> reporte = new TreeMap<>();

        for (Map.Entry<String, List<ReservaDTO>> entrada : reservasAgrupadas.entrySet()) {
            String mesAnio = entrada.getKey();
            List<ReservaDTO> reservasDelMes = entrada.getValue();

            Map<String, Double> ingresosPorVueltas = new HashMap<>();
            ingresosPorVueltas.put("10", 0.0);
            ingresosPorVueltas.put("15", 0.0);
            ingresosPorVueltas.put("20", 0.0);
            ingresosPorVueltas.put("TOTAL", 0.0);

            for (ReservaDTO reserva : reservasDelMes) {
                int vueltas = reserva.getNum_vueltas_tiempo_maximo();
                ComprobanteDTO comprobante = reserva.getComprobante();
                if (comprobante != null) {
                    double monto = comprobante.getMonto_total_iva();
                    if (vueltas == 10 || vueltas == 15 || vueltas == 20) {
                        ingresosPorVueltas.put(String.valueOf(vueltas),
                                ingresosPorVueltas.get(String.valueOf(vueltas)) + monto);
                    }
                    ingresosPorVueltas.put("TOTAL", ingresosPorVueltas.get("TOTAL") + monto);
                }
            }

            reporte.put(mesAnio, ingresosPorVueltas);
        }

        return reporte;
    }


    public Map<String, Map<String, Double>> generarReporteIngresosPorGrupoDePersonas(LocalDate fechaInicio, LocalDate fechaFin) {
        // Obtener reservas filtradas por rango de fechas
        List<ReservaDTO> reservas = obtenerReservasPorRangoDeMeses(fechaInicio, fechaFin);

        // Agrupar reservas por mes y año
        Map<String, List<ReservaDTO>> reservasAgrupadas = agruparReservasPorMesYAnio(reservas);

        // Mapa para almacenar el reporte final
        Map<String, Map<String, Double>> reporte = new TreeMap<>();

        // Iterar sobre cada grupo de reservas por mes y año
        for (Map.Entry<String, List<ReservaDTO>> entrada : reservasAgrupadas.entrySet()) {
            String mesAnio = entrada.getKey();
            List<ReservaDTO> reservasDelMes = entrada.getValue();

            // Inicializar mapa para ingresos por grupo de personas
            Map<String, Double> ingresosPorGrupo = new LinkedHashMap<>();
            ingresosPorGrupo.put("1-2", 0.0);
            ingresosPorGrupo.put("3-5", 0.0);
            ingresosPorGrupo.put("6-10", 0.0);
            ingresosPorGrupo.put("11-15", 0.0);
            ingresosPorGrupo.put("TOTAL", 0.0);

            // Calcular ingresos por grupo
            for (ReservaDTO reserva : reservasDelMes) {
                int personas = reserva.getNum_personas();
                ComprobanteDTO comprobante = reserva.getComprobante();

                if (comprobante != null) {
                    double monto = Math.round(comprobante.getMonto_total_iva());

                    if (personas >= 1 && personas <= 2) {
                        ingresosPorGrupo.put("1-2", ingresosPorGrupo.get("1-2") + monto);
                    } else if (personas >= 3 && personas <= 5) {
                        ingresosPorGrupo.put("3-5", ingresosPorGrupo.get("3-5") + monto);
                    } else if (personas >= 6 && personas <= 10) {
                        ingresosPorGrupo.put("6-10", ingresosPorGrupo.get("6-10") + monto);
                    } else if (personas >= 11 && personas <= 15) {
                        ingresosPorGrupo.put("11-15", ingresosPorGrupo.get("11-15") + monto);
                    }

                    ingresosPorGrupo.put("TOTAL", ingresosPorGrupo.get("TOTAL") + monto);
                }
            }

            // Agregar resultados al reporte final
            reporte.put(mesAnio, ingresosPorGrupo);
        }

        return reporte;
    }



}
