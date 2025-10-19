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
 * <p>
 * Este filtro se ejecuta una única vez por cada solicitud (OncePerRequestFilter).
 * Se activa automáticamente al estar anotado como {@code @Component}.
 *
 * <p><strong>Encabezado esperado:</strong> Authorization: Bearer [token]
 *
 * @see JWTUtils Utilidad encargada de validar y decodificar el JWT.
 */
@Component
@RequiredArgsConstructor
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtils jwtUtil;


    // Método principal que intercepta cada solicitud HTTP entrante.
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {

        // Se intenta extraer el token del encabezado "Authorization"
        String token = getToken(request);
        String requestURI = request.getRequestURI();

        System.out.println("🔹 [JWTFilter] URL: " + request.getRequestURI()); // 🇺🇸/🇪🇸 Show request being filtered


        if (requestURI.startsWith("/api/auth/") ||
                requestURI.startsWith("/api/store-it/")){

            chain.doFilter(request, response);
            return;
        }

        try {
            // Se valida el token JWT y se obtiene su contenido (payload)
            Jws<Claims> payload = jwtUtil.parseJwt(token);

            System.out.println("✅ [JWTFilter] Token validated successfully for user: "
                    + payload.getPayload().getSubject());

            String username = payload.getPayload().getSubject(); // Nombre Usuario
            String role = payload.getPayload().get("rol", String.class); // Rol del usuario

            // Se normaliza el rol al formato estándar de Spring Security (ROLE_XXX)
            if (role.startsWith("ROL_")) {
                // Caso: si el token trae "ROL_AUXILIAR_BODEGA" → convertirlo
                role = "ROLE_" + role.substring(4);
            } else if (!role.startsWith("ROLE_")) {
                // Caso: si viene "AUXILIAR_BODEGA" → agregar prefijo
                role = "ROLE_" + role;
            }

            // Verifica si ya hay una autenticación activa en el contexto
            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                // Crea un objeto de usuario con nombre y rol, sin necesidad de contraseña
                UserDetails userDetails = new User(
                        username,
                        "", // No se requiere contraseña aquí
                        List.of(new SimpleGrantedAuthority(role)) // Autoridades según el rol
                );

                // Se genera un token de autenticación que Spring Security reconocerá
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null,
                        userDetails.getAuthorities()
                );

                // Se guarda la autenticación en el contexto de seguridad
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }

        } catch (Exception e) {

            System.err.println("❌ [JWTFilter] Token error: " + e.getMessage());

            // Si hay algún error con el token (expirado, mal formado, inválido), se retorna una respuesta 401
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Unauthorized");

            return;}

        // Si todo está correcto, se continúa con la cadena de filtros.
        chain.doFilter(request, response);
    }


    // Extrae el token JWT del encabezado "Authorization" de la solicitud.
    private String getToken(HttpServletRequest req) {
        String header = req.getHeader("Authorization");

        // Verifica que el encabezado comience con "Bearer" y extrae el token
        return header != null && header.startsWith("Bearer ")
                ? header.replace("Bearer ", "") : null;
    }
}
