package co.edu.uniquindio.mapper.users;

import co.edu.uniquindio.dto.users.personalBodega.PersonalBodegaDto;
import co.edu.uniquindio.mapper.objects.inventario.movimiento.ParticipacionMovimientoMapper;
import co.edu.uniquindio.mapper.objects.solicitudes.incidente.IncidenteLoteMapper;
import co.edu.uniquindio.mapper.objects.solicitudes.incidente.IncidenteMovimientoMapper;
import co.edu.uniquindio.models.entities.users.PersonalBodega;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        PersonaMapper.class,
        ParticipacionMovimientoMapper.class,
        IncidenteMovimientoMapper.class,
        IncidenteLoteMapper.class
})
public interface PersonalBodegaMapper {


    @Mapping(source = ".", target = "persona")
    @Mapping(target = "datosLaborales.idBodega", source = "datosLaborales.bodega.id")
    PersonalBodegaDto toDto(PersonalBodega personaBodega);


}
