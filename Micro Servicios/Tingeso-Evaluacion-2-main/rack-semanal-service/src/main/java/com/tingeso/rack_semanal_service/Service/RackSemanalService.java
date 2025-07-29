package com.tingeso.rack_semanal_service.Service;

import com.tingeso.rack_semanal_service.DTO.HorarioOcupadoDTO;
import com.tingeso.rack_semanal_service.DTO.HorariosDisponiblesDTO;
import com.tingeso.rack_semanal_service.DTO.ReservaDTO;
import com.tingeso.rack_semanal_service.Entity.RackSemanal;
import com.tingeso.rack_semanal_service.Repository.RackSemanalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class RackSemanalService {

    @Autowired
    private RackSemanalRepository rackSemanalRepository;

    @Autowired
    RestTemplate restTemplate;

    public RackSemanal save(RackSemanal rackSemanal) {
        return rackSemanalRepository.save(rackSemanal);
    }

    public List<RackSemanal> findAll() {
        return rackSemanalRepository.findAll();
    }

    public Optional<RackSemanal> findById(int id) {
        return rackSemanalRepository.findById(id);
    }

    public void deleteById(int id) {
        rackSemanalRepository.deleteById(id);
    }

    public boolean saberSiEsFinDeSemana_Feriado(LocalDate fecha){
        boolean esDiaEspecial = restTemplate.getForObject("http://tarifa-dias-especiales/api/tarifasDiasEspeciales/esDiaEspecial?fecha=" + fecha, Boolean.class);
        return esDiaEspecial;
    }

    public List<ReservaDTO> getReservasByFecha(LocalDate fecha) {
        String url = "http://reserva-service/api/reservas/getAll";
        // Obtener todas las reservas desde el microservicio reserva
        ReservaDTO[] todasReservas = restTemplate.getForObject(url, ReservaDTO[].class);

        if (todasReservas == null) {
            return Collections.emptyList();
        }

        // Filtrar por la fecha dada
        return Arrays.stream(todasReservas)
                .filter(r -> r.getFechaInicio().isEqual(fecha))
                .collect(Collectors.toList());
    }

    public Optional<ReservaDTO> obtenerReservaPorFechaHoraInicioYHoraFin(LocalDate fechaInicio, LocalTime horaInicio, LocalTime horaFin) {
        String url = "http://reserva-service/api/reservas/getAll";
        ReservaDTO[] todasReservas = restTemplate.getForObject(url, ReservaDTO[].class);

        if (todasReservas == null || todasReservas.length == 0) {
            return Optional.empty();
        }

        return Arrays.stream(todasReservas)
                .filter(r ->
                        r.getFechaInicio().equals(fechaInicio) &&
                                r.getHoraInicio().equals(horaInicio) &&
                                r.getHoraFin().equals(horaFin)
                )
                .findFirst();
    }

    public List<HorarioOcupadoDTO> obtenerTodosLosHorariosOcupados() {
        // Llamar al microservicio de reserva (ajusta la URL según tu servicio)
        String url = "http://reserva-service/api/reservas/getAll";
        ReservaDTO[] reservas = restTemplate.getForObject(url, ReservaDTO[].class);

        if (reservas == null) {
            return Collections.emptyList();
        }

        // Agrupar todas las reservas por fecha
        Map<LocalDate, List<String>> mapaHorarios = new HashMap<>();

        for (ReservaDTO reserva : reservas) {
            String horario = reserva.getHoraInicio() + " - " + reserva.getHoraFin();
            mapaHorarios.computeIfAbsent(reserva.getFechaInicio(), k -> new ArrayList<>()).add(horario);
        }

        // Convertir el mapa a la lista de DTOs
        List<HorarioOcupadoDTO> resultado = new ArrayList<>();
        for (Map.Entry<LocalDate, List<String>> entry : mapaHorarios.entrySet()) {
            resultado.add(new HorarioOcupadoDTO(entry.getKey(), entry.getValue()));
        }

        // Ordenar por fecha para mejor legibilidad
        resultado.sort(Comparator.comparing(HorarioOcupadoDTO::getFecha));
        return resultado;
    }

    public List<HorariosDisponiblesDTO> obtenerHorariosDisponiblesProximosDosMeses(LocalDate fechaCualquieraDelMes) {
        List<HorariosDisponiblesDTO> horariosDisponiblesTotales = new ArrayList<>();


        LocalDate fechaInicio = fechaCualquieraDelMes.withDayOfMonth(1);
        LocalDate fechaFin = fechaInicio.plusMonths(2).withDayOfMonth(
                fechaInicio.plusMonths(2).lengthOfMonth()
        );

        for (LocalDate fecha = fechaInicio; !fecha.isAfter(fechaFin); fecha = fecha.plusDays(1)) {
            LocalTime horaInicioDia;
            if (saberSiEsFinDeSemana_Feriado(fecha)) {
                horaInicioDia = LocalTime.of(10, 0);
            } else {
                horaInicioDia = LocalTime.of(14, 0);
            }
            LocalTime horaFinDia = LocalTime.of(22, 0);

            // Obtener reservas del día (ocupados)
            List<ReservaDTO> reservas = getReservasByFecha(fecha);
            reservas.sort(Comparator.comparing(ReservaDTO::getHoraInicio));

            List<String> horariosLibres = new ArrayList<>();

            LocalTime horaLibreActual = horaInicioDia;

            for (ReservaDTO reserva : reservas) {
                LocalTime inicioReserva = reserva.getHoraInicio();
                LocalTime finReserva = reserva.getHoraFin();

                // Si hay espacio libre antes de la reserva actual
                if (horaLibreActual.isBefore(inicioReserva)) {
                    Duration duracionLibre = Duration.between(horaLibreActual, inicioReserva);
                    if (duracionLibre.toMinutes() >= 30) {
                        horariosLibres.add(horaLibreActual + " - " + inicioReserva);
                    }
                }

                // Actualizar el inicio del bloque libre al fin de la reserva
                if (horaLibreActual.isBefore(finReserva)) {
                    horaLibreActual = finReserva;
                }
            }

            // Agregar el último bloque libre si existe
            if (horaLibreActual.isBefore(horaFinDia)) {
                Duration duracionLibre = Duration.between(horaLibreActual, horaFinDia);
                if (duracionLibre.toMinutes() >= 30) {
                    horariosLibres.add(horaLibreActual + " - " + horaFinDia);
                }
            }

            horariosDisponiblesTotales.add(new HorariosDisponiblesDTO(fecha, horariosLibres));
        }

        return horariosDisponiblesTotales;
    }



}
