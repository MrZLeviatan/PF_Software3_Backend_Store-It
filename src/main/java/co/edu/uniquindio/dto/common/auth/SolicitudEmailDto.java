package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

/**
  Su propósito es encapsular y transferir el email del usuario de forma segura y
  eficiente, para operaciones de recuperación de contraseña.
 */
public record SolicitudEmailDto(
        // El email del usuario. No puede ser nulo y debe tener un formato de correo electrónico válido.
        @NotNull @Email String email
) {
}