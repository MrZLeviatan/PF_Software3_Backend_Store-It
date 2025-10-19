package co.edu.uniquindio.dto.users.cliente;

import co.edu.uniquindio.dto.common.UbicacionDto;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;


public record RegistroClienteGoogleDto(

        // Nombre del cliente. No puede estar en blanco y tiene un máximo de 100 caracteres.
        @NotBlank @Length(max = 100) String nombre,

        // Teléfono principal. No puede estar en blanco y tiene un máximo de 15 caracteres.
        @NotBlank @Length(max = 15) String telefono,

        // Código de país del teléfono principal. No puede estar en blanco.
        @NotBlank String codigoPais,

        // Teléfono secundario opcional.
        @Length(max = 15) String telefonoSecundario,

        // Código de país del teléfono secundario.
        String codigoPaisSecundario,

        // Email del cliente. No puede estar en blanco y debe ser un formato de email válido.
        @NotBlank @Email String email,

        // Contraseña del cliente. No puede estar en blanco.
        @NotBlank String password,

        // Ubicación del cliente. No puede ser nula.
        @NotNull UbicacionDto ubicacion
) {
}