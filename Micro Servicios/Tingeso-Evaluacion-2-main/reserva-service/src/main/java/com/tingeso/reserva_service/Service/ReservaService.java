package com.tingeso.reserva_service.Service;

import com.tingeso.reserva_service.Entity.Comprobante;
import com.tingeso.reserva_service.Entity.Reserva;
import com.tingeso.reserva_service.Model.DescuentoPorClienteFrecuente;
import com.tingeso.reserva_service.Model.DescuentoPorPersonas;
import com.tingeso.reserva_service.Model.TarifaDiasEspeciales;
import com.tingeso.reserva_service.Model.TarifaDuracion;
import com.tingeso.reserva_service.Repository.ReservaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;

@Service
public class ReservaService {

    @Autowired
    private ReservaRepository reservaRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private ComprobanteService comprobanteService;

    //----------------------------- CRUD -----------------------------
    //Obtener todas las reservas
    public List<Reserva> getAllReservas() {
        return reservaRepository.findAll();
    }

    //Obtener reserva por Id
    public Optional<Reserva> getReservaById(Integer id) {
        return reservaRepository.findById(id);
    }

    public Reserva updateReserva(Integer id, Reserva reservaActualizada) {
        return reservaRepository.findById(id).map(reserva -> {
            reserva.setNombreCliente(reservaActualizada.getNombreCliente());
            return reservaRepository.save(reserva);
        }).orElse(null);
    }

    public void deleteReserva(Integer id) {
        reservaRepository.deleteById(id);
    }

    //----------------- METODOS PARA OBTENER DATOS DE MICROSERVICIOS ---------------------

    public TarifaDuracion obtenerTarifaNormal(int numVueltas_TiempoMaximo){
        TarifaDuracion tarifaDuracion = restTemplate.getForObject("http://tarifa-duracion-reserva-service/api/tarifasDuracion/buscarPorVueltas?numVueltas=" + numVueltas_TiempoMaximo, TarifaDuracion.class);
        return tarifaDuracion;
    }

    public DescuentoPorPersonas obtenerDescuentoPorCantidadDePersonas(int numPersonas){
        DescuentoPorPersonas descuentoPorPersonas = restTemplate.getForObject("http://descuento-por-personas-service/api/descuentoPorNumPersonas/buscarDescuento/" + numPersonas, DescuentoPorPersonas.class);
        return descuentoPorPersonas;
    }

    public DescuentoPorClienteFrecuente obtenerDescuentoPorFrecuenciaDeCliente(int numFrecuencia){
        DescuentoPorClienteFrecuente descuentoPorClienteFrecuente = restTemplate.getForObject("http://descuento-por-cliente-frecuente/api/descuentoPorClienteFrecuente/obtenerPorFrecuencia/" + numFrecuencia, DescuentoPorClienteFrecuente.class);
        return descuentoPorClienteFrecuente;
    }

    public TarifaDiasEspeciales obtenerTarifaParaDiasEspeciales(int numVueltas, int cantidadCumpleaneros){
        TarifaDiasEspeciales tarifaDiasEspeciales = restTemplate.getForObject("http://tarifa-dias-especiales/api/tarifasDiasEspeciales/buscarTarifaPorVueltasYPersonas?numVueltas=" + numVueltas + "&cantidadCumpleaneros=" + cantidadCumpleaneros, TarifaDiasEspeciales.class);
        return tarifaDiasEspeciales;
    }

    public boolean saberSiEsFinDeSemana_Feriado(LocalDate fecha){
        boolean esDiaEspecial = restTemplate.getForObject("http://tarifa-dias-especiales/api/tarifasDiasEspeciales/esDiaEspecial?fecha=" + fecha, Boolean.class);
        return esDiaEspecial;
    }

    //----------------- LOGICA AVANZADA ---------------------
    public boolean esReservaPosible(LocalDate fecha, LocalTime horaInicio, LocalTime horaFin) {
        List<Reserva> reservasQueSeCruzan = reservaRepository.findReservasQueSeCruzan(fecha, horaInicio, horaFin);
        return reservasQueSeCruzan.isEmpty();
    }

    public Reserva crearReserva(int id_usuario, int numVueltas_TiempoMaximo, int numPersonas,
                             int numFrecuenciaCliente, String nombreCliente,
                             String correoCliente, Map<String, String> nombreCorreo, List<String> correosCumpleaneros,
                             LocalDate fechaInicio, LocalTime horaInicio){

        // Crear la reserva
        Reserva reserva = new Reserva();
        reserva.setIdUsuario(id_usuario);
        reserva.setNum_vueltas_tiempo_maximo(numVueltas_TiempoMaximo);
        reserva.setNum_personas(numPersonas);
        reserva.setFechaHora(LocalDateTime.now());
        reserva.setFechaInicio(fechaInicio);
        reserva.setHoraInicio(horaInicio);
        reserva.setNombreCliente(nombreCliente);

        //Obtener datos de los microservicios
        boolean esFinDeSemanaFeriado = saberSiEsFinDeSemana_Feriado(fechaInicio);
        TarifaDuracion tarifaDuracionNormal = obtenerTarifaNormal(numVueltas_TiempoMaximo);
        TarifaDiasEspeciales tarifaDiasEspeciales = obtenerTarifaParaDiasEspeciales(numVueltas_TiempoMaximo, numPersonas);
        DescuentoPorPersonas descuentoPorCantidadDePersonas = obtenerDescuentoPorCantidadDePersonas(numPersonas);
        DescuentoPorClienteFrecuente descuentoPorClienteFrecuente = obtenerDescuentoPorFrecuenciaDeCliente(numFrecuenciaCliente);

        double tarifa = 0.0; //Comprobante
        double descuentoPorPersonas = descuentoPorCantidadDePersonas.getDescuento(); //Comprobante
        double descuentoPorFrecuencia = descuentoPorClienteFrecuente.getDescuento(); //Comprobante
        int maxCumpleanerosConDescuento = tarifaDiasEspeciales.getMaxCumpleanerosConDescuento(); //Comprobante
        double descuentoCumpleaneros = tarifaDiasEspeciales.getDescuentoCumpleaneros(); //Comprobante
        int duracionTotal = tarifaDiasEspeciales.getDuracion_total();

        if (esFinDeSemanaFeriado){
            tarifa = tarifaDiasEspeciales.getTarifa();
        }else{
            tarifa = tarifaDuracionNormal.getTarifa();
        }

        reserva.setDuracion_total(duracionTotal);
        reserva.setPrecio_regular(tarifa);
        reserva.setHoraFin(horaInicio.plusMinutes(duracionTotal));


        if (!esReservaPosible(fechaInicio, horaInicio, reserva.getHoraFin())) {
            throw new RuntimeException("Ya existe una reserva en ese horario.");
        }


        Comprobante comprobante = comprobanteService.crearComprobante(tarifa,
                descuentoCumpleaneros, maxCumpleanerosConDescuento,
                descuentoPorPersonas, descuentoPorFrecuencia,
                nombreCliente, correoCliente,
                nombreCorreo, correosCumpleaneros);

        reserva.setComprobante(comprobante);

        reserva.setComprobante(comprobante);

        reserva = reservaRepository.save(reserva);

        // --- Aquí empieza el envío de correos ---
        String resumenReserva = obtenerInformacionReservaConComprobante(reserva);

        File archivoPdf = generarPDFReserva(resumenReserva);

        nombreCorreo.values().stream()
                .filter(correo -> correo != null && !correo.trim().isEmpty())
                .forEach(correo -> enviarCorreoReservaConPDF(correo, "Resumen de reserva", archivoPdf));

        enviarCorreoReservaConPDF(correoCliente, "Resumen de reserva", archivoPdf);

        if (archivoPdf != null && archivoPdf.exists()) {
            archivoPdf.delete();
        }

        return reserva;
    }


    public String obtenerInformacionReservaConComprobante(Reserva reserva) {
        Comprobante comprobante = reserva.getComprobante();
        String nombreCliente = reserva.getNombreCliente();

        DateTimeFormatter formatterFechaHora = DateTimeFormatter.ofPattern("yy/MM/dd HH:mm:ss");
        DateTimeFormatter formatterFecha = DateTimeFormatter.ofPattern("yy/MM/dd");
        DateTimeFormatter formatterHora = DateTimeFormatter.ofPattern("HH:mm:ss");

        StringBuilder informacionReserva = new StringBuilder();
        informacionReserva.append("========= INFORMACIÓN DE LA RESERVA =========\n");
        informacionReserva.append("Código de la reserva: ").append(reserva.getId()).append("\n");
        informacionReserva.append("Fecha y hora de la reserva: ").append(reserva.getFechaHora().format(formatterFechaHora)).append("\n");
        informacionReserva.append("Fecha de inicio: ").append(reserva.getFechaInicio().format(formatterFecha)).append("\n");
        informacionReserva.append("Hora de inicio: ").append(reserva.getHoraInicio().format(formatterHora)).append("\n");
        informacionReserva.append("Hora de fin: ").append(reserva.getHoraFin().format(formatterHora)).append("\n");
        informacionReserva.append("Número de vueltas o tiempo máximo reservado: ").append(reserva.getNum_vueltas_tiempo_maximo()).append("\n");
        informacionReserva.append("Cantidad de personas incluidas: ").append(reserva.getNum_personas()).append("\n");
        // Si el nombreCliente puede contener pipe, toma solo la primera parte (ejemplo: "Juan|Algo")
        informacionReserva.append("Nombre de la persona que hizo la reserva: ").append(nombreCliente.split("\\|")[0]).append("\n\n");

        // Aquí llamamos a tu método que formatea el comprobante
        informacionReserva.append(comprobanteService.formatearComprobante(comprobante));

        return informacionReserva.toString();
    }

    // ==================== GENERACIÓN DE PDF Y CORREO ====================

    public File generarPDFReserva(String resumen) {
        String filePath = "reserva_comprobante.pdf";
        File file = new File(filePath);

        try {
            PdfWriter writer = new PdfWriter(file);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf);
            document.add(new Paragraph(resumen));
            document.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return file;
    }

    public void enviarCorreoReservaConPDF(String correo, String cuerpo, File archivoPdf) {
        try {
            MimeMessage mensaje = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mensaje, true);
            helper.setTo(correo);
            helper.setSubject("Resumen de tu Reserva");
            helper.setText(cuerpo, true);
            helper.addAttachment("Resumen_Reserva.pdf", archivoPdf);
            mailSender.send(mensaje);
            System.out.println("Correo enviado a " + correo);
        } catch (MessagingException e) {
            System.err.println("Error al enviar correo a " + correo + ": " + e.getMessage());
        }
    }


}
