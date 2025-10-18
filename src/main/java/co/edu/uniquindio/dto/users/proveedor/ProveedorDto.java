package co.edu.uniquindio.dto.users.proveedor;

import co.edu.uniquindio.dto.objects.inventario.producto.ProductoDto;
import co.edu.uniquindio.dto.users.PersonaDto;
import co.edu.uniquindio.models.entities.objects.inventario.documento.DocumentoIngreso;

import java.util.List;

public record ProveedorDto(

        PersonaDto personaDto,
        List<ProductoDto> productos,
        List<DocumentoIngreso> documentoIngresos

) {
}
