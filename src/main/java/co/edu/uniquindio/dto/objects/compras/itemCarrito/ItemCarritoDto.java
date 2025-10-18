package co.edu.uniquindio.dto.objects.compras.itemCarrito;

public record ItemCarritoDto(

        Long id,
        Long idProducto,
        int cantidad,
        Double valorTotal,
        Long idCarritoCompra

) {
}
