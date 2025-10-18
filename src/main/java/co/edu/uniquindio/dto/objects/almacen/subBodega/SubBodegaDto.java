package co.edu.uniquindio.dto.objects.almacen.subBodega;

import co.edu.uniquindio.dto.common.unidadAlmacenamiento.UnidadAlmacenamientoDto;
import co.edu.uniquindio.dto.objects.almacen.espacioProducto.EspacioProductoDto;
import co.edu.uniquindio.models.enums.entities.TipoProducto;

import java.util.List;

public record SubBodegaDto(

        Long id,
        Long idBodega,
        UnidadAlmacenamientoDto unidadAlmacenamiento,
        TipoProducto tipoProducto,
        List<EspacioProductoDto> espaciosProductos


) {
}
