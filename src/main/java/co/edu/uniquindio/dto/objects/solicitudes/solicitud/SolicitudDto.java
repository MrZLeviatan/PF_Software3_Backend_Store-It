package co.edu.uniquindio.dto.objects.solicitudes.solicitud;

import co.edu.uniquindio.dto.objects.solicitudes.resolucion.ResolucionDto;
import co.edu.uniquindio.models.enums.entities.EstadoProceso;
import co.edu.uniquindio.models.enums.entities.TipoSolicitud;

import java.time.LocalDate;

public record SolicitudDto(

        Long id,
        TipoSolicitud tipoSolicitud,
        String descripcion,
        LocalDate fechaSolicitud,
        EstadoProceso estadoSolicitud,
        Double espacioSolicitado,
        Long idGestorComercial,
        Long idEspacioProducto,
        ResolucionDto resolucion


) {
}
