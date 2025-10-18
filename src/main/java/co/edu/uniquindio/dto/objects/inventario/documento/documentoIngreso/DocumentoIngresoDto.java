package co.edu.uniquindio.dto.objects.inventario.documento.documentoIngreso;

import co.edu.uniquindio.dto.objects.inventario.movimiento.movimientoIngreso.MovimientoIngresoDto;
import co.edu.uniquindio.models.enums.entities.EstadoProceso;

import java.time.LocalDate;

public record DocumentoIngresoDto(

        Long id,
        String codigoIngreso,
        Double areaTotal,
        LocalDate fechaEntrega,
        int cantidad,
        EstadoProceso estadoProceso,

        Long idGestorComercial,
        Long idProveedor,
        Long idLote,
        MovimientoIngresoDto movimientoIngreso

) {
}
