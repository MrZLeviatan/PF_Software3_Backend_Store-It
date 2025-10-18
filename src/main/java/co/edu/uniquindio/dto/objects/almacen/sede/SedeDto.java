package co.edu.uniquindio.dto.objects.almacen.sede;

import co.edu.uniquindio.dto.common.UbicacionDto;
import co.edu.uniquindio.dto.objects.almacen.bodega.BodegaDto;

import java.util.List;

public record SedeDto(

        Long id,
        String nombre,
        String direccion,
        String telefono,
        String email,
        UbicacionDto ubicacion,
        List<BodegaDto> bodegas

) {
}
