package co.edu.uniquindio.service.common.impl;


import co.edu.uniquindio.dto.objects.inventario.producto.ProductoDashboardDto;
import co.edu.uniquindio.dto.objects.inventario.producto.ResumenDashboardDto;
import co.edu.uniquindio.models.entities.objects.inventario.Producto;
import co.edu.uniquindio.repository.objects.compra.DetalleCompraRepo;
import co.edu.uniquindio.repository.objects.inventario.ProductoRepo;
import co.edu.uniquindio.service.common.DashboardProductoService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class DashboardProductoServiceImpl implements DashboardProductoService {

    private final ProductoRepo productoRepo;
    private final DetalleCompraRepo detalleCompraRepo;

    @Override
    public ResumenDashboardDto obtenerResumenProductos() {
        // --- Obtener productos ordenados por cantidad total ---
        List<Producto> productosDesc = productoRepo.findTopByCantidadTotalDesc();
        List<Producto> productosAsc = productoRepo.findTopByCantidadTotalAsc();

        Producto productoMasUnidades = productosDesc.isEmpty() ? null : productosDesc.get(0);
        Producto productoMenosUnidades = productosAsc.isEmpty() ? null : productosAsc.get(0);

        // --- 2️⃣ Obtener productos más vendidos ---
        List<Object[]> masVendidos = detalleCompraRepo.obtenerProductosMasVendidos();

        ProductoDashboardDto productoMasVendidoDto = null;
        if (!masVendidos.isEmpty()) {
            Object[] fila = masVendidos.get(0);
            Long idProducto = (Long) fila[0];
            long cantidadVendida = ((Number) fila[1]).longValue();

            Producto producto = productoRepo.findById(idProducto).orElse(null);
            if (producto != null) {
                productoMasVendidoDto = mapToDashboardDto(producto, cantidadVendida);
            }
        }

        // --- Crear DTOs para los otros dos productos ---
        ProductoDashboardDto conMasUnidades = productoMasUnidades != null ?
                mapToDashboardDto(productoMasUnidades, 0) : null;

        ProductoDashboardDto conMenosUnidades = productoMenosUnidades != null ?
                mapToDashboardDto(productoMenosUnidades, 0) : null;

        // --- Construir resumen ---
        return new ResumenDashboardDto(productoMasVendidoDto, conMasUnidades, conMenosUnidades);
    }

    private ProductoDashboardDto mapToDashboardDto(Producto producto, long unidadesVendidas) {
        int cantidadDisponible = producto.getEspacioProducto() != null ?
                producto.getEspacioProducto().getCantidadTotal() : 0;

        double ingresos = producto.getValorVenta() * unidadesVendidas;

        return new ProductoDashboardDto(
                producto.getId(),
                producto.getNombre(),
                producto.getCodigoBarras(),
                producto.getTipoProducto().name(),
                producto.getProveedor().getNombre(),
                cantidadDisponible,
                unidadesVendidas,
                ingresos,
                producto.getImagen()
        );
    }
}
