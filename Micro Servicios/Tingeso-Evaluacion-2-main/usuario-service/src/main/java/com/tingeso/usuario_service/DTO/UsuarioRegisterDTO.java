package com.tingeso.usuario_service.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UsuarioRegisterDTO {
    private String name;
    private String email;
    private String contrasena;
    private LocalDate birthday;
}
