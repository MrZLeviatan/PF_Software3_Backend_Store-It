package co.edu.uniquindio.dto.users.cliente;

import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.common.user.CrearUserDto;
import co.edu.uniquindio.models.enums.users.TipoCliente;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

public record RegistroClienteDto(


        @NotBlank @Length(max = 100)String nombre,

        @NotBlank @Length(max = 15) String telefono,

        @NotBlank String codigoPais,

        // Teléfono secundario opcional.
        @Length(max = 15) String telefonoSecundario,

        // Código de país del teléfono secundario.
        String codigoPaisSecundario,

        // Información del usuario (correo y contraseña). Debe ser válido y no nulo.
        @NotNull @Valid CrearUserDto user,

        // Información de la ubicación del cliente. Debe ser válida y no nula.
        @NotNull @Valid UbicacionDto ubicacion,

        // Tipo de cliente (Ej: NATURAL o JURIDICO). No puede ser nulo.
        @NotNull TipoCliente tipoCliente,

        // Número de identificación tributaria (NIT) del cliente, opcional.
        String nit

) {
}
