package co.edu.uniquindio.dto.objects.compras.carritoCompra;

import co.edu.uniquindio.dto.objects.compras.itemCarrito.ItemCarritoDto;

import java.util.List;

public record CarritoCompraDto(

        Long id,
        Long idCliente,
        Double totalValor,
        List<ItemCarritoDto> itemsCarrito
) {
}
