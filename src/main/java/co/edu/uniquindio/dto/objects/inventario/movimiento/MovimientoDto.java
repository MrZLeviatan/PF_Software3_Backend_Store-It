package co.edu.uniquindio.dto.objects.inventario.movimiento;

import co.edu.uniquindio.dto.objects.inventario.movimiento.participacionMovimiento.ParticipacionMovimientoDto;
import co.edu.uniquindio.dto.objects.solicitudes.incidentes.incidenteMovimiento.IncidenteMovimientoDto;
import co.edu.uniquindio.models.enums.entities.TipoMovimiento;

import java.time.LocalDate;
import java.util.List;

public record MovimientoDto(

        Long id,
        LocalDate fechaSolicitud,
        int cantidad,
        String observaciones,
        TipoMovimiento tipoMovimiento,
        Long idLote,
        List<IncidenteMovimientoDto> incidenteMovimientos,
        LocalDate fechaConfirmacion,
        Long idGestorComercial,
        List<ParticipacionMovimientoDto> participaciones


) {
}
