package co.edu.uniquindio.mapper.objects.almacen;

import co.edu.uniquindio.dto.objects.almacen.sede.SedeDto;
import co.edu.uniquindio.models.entities.objects.almacen.Sede;
import org.mapstruct.Mapper;


@Mapper(componentModel = "spring", uses = {
        BodegaMapper.class,
})
public interface SedeMapper {



    SedeDto toDto(Sede sede);
}
