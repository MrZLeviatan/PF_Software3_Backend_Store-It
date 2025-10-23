package co.edu.uniquindio.mapper.users;

import co.edu.uniquindio.dto.common.datosLaborales.DatosLaboralesDto;
import co.edu.uniquindio.models.embeddable.DatosLaborales;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface DatosLaboralesMapper {


    @Mapping(target = "idBodega", source = "bodega.id")
    DatosLaboralesDto toDto (DatosLaborales datosLaborales);

}
