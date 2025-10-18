package co.edu.uniquindio.mapper.objects.almacen;

import co.edu.uniquindio.dto.objects.almacen.espacioProducto.EspacioProductoDto;
import co.edu.uniquindio.mapper.objects.inventario.LoteMapper;
import co.edu.uniquindio.mapper.objects.solicitudes.SolicitudMapper;
import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        LoteMapper.class,
        SolicitudMapper.class

})
public interface EspacioProductoMapper {


    @Mapping(target = "idSubBodega", source = "subBodega.id")
    @Mapping(target = "idProducto", source = "producto.id")
    EspacioProductoDto toDto(EspacioProducto espacioProducto);
}
