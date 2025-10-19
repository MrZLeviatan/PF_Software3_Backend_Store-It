package co.edu.uniquindio.mapper.objects.solicitudes;

import co.edu.uniquindio.dto.objects.solicitudes.solicitud.RegistroSolicitudDto;
import co.edu.uniquindio.dto.objects.solicitudes.solicitud.SolicitudDto;
import co.edu.uniquindio.models.entities.objects.solicitudes.Solicitud;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        ResolucionMapper.class,
})
public interface SolicitudMapper {


    @Mapping(target = "idGestorComercial", source = "gestorComercial.id")
    @Mapping(target = "idEspacioProducto", source = "espacioProducto.id")
    SolicitudDto toDto(Solicitud solicitud);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "tipoSolicitud", constant = "AMPLIAR_ESPACIO_PRODUCTO")
    @Mapping(target = "fechaSolicitud", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "estadoSolicitud", constant = "PENDIENTE")
    @Mapping(target = "gestorComercial", ignore = true)
    @Mapping(target = "espacioProducto", ignore = true)
    @Mapping(target = "resolucion", ignore = true)
    Solicitud toEntity(RegistroSolicitudDto registroSolicitudDto);



}
