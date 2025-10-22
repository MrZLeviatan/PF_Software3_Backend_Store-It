package co.edu.uniquindio.mapper.objects.almacen;

import co.edu.uniquindio.dto.common.unidadAlmacenamiento.UnidadAlmacenamientoDto;
import co.edu.uniquindio.models.embeddable.UnidadAlmacenamiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UnidadAlmacenamientoMapper {


    @Mapping(target = "volumenTotal", expression = "java(unidadAlmacenamiento.getVolumenTotal())")
    @Mapping(target = "volumenDisponible", expression = "java(unidadAlmacenamiento.getVolumenDisponible())")
    @Mapping(target = "porcentajeOcupacion", expression = "java(unidadAlmacenamiento.getPorcentajeOcupacion())")
    UnidadAlmacenamientoDto toDto(UnidadAlmacenamiento unidadAlmacenamiento);

}
