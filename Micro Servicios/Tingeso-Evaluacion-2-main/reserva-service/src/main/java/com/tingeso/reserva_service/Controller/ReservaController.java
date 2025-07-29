package com.tingeso.reserva_service.Controller;

import com.tingeso.reserva_service.DTO.ReservaDTO;
import com.tingeso.reserva_service.Entity.Reserva;
import com.tingeso.reserva_service.Model.DescuentoPorClienteFrecuente;
import com.tingeso.reserva_service.Model.DescuentoPorPersonas;
import com.tingeso.reserva_service.Model.TarifaDiasEspeciales;
import com.tingeso.reserva_service.Model.TarifaDuracion;
import com.tingeso.reserva_service.Service.ReservaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;

    public ReservaController(ReservaService reservaService) {
        this.reservaService = reservaService;
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<Reserva>> getAllReservas() {
        return ResponseEntity.ok(reservaService.getAllReservas());
    }

    @GetMapping("/getById/{id}")
    public ResponseEntity<Reserva> getReservaById(@PathVariable Integer id) {
        return reservaService.getReservaById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<Reserva> crearReserva(@RequestBody ReservaDTO request) {
        Reserva reserva = reservaService.crearReserva(
                request.getIdUsuario(),
                request.getNumVueltasTiempoMaximo(),
                request.getNumPersonas(),
                request.getNumFrecuenciaCliente(),
                request.getNombreCliente(),
                request.getCorreoCliente(),
                request.getNombreCorreo(),
                request.getCorreosCumpleaneros(),
                request.getFechaInicio(),
                request.getHoraInicio()
        );
        return ResponseEntity.ok(reserva);
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<Reserva> updateReserva(@PathVariable Integer id, @RequestBody Reserva reserva) {
        Reserva updated = reservaService.updateReserva(id, reserva);
        if (updated == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Void> deleteReserva(@PathVariable Integer id) {
        reservaService.deleteReserva(id);
        return ResponseEntity.noContent().build();
    }

    //-------------- ENDPOINTS DE OTROS MICROSERVICIOS -----------
    @GetMapping("/obtenerTarifaNormal")
    public TarifaDuracion getTarifa(@RequestParam int numVueltas) {
        return reservaService.obtenerTarifaNormal(numVueltas);
    }

    @GetMapping("/obtenerDescuentoPorCantidadDePersonas/{numPersonas}")
    public DescuentoPorPersonas getDescuentoPorCantidadDePersonas(@PathVariable int numPersonas) {
        return reservaService.obtenerDescuentoPorCantidadDePersonas(numPersonas);
    }

    @GetMapping("/obtenerDescuentoPorFrecuenciaDeCliente/{numFrecuencia}")
    public DescuentoPorClienteFrecuente getDescuentoPorFrecuenciaDeCliente(@PathVariable int numFrecuencia) {
        return reservaService.obtenerDescuentoPorFrecuenciaDeCliente(numFrecuencia);
    }

    @GetMapping("/obtenerTarifaParaDiasEspeciales")
    public TarifaDiasEspeciales getTarifaParaDiasEspeciales(@RequestParam int numVueltas,
                                                            @RequestParam int cantidadCumpleaneros) {
        return reservaService.obtenerTarifaParaDiasEspeciales(numVueltas, cantidadCumpleaneros);
    }

    @GetMapping("/getInfoReserva/{id}")
    public ResponseEntity<String> obtenerInformacionReserva(@PathVariable int id) {
        Optional<Reserva> reservaOptional = reservaService.getReservaById(id);
        if (reservaOptional.isPresent()) {
            Reserva reserva = reservaOptional.get();
            String informacionReserva = reservaService.obtenerInformacionReservaConComprobante(reserva);
            return ResponseEntity.ok(informacionReserva);
        } else {
            return ResponseEntity.notFound().build();  // Retorna 404 si no se encuentra la reserva
        }
    }

    /**
     * Actualizar una reserva existente por ID.
     */
    @PutMapping("/updateReservaById/{id}")
    public Reserva updateReserva(@PathVariable int id, @RequestBody Reserva updatedReserva) {
        return reservaService.updateReserva(id, updatedReserva);
    }

    /**
     * Eliminar una reserva por ID.
     */
    @DeleteMapping("/deleteReservaById/{id}")
    public void deleteReserva(@PathVariable int id) {
        reservaService.deleteReserva(id);
    }

}
