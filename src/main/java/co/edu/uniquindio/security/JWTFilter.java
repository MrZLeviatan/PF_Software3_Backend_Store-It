package co.edu.uniquindio.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

/**
 * Filtro de seguridad que intercepta cada solicitud HTTP para validar un token JWT.
 * <p>
 * Si el token es válido, se extrae la información del usuario (como nombre y rol)
 * y se registra en el contexto de seguridad de Spring Security, permitiendo así
 * el acceso a recursos protegidos bajo autorización.
 *
 * <p><strong>Encabezado esperado:</strong> Authorization: Bearer [token]
 *
 * @see JWTUtils Utilidad encargada de validar y decodificar el JWT.
 */
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        String requestURI = request.getRequestURI();
        System.out.println("🔹 [JWTFilter] URL: " + requestURI); // Mostrar la URL que se está filtrando

        // 🔹 Rutas públicas: no requieren token
        // 🔹 Rutas públicas: no requieren token
        if (requestURI.equals("/") ||
                requestURI.startsWith("/error") ||
                requestURI.startsWith("/api/auth/") ||
                requestURI.startsWith("/api/store-it/")) {

            chain.doFilter(request, response);
            return;
        }


        // 🔹 Solo rutas protegidas: obtener token del header Authorization
        String token = getToken(request);

        // Validar que el token exista
        if (token == null || token.isEmpty()) {
            System.err.println("❌ [JWTFilter] Token missing or empty for: " + requestURI);
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Token missing or empty");
            return;
        }

        try {
            // Validar token JWT y obtener payload
            Jws<Claims> payload = jwtUtil.parseJwt(token);
            System.out.println("✅ [JWTFilter] Token validated successfully for user: " + payload.getPayload().getSubject());

            String username = payload.getPayload().getSubject(); // Nombre del usuario
            String role = payload.getPayload().get("rol", String.class); // Rol del usuario

            // Normalizar rol al formato ROLE_XXX
            if (role.startsWith("ROL_")) {
                role = "ROLE_" + role.substring(4);
            } else if (!role.startsWith("ROLE_")) {
                role = "ROLE_" + role;
            }

            // Si no hay autenticación previa, crear objeto UserDetails y autenticar
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = new User(
                        username,
                        "", // No se requiere contraseña aquí
                        List.of(new SimpleGrantedAuthority(role))
                );

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {
            System.err.println("❌ [JWTFilter] Token error: " + e.getMessage());
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");
            return;
        }

        // Continuar con la cadena de filtros
        chain.doFilter(request, response);
    }

    // 🔹 Extrae el token JWT del encabezado "Authorization"
    private String getToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");
        return header != null && header.startsWith("Bearer ")
                ? header.replace("Bearer ", "")
                : null;
    }
}
