package co.edu.uniquindio.mapper.objects.compra;


import co.edu.uniquindio.dto.objects.compras.carritoCompra.CarritoCompraDto;
import co.edu.uniquindio.models.entities.objects.compra.CarritoCompra;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",
        uses = {
            ItemsCarritoMapper.class
})
public interface CarritoCompraMapper {


    @Mapping(target = "idCliente", source = "cliente.id")
    CarritoCompraDto toDto(CarritoCompra carritoCompra);



}
