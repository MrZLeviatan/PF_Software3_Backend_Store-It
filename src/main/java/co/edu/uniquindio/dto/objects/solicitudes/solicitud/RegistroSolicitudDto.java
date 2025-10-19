package co.edu.uniquindio.dto.objects.solicitudes.solicitud;

import jakarta.validation.constraints.NotNull;

public record RegistroSolicitudDto(

        @NotNull Double espacioSolicitado,
        String descripcion,
        Long idEspacio,
        Long idGestorComercial

) {
}
