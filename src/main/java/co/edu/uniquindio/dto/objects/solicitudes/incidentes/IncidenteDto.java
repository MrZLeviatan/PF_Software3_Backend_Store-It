package co.edu.uniquindio.dto.objects.solicitudes.incidentes;

import co.edu.uniquindio.dto.objects.solicitudes.resolucion.ResolucionDto;
import co.edu.uniquindio.models.enums.entities.EstadoProceso;
import co.edu.uniquindio.models.enums.entities.TipoIncidente;

import java.time.LocalDate;

public record IncidenteDto(

        Long id,
        String descripcion,
        LocalDate fechaRegistro,
        boolean isPerdidas,
        int cantidadAfectada,
        Double valorPerdidaCantidad,
        EstadoProceso estadoProceso,
        TipoIncidente tipoIncidente,
        Long idPersonalBodega,
        ResolucionDto resolucion


) {
}
