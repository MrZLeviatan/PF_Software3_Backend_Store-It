package co.edu.uniquindio.dto.objects.solicitudes.incidentes.incidenteMovimiento;

import co.edu.uniquindio.dto.objects.solicitudes.incidentes.IncidenteDto;
import co.edu.uniquindio.models.enums.entities.TipoIncidenteMovimiento;

public record IncidenteMovimientoDto(

        IncidenteDto incidenteDto,
        Long idMovimiento,
        Long idProveedor,
        Long idPersonalBodega,
        TipoIncidenteMovimiento tipoIncidenteMovimiento

) {
}
