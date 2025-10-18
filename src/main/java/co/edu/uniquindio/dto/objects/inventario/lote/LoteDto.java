package co.edu.uniquindio.dto.objects.inventario.lote;

import co.edu.uniquindio.dto.objects.solicitudes.incidentes.incidenteLote.IncidenteLoteDto;
import co.edu.uniquindio.dto.objects.inventario.movimiento.movimientoIngreso.MovimientoIngresoDto;
import co.edu.uniquindio.dto.objects.inventario.movimiento.movimientoRetiro.MovimientoRetiroDto;
import co.edu.uniquindio.models.enums.entities.EstadoLote;

import java.time.LocalDate;
import java.util.List;

public record LoteDto(

       Long id,
       String codigoLote,
       LocalDate fechaIngreso,
       LocalDate fechaVencimiento,
       EstadoLote estadoLote,
       int cantidadDisponible,
       Double valorTotal,
       Double areaTotal,
       Long idEspacioProducto,

       List<MovimientoIngresoDto> movimientoIngreso,
       List<MovimientoRetiroDto> movimientoRetiro,
       List<IncidenteLoteDto> incidenteLotes


) {
}
