package co.edu.uniquindio.dto.objects.inventario.movimiento.participacionMovimiento;

import co.edu.uniquindio.models.enums.users.TipoPersonalBodega;

public record ParticipacionMovimientoDto(


        Long id,
        Long idMovimiento,
        Long idPersonalBodega,
        TipoPersonalBodega rolParticipacion

) {
}
