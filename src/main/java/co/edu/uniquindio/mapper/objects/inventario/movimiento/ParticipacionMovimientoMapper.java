package co.edu.uniquindio.mapper.objects.inventario.movimiento;

import co.edu.uniquindio.dto.objects.inventario.movimiento.participacionMovimiento.ParticipacionMovimientoDto;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.ParticipacionMovimiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ParticipacionMovimientoMapper {

    @Mapping(target = "idMovimiento", source = "movimiento.id")
    @Mapping(target = "idPersonalBodega", source = "personalBodega.id")
    ParticipacionMovimientoDto toDto(ParticipacionMovimiento participacionMovimiento);

}
