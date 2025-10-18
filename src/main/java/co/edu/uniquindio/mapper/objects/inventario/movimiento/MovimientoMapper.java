package co.edu.uniquindio.mapper.objects.inventario.movimiento;

import co.edu.uniquindio.dto.objects.inventario.movimiento.MovimientoDto;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.Movimiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        ParticipacionMovimientoMapper.class,
})
public interface MovimientoMapper {

    @Mapping(target = "idLote", source = "lote.id")
    @Mapping(target = "idGestorComercial", source = "gestorComercial.id")
    MovimientoDto toDto(Movimiento movimiento);


}
