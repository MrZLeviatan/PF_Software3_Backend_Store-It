package co.edu.uniquindio.dto.objects.almacen.espacioProducto;

import co.edu.uniquindio.dto.common.unidadAlmacenamiento.UnidadAlmacenamientoDto;
import co.edu.uniquindio.dto.objects.inventario.lote.LoteDto;
import co.edu.uniquindio.dto.objects.solicitudes.solicitud.SolicitudDto;
import co.edu.uniquindio.models.enums.entities.EstadoEspacioProducto;

import java.util.List;

public record EspacioProductoDto(

        Long id,
        UnidadAlmacenamientoDto unidadAlmacenamiento,
        Long idSubBodega,
        Long idProducto,
        int cantidadTotal,
        EstadoEspacioProducto estadoEspacio,
        List<SolicitudDto> solicitudes,
        List<LoteDto> lotes

) {
}
