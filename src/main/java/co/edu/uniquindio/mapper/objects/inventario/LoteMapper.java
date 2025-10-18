package co.edu.uniquindio.mapper.objects.inventario;

import co.edu.uniquindio.dto.objects.inventario.lote.LoteDto;
import co.edu.uniquindio.mapper.objects.inventario.movimiento.MovimientoIngresoMapper;
import co.edu.uniquindio.mapper.objects.solicitudes.incidente.IncidenteLoteMapper;
import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.MovimientoRetiro;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        MovimientoIngresoMapper.class,
        MovimientoRetiro.class,
        IncidenteLoteMapper.class,

})
public interface LoteMapper {

    @Mapping(target = "idEspacioProducto", source = "espacioProducto.id")
    LoteDto toDto(Lote lote);
}
