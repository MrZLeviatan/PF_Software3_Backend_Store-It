package co.edu.uniquindio.dto.objects.solicitudes.resolucion;

import co.edu.uniquindio.models.enums.entities.TipoResolucion;

import java.time.LocalDateTime;

public record ResolucionDto(

        Long id,
        String observaciones,
        LocalDateTime fechayHoraRegistro,
        Long idResolutor,
        TipoResolucion tipoResolucion,

        // Uno de los dos puede estar Null
        Long idIncidente,
        Long idSolicitud



) {
}
