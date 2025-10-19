package co.edu.uniquindio.mapper.objects.solicitudes;

import co.edu.uniquindio.dto.objects.solicitudes.resolucion.RegistroResolucionDto;
import co.edu.uniquindio.dto.objects.solicitudes.resolucion.ResolucionDto;
import co.edu.uniquindio.models.entities.objects.solicitudes.Resolucion;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ResolucionMapper {

    @Mapping(target = "idResolutor", source = "resolutor.id")
    @Mapping(target = "idIncidente", source = "incidente.id")
    @Mapping(target = "idSolicitud", source = "solicitud.id")
    ResolucionDto toDto(Resolucion resolucion);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "fechayHoraRegistro", expression = "java(java.time.LocalDateTime.now())")
    @Mapping(target = "resolutor", ignore = true)
    @Mapping(target = "solicitud", ignore = true)
    @Mapping(target = "incidente", ignore = true)
    Resolucion toEntity(RegistroResolucionDto registroResolucionDto);

}
