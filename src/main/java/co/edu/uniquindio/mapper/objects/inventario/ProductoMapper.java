package co.edu.uniquindio.mapper.objects.inventario;

import co.edu.uniquindio.dto.objects.inventario.producto.ProductoDto;
import co.edu.uniquindio.dto.objects.inventario.producto.RegistroProductoDto;
import co.edu.uniquindio.mapper.objects.almacen.EspacioProductoMapper;
import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        EspacioProductoMapper.class,
})
public interface ProductoMapper {


    @Mapping(target = "idProveedor", source = "proveedor.id")
    ProductoDto toDto(Producto producto);

     @Mapping(target = "imagen", ignore = true)
    @Mapping(target = "proveedor", ignore = true)
    @Mapping(target = "espacioProducto", ignore = true)
    @Mapping(target = "valorVenta", ignore = true)
    Producto toEntity(RegistroProductoDto registroProductoDto);

}
