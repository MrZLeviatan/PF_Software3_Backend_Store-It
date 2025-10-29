package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

// DTO para el logueo tradiciónal utilizando el email y password
public record LoginDto(
        // El email del usuario. No puede estar en blanco y debe tener un formato de correo válido.
        @NotBlank @Email String email,
        // La contraseña del usuario. No puede estar en blanco.
        @NotBlank String password
) {
}