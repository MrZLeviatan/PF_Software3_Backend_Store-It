package co.edu.uniquindio.mapper.objects.solicitudes.incidente;

import co.edu.uniquindio.dto.objects.solicitudes.incidentes.incidenteMovimiento.IncidenteMovimientoDto;
import co.edu.uniquindio.models.entities.objects.solicitudes.incidentes.IncidenteMovimiento;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = {
        IncidenteMapper.class
})
public interface IncidenteMovimientoMapper {

    @Mapping(source = ".", target = "incidente")
    @Mapping(target = "idMovimiento", source = "movimiento.id")
    @Mapping(target = "idProveedor", source = "proveedor.id")
    @Mapping(target = "idPersonalBodega", source = "responsable.id")
    IncidenteMovimientoDto toDto(IncidenteMovimiento incidenteMovimiento);
}
