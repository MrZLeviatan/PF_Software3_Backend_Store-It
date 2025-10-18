package co.edu.uniquindio.mapper.objects.compra;

import co.edu.uniquindio.dto.objects.compras.compra.CompraDto;
import co.edu.uniquindio.models.entities.objects.compra.Compra;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.MovimientoRetiro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring",uses = {
        DetalleCompraMapper.class,
        FacturaMapper.class,
        MovimientoRetiro.class
})
public interface CompraMapper {

    @Mapping(target = "idCliente", source = "cliente.id")
    CompraDto compraDtoToCompra(Compra compra);

}
