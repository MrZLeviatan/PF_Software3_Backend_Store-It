package co.edu.uniquindio.controller.utils;


import co.edu.uniquindio.dto.objects.notificacion.NotificacionDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

// Controlador WebSocket que maneja el envío de notificaciones en tiempo real.
@Controller
@RequiredArgsConstructor
public class NotificacionWebSocketController {

    private final SimpMessagingTemplate messagingTemplate;


    // Envía la notificación recibida a todos los suscriptores del canal
    @MessageMapping("/notificar") // Endpoint para recibir mensajes desde el cliente
    public void enviarNotificacion(NotificacionDto notificacion) {
        messagingTemplate.convertAndSend("/topic/notificaciones", notificacion);
    }

    // Método auxiliar para enviar una notificación específica desde el backend.
    public void notificarReceptor(String destino, NotificacionDto notificacion) {
        messagingTemplate.convertAndSend(destino, notificacion);
    }

}
