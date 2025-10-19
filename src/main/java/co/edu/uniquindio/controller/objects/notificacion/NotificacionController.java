package co.edu.uniquindio.controller.objects.notificacion;

import co.edu.uniquindio.dto.objects.notificacion.NotificacionDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.service.objects.notificacion.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notificaciones")
@RequiredArgsConstructor
public class NotificacionController {

    private final NotificacionService notificacionService;


    // Obtiene todas las notificaciones de un usuario.
    @GetMapping("/usuario/{idUsuario}")
    public ResponseEntity<List<NotificacionDto>> obtenerNotificacionesPorUsuario(@PathVariable Long idUsuario)
            throws ElementoNoEncontradoException {

        List<NotificacionDto> notificaciones = notificacionService.obtenerNotificacionesPorUsuario(idUsuario);
        return ResponseEntity.ok(notificaciones);
    }


    // Marca una notificación como leída.
    @PutMapping("/{idNotificacion}/leida")
    public ResponseEntity<String> marcarComoLeida(@PathVariable Long idNotificacion)
            throws ElementoNoEncontradoException {

        notificacionService.marcarNotificacionLeida(idNotificacion);
        return ResponseEntity.ok("Notificación marcada como leída correctamente.");
    }
}
