package co.edu.uniquindio.dto.common.unidadAlmacenamiento;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;

public record RegistroUnidadAlmacenamientoDto(


        @NotNull
        @DecimalMin(value = "0.1", message = "El largo debe ser mayor a 0")
        Double largo,

        @NotNull
        @DecimalMin(value = "0.1", message = "El ancho debe ser mayor a 0")
        Double ancho,

        @NotNull
        @DecimalMin(value = "0.1", message = "El alto debe ser mayor a 0")
        Double alto

) {
}
