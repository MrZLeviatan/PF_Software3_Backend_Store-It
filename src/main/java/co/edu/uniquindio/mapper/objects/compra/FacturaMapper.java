package co.edu.uniquindio.mapper.objects.compra;

import co.edu.uniquindio.dto.objects.compras.facturas.FacturaDto;
import co.edu.uniquindio.models.entities.objects.compra.Factura;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface FacturaMapper {


    @Mapping(target = "idCompra", source = "compra.id")
    @Mapping(target = "idCliente", source = "cliente.id")
    @Mapping(target = "idSede", source = "sede.id")
    FacturaDto toDto(Factura factura);

}
