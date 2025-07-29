package com.tingeso.usuario_service.Service;

import com.tingeso.usuario_service.Entity.Usuario;
import com.tingeso.usuario_service.Model.Reserva;
import com.tingeso.usuario_service.Repository.UsuarioRepository;
import org.springframework.stereotype.Service;

import org.springframework.http.HttpEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final RestTemplate restTemplate;

    public UsuarioService(UsuarioRepository usuarioRepository, RestTemplate restTemplate) {
        this.usuarioRepository = usuarioRepository;
        this.restTemplate = restTemplate;
    }

    // CRUD básico
    public List<Usuario> getAllUsuarios() {
        return usuarioRepository.findAll();
    }

    public Optional<Usuario> getUsuarioById(int id) {
        return usuarioRepository.findById(id);
    }

    public Usuario updateUsuario(int id, Usuario usuario) {
        if (usuarioRepository.existsById(id)) {
            usuario.setId(id);
            return usuarioRepository.save(usuario);
        }
        return null;
    }

    public boolean deleteUsuario(int id) {
        if (usuarioRepository.existsById(id)) {
            usuarioRepository.deleteById(id);
            return true;
        }
        return false;
    }

    public Usuario register(String name, String email, String contrasenia, LocalDate birthday) {
        if (usuarioRepository.findByEmail(email) != null) {
            throw new RuntimeException("El cliente con el correo ya existe.");
        }

        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setName(name);
        nuevoUsuario.setEmail(email);
        nuevoUsuario.setContrasena(contrasenia);
        nuevoUsuario.setBirthday(birthday);
        nuevoUsuario.setNum_visitas_al_mes(0);
        nuevoUsuario.setRol("Cliente");

        return usuarioRepository.save(nuevoUsuario);
    }

    public Usuario login(String email, String contrasena) {
        Usuario usuario = usuarioRepository.findByEmail(email);

        if (usuario == null) throw new RuntimeException("No se encontró el cliente con el email " + email);

        if (!usuario.getContrasena().equals(contrasena)) {
            throw new RuntimeException("Contraseña incorrecta");
        }

        return usuario;
    }

    public Reserva generarReservaCliente(int idUsuario, Reserva reserva) {
        reserva.setIdUsuario(idUsuario);

        // Obtener usuario
        Optional<Usuario> usuarioOpt = getUsuarioById(idUsuario);
        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("Usuario no encontrado con ID: " + idUsuario);
        }

        Usuario usuario = usuarioOpt.get();
        String correoCliente = usuario.getEmail();
        String nombreCliente = usuario.getName();
        int numFrecuencia = usuario.getNum_visitas_al_mes();

        // Setear datos del cliente en la reserva
        reserva.setCorreoCliente(correoCliente);
        reserva.setNombreCliente(nombreCliente);
        reserva.setNumFrecuenciaCliente(numFrecuencia);

        // Generar la reserva en el microservicio
        HttpEntity<Reserva> request = new HttpEntity<>(reserva);
        Reserva reservaNew = restTemplate.postForObject("http://reserva-service/api/reservas/create", request, Reserva.class);

        // Aumentar la frecuencia del cliente
        usuario.setNum_visitas_al_mes(numFrecuencia + 1);
        usuarioRepository.save(usuario); // ✅ Asegúrate de tener acceso al repositorio

        return reservaNew;
    }

    public Reserva generarReservaAdmin(String correoCliente, Reserva reserva){
        Usuario usuario = usuarioRepository.findByEmail(correoCliente);
        int idUsuario = usuario.getId();
        String nombreCliente = usuario.getName();

        reserva.setIdUsuario(idUsuario);
        reserva.setCorreoCliente(correoCliente);
        reserva.setNombreCliente(nombreCliente);
        reserva.setNumFrecuenciaCliente(0); //Aqui es cero dado que es el admin quien hace la reserva

        // Generar la reserva en el microservicio
        HttpEntity<Reserva> request = new HttpEntity<>(reserva);
        Reserva reservaNew = restTemplate.postForObject("http://reserva-service/api/reservas/create", request, Reserva.class);

        return reservaNew;
    }

}
