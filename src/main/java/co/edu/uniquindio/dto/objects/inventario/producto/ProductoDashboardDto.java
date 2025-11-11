package co.edu.uniquindio.dto.objects.inventario.producto;

public record ProductoDashboardDto(

        Long idProducto,
        String nombre,
        String codigoBarras,
        String tipo,
        String proveedor,
        int cantidadDisponible,
        long unidadesVendidas,
        double ingresosGenerados,
        String imagen
) {
}
