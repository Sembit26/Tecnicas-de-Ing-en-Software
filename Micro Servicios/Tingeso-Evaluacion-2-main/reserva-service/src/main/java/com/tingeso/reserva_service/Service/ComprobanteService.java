package com.tingeso.reserva_service.Service;

import com.tingeso.reserva_service.Entity.Comprobante;
import com.tingeso.reserva_service.Entity.DetallePagoPorPersona;
import com.tingeso.reserva_service.Repository.ComprobanteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


@Service
public class ComprobanteService {

    @Autowired
    private ComprobanteRepository comprobanteRepository;

    public List<Comprobante> getAll() {
        return comprobanteRepository.findAll();
    }

    public Comprobante getById(Long id) {
        return comprobanteRepository.findById(id).orElse(null);
    }

    public void delete(Long id) {
        comprobanteRepository.deleteById(id);
    }

    //------------------ LOGICA GENERAR COMPROBANTE ---------------------

    public Comprobante crearComprobante(
            double tarifa,
            double descuentoCumpleaneros,
            int maxCumpleanerosConDescuento,
            double descuentoPorCantidadDePersonas,
            double descuentoPorFrecuenciaCliente,
            String nombreCliente,
            String correoCliente,
            Map<String, String> nombreCorreo,
            List<String> correosCumpleaneros
    ) {
        double totalSinIva = 0.0;
        int cumpleDescuentoAsignado = 0;
        List<DetallePagoPorPersona> detalles = new ArrayList<>();

        // Procesar acompañantes (grupo excepto cliente principal)
        for (Map.Entry<String, String> entry : nombreCorreo.entrySet()) {
            String nombre = entry.getKey();
            String correo = entry.getValue();

            double descuentoAplicado = 0.0;
            String tipoDescuento = "Ninguno";

            if (correosCumpleaneros.contains(correo) && cumpleDescuentoAsignado < maxCumpleanerosConDescuento) {
                descuentoAplicado = descuentoCumpleaneros;
                tipoDescuento = "Cumpleaños";
                cumpleDescuentoAsignado++;
            } else if (descuentoPorCantidadDePersonas > 0) {
                descuentoAplicado = descuentoPorCantidadDePersonas;
                tipoDescuento = "Grupal";
            }

            double montoSinIva = tarifa * (1 - descuentoAplicado);
            double iva = montoSinIva * 0.19;
            double totalConIva = montoSinIva + iva;
            totalSinIva += montoSinIva;

            DetallePagoPorPersona detalle = new DetallePagoPorPersona();
            detalle.setNombrePersona(nombre);
            detalle.setPrecioBase(tarifa);
            detalle.setTipoDescuento(tipoDescuento);
            detalle.setPorcentajeDescuento(Math.round(descuentoAplicado * 100.0));
            detalle.setMontoFinalSinIva(Math.round(montoSinIva * 100.0) / 100.0);
            detalle.setIva(Math.round(iva * 100.0) / 100.0);
            detalle.setTotalConIva(Math.round(totalConIva * 100.0) / 100.0);

            detalles.add(detalle);
        }

        // Procesar cliente principal **separado**, con prioridad en descuentos (cumpleaños, frecuencia, grupal)
        double descuentoAplicadoCliente = 0.0;
        String tipoDescuentoCliente = "Ninguno";

        if (correosCumpleaneros.contains(correoCliente) && cumpleDescuentoAsignado < maxCumpleanerosConDescuento) {
            descuentoAplicadoCliente = descuentoCumpleaneros;
            tipoDescuentoCliente = "Cumpleaños";
            cumpleDescuentoAsignado++;
        } else if (descuentoPorFrecuenciaCliente > 0) {
            descuentoAplicadoCliente = descuentoPorFrecuenciaCliente;
            tipoDescuentoCliente = "Frecuencia";
        } else if (descuentoPorCantidadDePersonas > 0) {
            descuentoAplicadoCliente = descuentoPorCantidadDePersonas;
            tipoDescuentoCliente = "Grupal";
        }

        double montoSinIvaCliente = tarifa * (1 - descuentoAplicadoCliente);
        double ivaCliente = montoSinIvaCliente * 0.19;
        double totalConIvaCliente = montoSinIvaCliente + ivaCliente;
        totalSinIva += montoSinIvaCliente;

        DetallePagoPorPersona detalleCliente = new DetallePagoPorPersona();
        detalleCliente.setNombrePersona(nombreCliente);
        detalleCliente.setPrecioBase(tarifa);
        detalleCliente.setTipoDescuento(tipoDescuentoCliente);
        detalleCliente.setPorcentajeDescuento(Math.round(descuentoAplicadoCliente * 100.0));
        detalleCliente.setMontoFinalSinIva(Math.round(montoSinIvaCliente * 100.0) / 100.0);
        detalleCliente.setIva(Math.round(ivaCliente * 100.0) / 100.0);
        detalleCliente.setTotalConIva(Math.round(totalConIvaCliente * 100.0) / 100.0);

        detalles.add(detalleCliente);

        double ivaTotal = totalSinIva * 0.19;
        double totalConIva = totalSinIva + ivaTotal;

        Comprobante comprobante = new Comprobante();
        comprobante.setPrecio_final(Math.round(totalSinIva * 100.0) / 100.0);
        comprobante.setIva(Math.round(ivaTotal * 100.0) / 100.0);
        comprobante.setMonto_total_iva(Math.round(totalConIva * 100.0) / 100.0);
        comprobante.setDetallesPago(detalles);

        return comprobanteRepository.save(comprobante);
    }


    public String formatearComprobante(Comprobante comprobante) {
        StringBuilder sb = new StringBuilder();

        sb.append("========= RESUMEN DEL COMPROBANTE =========\n");
        sb.append(String.format("Subtotal (sin IVA): %.2f\n", comprobante.getPrecio_final()));
        sb.append(String.format("IVA: %.2f\n", comprobante.getIva()));
        sb.append(String.format("Total con IVA: %.2f\n", comprobante.getMonto_total_iva()));
        sb.append("-------------------------------------------\n");
        sb.append("Detalle por persona:\n\n");

        for (DetallePagoPorPersona detalle : comprobante.getDetallesPago()) {
            sb.append("- ").append(detalle.getNombrePersona()).append("\n");
            sb.append(String.format("  Precio Base (sin IVA): %.2f\n", detalle.getPrecioBase()));
            sb.append(String.format("  Tipo de Descuento: %s\n", detalle.getTipoDescuento()));
            sb.append(String.format("  Porcentaje de Descuento: %.1f%%\n", detalle.getPorcentajeDescuento()));
            sb.append(String.format("  Monto Final sin IVA: %.2f\n", detalle.getMontoFinalSinIva()));
            sb.append(String.format("  IVA: %.2f\n", detalle.getIva()));
            sb.append(String.format("  Total con IVA: %.2f\n\n", detalle.getTotalConIva()));
        }

        sb.append("===========================================\n");
        return sb.toString();
    }


}
