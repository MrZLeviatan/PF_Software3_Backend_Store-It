package co.edu.uniquindio.mapper.objects.inventario;

import co.edu.uniquindio.dto.objects.inventario.lote.LoteDto;
import co.edu.uniquindio.dto.objects.inventario.lote.RegistroLoteDto;
import co.edu.uniquindio.mapper.objects.inventario.movimiento.MovimientoIngresoMapper;
import co.edu.uniquindio.mapper.objects.inventario.movimiento.MovimientoRetiroMapper;
import co.edu.uniquindio.mapper.objects.solicitudes.incidente.IncidenteLoteMapper;
import co.edu.uniquindio.models.entities.objects.inventario.Lote;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        MovimientoIngresoMapper.class,
        MovimientoRetiroMapper.class,
        IncidenteLoteMapper.class,

})
public interface LoteMapper {

    @Mapping(target = "idEspacioProducto", source = "espacioProducto.id")
    LoteDto toDto(Lote lote);


    @Mapping(target = "id", ignore = true)
    @Mapping(target = "codigoLote", ignore = true)
    @Mapping(target = "fechaIngreso", expression = "java(java.time.LocalDate.now())")
    @Mapping(target = "estadoLote", constant = "EN_VERIFICACION")
    @Mapping(target = "valorTotal", ignore = true)
    @Mapping(target = "espacioProducto", ignore = true)
    @Mapping(target = "movimientos", expression = "java(new java.util.ArrayList<>())")
    @Mapping(target = "incidenteLotes", expression = "java(new java.util.ArrayList<>())")
    Lote toEntity(RegistroLoteDto registroLoteDto);
}
