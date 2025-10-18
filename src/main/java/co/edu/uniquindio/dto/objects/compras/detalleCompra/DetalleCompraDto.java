package co.edu.uniquindio.dto.objects.compras.detalleCompra;

import co.edu.uniquindio.dto.objects.inventario.movimiento.movimientoRetiro.MovimientoRetiroDto;

import java.util.List;

public record DetalleCompraDto(


        Long id,
        Long idProducto,
        int cantidad,
        Double valorDetalle,
        Long idCompra,
        List<MovimientoRetiroDto> movimientoRetiros
) {
}
