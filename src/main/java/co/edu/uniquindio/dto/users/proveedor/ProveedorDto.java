package co.edu.uniquindio.dto.users.proveedor;

import co.edu.uniquindio.dto.objects.inventario.documento.documentoIngreso.DocumentoIngresoDto;
import co.edu.uniquindio.dto.objects.inventario.producto.ProductoDto;
import co.edu.uniquindio.dto.users.PersonaDto;

import java.util.List;

public record ProveedorDto(

        PersonaDto persona,
        List<ProductoDto> productos,
        List<DocumentoIngresoDto> documentoIngresos

) {
}
