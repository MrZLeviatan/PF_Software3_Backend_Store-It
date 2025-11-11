package co.edu.uniquindio.dto.objects.inventario.producto;

public record ResumenDashboardDto(

        ProductoDashboardDto productoMasVendido,
        ProductoDashboardDto productoConMasUnidades,
        ProductoDashboardDto productoConMenosUnidades
) {
}
