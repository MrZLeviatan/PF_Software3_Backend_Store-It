package co.edu.uniquindio.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

import java.util.Map;

// Interceptor para validar el token JWT antes de establecer la conexión WebSocket.
@Component
@RequiredArgsConstructor
public class WebSocketInterceptor implements HandshakeInterceptor {

    private final JWTUtils jwtUtils;

    @Override
    public boolean beforeHandshake(ServerHttpRequest request,
                                   ServerHttpResponse response,
                                   WebSocketHandler wsHandler,
                                   Map<String, Object> attributes) {

        if (request instanceof ServletServerHttpRequest servletRequest) {
            HttpServletRequest httpRequest = servletRequest.getServletRequest();
            String token = httpRequest.getHeader("Authorization");

            // Verifica que el token exista y tenga el prefijo "Bearer "
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);

                try {
                    // Valida el token con JWTUtils
                    Jws<Claims> claims = jwtUtils.parseJwt(token);

                    // Extrae los datos del usuario y los guarda en los atributos del socket
                    String username = claims.getPayload().getSubject();
                    String rol = claims.getPayload().get("rol", String.class);

                    attributes.put("username", username);
                    attributes.put("rol", rol);
                    attributes.put("token", token);

                } catch (Exception e) {
                    System.out.println("Token inválido en conexión WebSocket: " + e.getMessage());
                    return false; // Rechaza la conexión si el token no es válido
                }
            } else {
                System.out.println("No se envió token JWT en la solicitud WebSocket");
                return false; // Bloquea conexiones sin token
            }
        }
        return true;
    }

    @Override
    public void afterHandshake(ServerHttpRequest request,
                               ServerHttpResponse response,
                               WebSocketHandler wsHandler,
                               Exception exception) {
        // No se necesita lógica adicional tras el handshake
    }
}

