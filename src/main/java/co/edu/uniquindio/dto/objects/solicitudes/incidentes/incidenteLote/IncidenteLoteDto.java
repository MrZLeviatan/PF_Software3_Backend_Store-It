package co.edu.uniquindio.dto.objects.solicitudes.incidentes.incidenteLote;

import co.edu.uniquindio.dto.objects.solicitudes.incidentes.IncidenteDto;
import co.edu.uniquindio.models.enums.entities.TipoIncidenteLote;

public record IncidenteLoteDto(

        IncidenteDto incidenteDto,
        Long idLote,
        Double valorDaniosPropiedad,
        TipoIncidenteLote tipoIncidenteLote

) {
}
