package co.edu.uniquindio.dto.objects.almacen.bodega;

import co.edu.uniquindio.dto.common.datosLaborales.DatosLaboralesDto;
import co.edu.uniquindio.dto.objects.almacen.subBodega.SubBodegaDto;

import java.util.List;

public record BodegaDto(

        Long id,
        Long idSede,
        List<DatosLaboralesDto> datosLaborales,
        List<SubBodegaDto> subBodegas
) {
}
