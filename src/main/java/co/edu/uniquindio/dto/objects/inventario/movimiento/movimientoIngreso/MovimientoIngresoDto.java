package co.edu.uniquindio.dto.objects.inventario.movimiento.movimientoIngreso;

import co.edu.uniquindio.dto.objects.inventario.movimiento.MovimientoDto;
import co.edu.uniquindio.models.enums.entities.EstadoMovimientoIngreso;

public record MovimientoIngresoDto(


        MovimientoDto movimientoDto,
        Long idProveedor,
        Long idDocumentoIngreso,
        EstadoMovimientoIngreso estadoMovimientoIngreso

) {
}
