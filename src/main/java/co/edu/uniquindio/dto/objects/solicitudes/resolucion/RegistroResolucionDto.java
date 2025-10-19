package co.edu.uniquindio.dto.objects.solicitudes.resolucion;

import co.edu.uniquindio.models.enums.entities.TipoResolucion;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record RegistroResolucionDto(

        @NotBlank String observaciones,
        @NotNull Long idAdmin,
        TipoResolucion tipoResolucion,
        Long idIncidente,
        Long idSolicitud


) {
}
