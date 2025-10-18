package co.edu.uniquindio.dto.users.adminBodega;

import co.edu.uniquindio.dto.common.datosLaborales.DatosLaboralesDto;
import co.edu.uniquindio.dto.objects.solicitudes.resolucion.ResolucionDto;
import co.edu.uniquindio.dto.users.PersonaDto;

import java.util.List;

public record AdminBodegaDto(


        PersonaDto persona,
        DatosLaboralesDto datosLaborales,
        List<ResolucionDto> resoluciones

) {
}
