package co.edu.uniquindio.mapper.objects.compra;

import co.edu.uniquindio.dto.objects.compras.itemCarrito.ItemCarritoDto;
import co.edu.uniquindio.models.entities.objects.compra.ItemsCarrito;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ItemsCarritoMapper {


    @Mapping(target = "idProducto", source = "producto.id")
    @Mapping(target = "idCarritoCompra", source = "carritoCompra.id")
    ItemCarritoDto toDto(ItemsCarrito itemCarrito);

}
