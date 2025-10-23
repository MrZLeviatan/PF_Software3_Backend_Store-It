package co.edu.uniquindio.mapper.objects.almacen;

import co.edu.uniquindio.dto.objects.almacen.espacioProducto.EspacioProductoDto;
import co.edu.uniquindio.dto.objects.almacen.espacioProducto.RegistroEspacioProductoDto;
import co.edu.uniquindio.mapper.objects.inventario.LoteMapper;
import co.edu.uniquindio.mapper.objects.solicitudes.SolicitudMapper;
import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        LoteMapper.class,
        SolicitudMapper.class,
        UnidadAlmacenamientoMapper.class

})
public interface EspacioProductoMapper {

    @Mapping(target = "idSubBodega", source = "subBodega.id")
    @Mapping(target = "idProducto", source = "producto.id")
    EspacioProductoDto toDto(EspacioProducto espacioProducto);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "subBodega", ignore = true)
    @Mapping(target = "producto", ignore = true)
    @Mapping(target = "cantidadTotal", ignore = true)
    @Mapping(target = "unidadAlmacenamiento", source = "unidadAlmacenamiento")
    @Mapping(target = "unidadAlmacenamiento.volumenOcupado", ignore = true)
    @Mapping(target = "unidadAlmacenamiento.estadoUnidad", constant = "DISPONIBLE")
    @Mapping(target = "estadoEspacio", constant = "ACTIVO")
    @Mapping(target = "solicitudes", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "lotes", expression = "java(new java.util.ArrayList<>())")
    EspacioProducto toEntity(RegistroEspacioProductoDto espacioProductoDto);

}
