package co.edu.uniquindio.mapper.objects.almacen;

import co.edu.uniquindio.dto.objects.almacen.subBodega.SubBodegaDto;
import co.edu.uniquindio.models.entities.objects.almacen.SubBodega;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        EspacioProductoMapper.class
})
public interface SubBodegaMapper {

    @Mapping(target = "idBodega", source = "bodega.id")
    SubBodegaDto toDto(SubBodega subBodega);
}
