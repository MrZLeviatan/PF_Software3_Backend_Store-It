package co.edu.uniquindio.dto.users.personalBodega;

import co.edu.uniquindio.dto.common.datosLaborales.DatosLaboralesDto;
import co.edu.uniquindio.dto.objects.solicitudes.incidentes.incidenteLote.IncidenteLoteDto;
import co.edu.uniquindio.dto.objects.solicitudes.incidentes.incidenteMovimiento.IncidenteMovimientoDto;
import co.edu.uniquindio.dto.objects.inventario.movimiento.participacionMovimiento.ParticipacionMovimientoDto;
import co.edu.uniquindio.dto.users.PersonaDto;
import co.edu.uniquindio.models.enums.users.TipoPersonalBodega;

import java.util.List;

public record PersonalBodegaDto(

        PersonaDto personaDto,
        DatosLaboralesDto datosLaborales,
        TipoPersonalBodega tipoPersonalBodega,
        List<ParticipacionMovimientoDto> participacionesMovimiento,
        List<IncidenteMovimientoDto> incidentesMovimiento,
        List<IncidenteLoteDto> incidentesLote,
        List<IncidenteMovimientoDto> listaResponsabilidad

) {
}
