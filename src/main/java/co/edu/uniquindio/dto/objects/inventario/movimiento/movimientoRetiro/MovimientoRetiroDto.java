package co.edu.uniquindio.dto.objects.inventario.movimiento.movimientoRetiro;

import co.edu.uniquindio.dto.objects.inventario.movimiento.MovimientoDto;

public record MovimientoRetiroDto(

        MovimientoDto movimiento,
        Long idCliente,
        Long idDocumentoRetiro,
        Long idDetalleCompra

) {
}
