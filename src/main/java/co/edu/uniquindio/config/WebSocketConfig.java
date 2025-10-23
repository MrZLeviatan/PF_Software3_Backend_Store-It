package co.edu.uniquindio.config;

import co.edu.uniquindio.security.WebSocketInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

// Configuración de WebSocket con STOMP. Permite la comunicación en tiempo real entre el servidor y los clientes.
@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final WebSocketInterceptor webSocketInterceptor;

    // Registra el endpoint principal para las conexiones WebSocket.
    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.addEndpoint("/ws") // URL del endpoint WebSocket
                .setAllowedOriginPatterns("https://pfs3-storeit.web.app") // Permite todas las conexiones (puedes restringir por dominio)
                .withSockJS(); // Permite compatibilidad con navegadores que no soportan WebSocket nativo
    }


    // Configura el broker de mensajes para enviar y recibir notificaciones.
    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        registry.enableSimpleBroker("/topic", "/queue"); // Canales de salida
        registry.setApplicationDestinationPrefixes("/app"); // Prefijo de destino para mensajes entrantes
    }

}
