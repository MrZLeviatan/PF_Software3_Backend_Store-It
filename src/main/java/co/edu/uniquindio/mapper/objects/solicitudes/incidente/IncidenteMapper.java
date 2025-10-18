package co.edu.uniquindio.mapper.objects.solicitudes.incidente;

import co.edu.uniquindio.dto.objects.solicitudes.incidentes.IncidenteDto;
import co.edu.uniquindio.mapper.objects.solicitudes.ResolucionMapper;
import co.edu.uniquindio.models.entities.objects.solicitudes.incidentes.Incidente;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        ResolucionMapper.class,
})
public interface IncidenteMapper {


    @Mapping(target = "idPersonalBodega", source = "reportadoPor.id")
    IncidenteDto toDto(Incidente incidente);

}
