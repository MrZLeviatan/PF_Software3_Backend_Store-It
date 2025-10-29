package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

/**
  Su propósito es encapsular el correo electrónico del usuario y el código de verificación
  que ha ingresado.
 */
public record VerificacionCodigoDto(
        // El email del usuario. No puede estar en blanco y debe tener un formato válido.
        @NotBlank @Email String email,
        // El código de verificación.
        String codigo

) {
}
