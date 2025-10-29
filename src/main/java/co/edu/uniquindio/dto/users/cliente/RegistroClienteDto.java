package co.edu.uniquindio.dto.users.cliente;

import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.common.user.CrearUserDto;
import co.edu.uniquindio.models.enums.users.TipoCliente;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;

/**
 * Su propósito es encapsular las credenciales básicas necesarias para un registro de
 * clientes nuevos.
 */
public record RegistroClienteDto(


        // Nombre del cliente. No puede ser NUlo y debe tener una longitud maxima de 100 caracteres.
        @NotBlank @Length(max = 100)String nombre,

        // Teléfono principal del cliente. No puede estar vacío y debe tener una longitud minima de 8 caracteres.
        @NotBlank @Length(max = 15) String telefono,

        // Código de pais del teléfono
        @NotBlank String codigoPais,

        // Teléfono secundario opcional.
        @Length(max = 15) String telefonoSecundario,

        // Código de país del teléfono secundario.
        String codigoPaisSecundario,

        // Información del usuario (correo y contraseña). Debe ser válido y no nulo.
        @NotNull @Valid CrearUserDto user,

        // Información de la ubicación del cliente. Debe ser válida y no nula.
        @NotNull @Valid UbicacionDto ubicacion,

        // Tipo de cliente (Ej: NATURAL o JURÍDICO). No puede ser nulo.
        @NotNull TipoCliente tipoCliente,

        // Número de identificación tributaria (NIT) del cliente, opcional.
        String nit

) {
}
