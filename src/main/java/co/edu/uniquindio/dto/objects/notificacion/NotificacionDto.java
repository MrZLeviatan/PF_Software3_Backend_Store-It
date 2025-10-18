package co.edu.uniquindio.dto.objects.notificacion;

import java.time.LocalDateTime;
import java.util.List;

public record NotificacionDto(


        Long id,
        String titulo,
        String descripcion,
        LocalDateTime fechaEnvio,
        boolean esLeida,
        Long idReceptor,
        List<ReferenciaEntidadDto> entidadesAsociadas


) {
}
