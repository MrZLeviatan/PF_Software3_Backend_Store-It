package co.edu.uniquindio.dto.users.proveedor;

import co.edu.uniquindio.dto.objects.inventario.documento.documentoIngreso.DocumentoIngresoDto;
import co.edu.uniquindio.dto.objects.inventario.producto.ProductoDto;

import java.util.List;

public record ProveedorDto(

        Long id,
        String nombre,
        String email,
        String telefono,
        String marca,
        List<ProductoDto> productos,
        List<DocumentoIngresoDto> documentoIngresos

) {
}
