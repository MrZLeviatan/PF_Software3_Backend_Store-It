package co.edu.uniquindio.mapper.objects.notificaciones;

import co.edu.uniquindio.dto.objects.notificacion.ReferenciaEntidadDto;
import co.edu.uniquindio.models.entities.objects.notificaciones.ReferenciaEntidad;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ReferenciaEntidadMapper {


    @Mapping(target = "idNotificacion", source = "notificacion.id")
    ReferenciaEntidadDto toDto(ReferenciaEntidad ref);
}
