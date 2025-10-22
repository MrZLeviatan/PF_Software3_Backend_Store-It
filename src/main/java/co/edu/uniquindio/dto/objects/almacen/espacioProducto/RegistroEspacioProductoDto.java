package co.edu.uniquindio.dto.objects.almacen.espacioProducto;

import co.edu.uniquindio.dto.common.unidadAlmacenamiento.RegistroUnidadAlmacenamientoDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;

public record RegistroEspacioProductoDto(


        @NotNull @Valid RegistroUnidadAlmacenamientoDto unidadAlmacenamiento,
        Long idSubBodega,
        @NotNull Long idProducto,
        String descripcion,
        @NotNull Long idGestor


) {
}
