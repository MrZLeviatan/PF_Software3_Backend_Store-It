package co.edu.uniquindio.mapper.objects.solicitudes.incidente;

import co.edu.uniquindio.dto.objects.solicitudes.incidentes.incidenteLote.IncidenteLoteDto;
import co.edu.uniquindio.models.entities.objects.solicitudes.incidentes.IncidenteLote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        IncidenteMapper.class,
})
public interface IncidenteLoteMapper {

    @Mapping(source = ".", target = "incidente")
    @Mapping(target = "idLote", source = "lote.id")
    IncidenteLoteDto toDto(IncidenteLote incidenteLote);

}
