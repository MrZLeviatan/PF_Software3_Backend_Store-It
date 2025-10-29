package co.edu.uniquindio.dto.common.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/**
 * Su propósito es encapsular las credenciales básicas necesarias para un registro,
 * como el email y la contraseña.
 */
public record CrearUserDto(

        // El email del nuevo usuario. No puede estar en blanco y debe tener un formato de email válido.
        @Email @NotBlank String email,
        // La contraseña del nuevo usuario. No puede estar en blanco y debe tener un mínimo de 8 caracteres.
        @NotBlank @Size(min = 8) String password
) {
}
