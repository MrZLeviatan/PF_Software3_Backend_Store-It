package co.edu.uniquindio.mapper.objects.inventario.movimiento;

import co.edu.uniquindio.dto.objects.inventario.movimiento.movimientoRetiro.MovimientoRetiroDto;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.MovimientoRetiro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MovimientoMapper.class)
public interface MovimientoRetiroMapper {

    @Mapping(source = ".", target = "movimiento")
    @Mapping(target = "idCliente", source = "cliente.id")
    @Mapping(target = "idDocumentoRetiro", source = "documentoRetiro.id")
    @Mapping(target = "idDetalleCompra", source = "detalleCompra.id")
    MovimientoRetiroDto toDto(MovimientoRetiro movimientoRetiro);

}
