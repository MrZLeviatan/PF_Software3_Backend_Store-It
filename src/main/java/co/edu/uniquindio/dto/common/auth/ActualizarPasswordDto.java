package co.edu.uniquindio.dto.common.auth;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

// Su propósito es transferir de manera segura el email del usuario y la nueva contraseña
public record ActualizarPasswordDto (
        // El email del usuario cuya contraseña será actualizada. No puede ser nulo ni estar en blanco.
        @NotBlank @NotNull String email,
        // La nueva contraseña. No puede estar en blanco y debe tener un mínimo de 8 caracteres.
        @NotBlank @Size(min = 8) String nuevaPassword
){
}