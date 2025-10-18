package co.edu.uniquindio.mapper.objects.inventario.movimiento;

import co.edu.uniquindio.dto.objects.inventario.movimiento.movimientoIngreso.MovimientoIngresoDto;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.MovimientoIngreso;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = MovimientoMapper.class)
public interface MovimientoIngresoMapper {

    @Mapping(source = ".", target = "movimiento")
    @Mapping(target = "idProveedor", source = "proveedor.id")
    @Mapping(target = "idDocumentoIngreso", source = "documentoIngreso.id")
    MovimientoIngresoDto toDto(MovimientoIngreso movimientoIngreso);

}
