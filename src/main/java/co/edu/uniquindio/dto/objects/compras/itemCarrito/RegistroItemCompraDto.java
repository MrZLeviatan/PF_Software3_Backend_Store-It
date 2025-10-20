package co.edu.uniquindio.dto.objects.compras.itemCarrito;

public record RegistroItemCompraDto(

        Long idProducto,
        int cantidad,
        Long idCliente
) {
}
