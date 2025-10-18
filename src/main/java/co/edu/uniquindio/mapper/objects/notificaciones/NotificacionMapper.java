package co.edu.uniquindio.mapper.objects.notificaciones;

import co.edu.uniquindio.dto.objects.notificacion.NotificacionDto;
import co.edu.uniquindio.models.entities.objects.notificaciones.Notificacion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        ReferenciaEntidadMapper.class
})
public interface NotificacionMapper {

    @Mapping(target = "idReceptor", source = "receptor.id")
    NotificacionDto toDto(Notificacion notificacion);

}
