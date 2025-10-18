package co.edu.uniquindio.dto.objects.inventario.producto;

import co.edu.uniquindio.dto.objects.almacen.espacioProducto.EspacioProductoDto;
import co.edu.uniquindio.models.enums.entities.TipoProducto;

public record ProductoDto(

        Long id,
        String codigoBarras,
        String nombre,
        Double valorCompra,
        Double valorVenta,
        TipoProducto tipoProducto,
        EspacioProductoDto espacioProducto,
        Long idProveedor

) {
}
