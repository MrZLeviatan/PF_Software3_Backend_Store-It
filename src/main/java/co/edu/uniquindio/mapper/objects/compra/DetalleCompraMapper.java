package co.edu.uniquindio.mapper.objects.compra;

import co.edu.uniquindio.dto.objects.compras.detalleCompra.DetalleCompraDto;
import co.edu.uniquindio.mapper.objects.inventario.movimiento.MovimientoRetiroMapper;
import co.edu.uniquindio.models.entities.objects.compra.DetalleCompra;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
        MovimientoRetiroMapper.class,
})
public interface DetalleCompraMapper {


    @Mapping(target = "idProducto", source = "producto.id")
    @Mapping(target = "idCompra", source = "compra.id")
    DetalleCompraDto toDto(DetalleCompra detalleCompra);

}
