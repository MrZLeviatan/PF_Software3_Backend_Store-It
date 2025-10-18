package co.edu.uniquindio.dto.users.gestroComercial;

import co.edu.uniquindio.dto.common.datosLaborales.DatosLaboralesDto;
import co.edu.uniquindio.dto.objects.inventario.movimiento.movimientoIngreso.MovimientoIngresoDto;
import co.edu.uniquindio.dto.objects.inventario.movimiento.movimientoRetiro.MovimientoRetiroDto;
import co.edu.uniquindio.dto.objects.solicitudes.solicitud.SolicitudDto;
import co.edu.uniquindio.dto.users.PersonaDto;

import java.util.List;

public record GestorComercialDto(

        PersonaDto personaDto,
        DatosLaboralesDto datosLaborales,
        List<SolicitudDto> solicitudes,
        List<MovimientoRetiroDto> movimientoRetiro,
        List<MovimientoIngresoDto> movimientoIngreso

) {
}
