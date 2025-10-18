package co.edu.uniquindio.mapper.objects.almacen;

import co.edu.uniquindio.dto.objects.almacen.bodega.BodegaDto;
import co.edu.uniquindio.models.entities.objects.almacen.Bodega;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        SubBodegaMapper.class,
})
public interface BodegaMapper {


    @Mapping(target = "idSede", source = "sede.id")
    BodegaDto toDto(Bodega bodega);

}
