package co.edu.uniquindio.dto.users.proveedor;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import org.hibernate.validator.constraints.Length;

public record RegistroProveedorDto(


        @NotBlank @Length(max = 100)String nombre,

        @NotBlank @Email String email,

        @NotBlank @Length(max = 15) String telefono,

        @NotBlank @Length(max = 100) String marca

) {
}
