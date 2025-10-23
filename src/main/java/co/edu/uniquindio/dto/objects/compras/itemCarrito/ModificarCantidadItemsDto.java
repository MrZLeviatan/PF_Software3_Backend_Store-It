package co.edu.uniquindio.dto.objects.compras.itemCarrito;

public record ModificarCantidadItemsDto(

        Long idProducto,
        Long idCliente,
        int cantidadAgregar

) {
}
