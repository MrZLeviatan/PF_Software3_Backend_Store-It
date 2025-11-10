package co.edu.uniquindio.config;

import co.edu.uniquindio.security.AuthenticationEntryPoint;
import co.edu.uniquindio.security.JWTFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    // Filtro personalizado para procesar los tokens JWT antes de la autenticación estándar
    private final JWTFilter jwtFilter;

    private final AuthenticationEntryPoint authenticationEntryPoint;



    // Método de seguridad, configura el manejo de sesiones y donde estas pueden ingresar.
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        // Configura la seguridad de la aplicación

        http
                //  Desactiva CSRF (se usa JWT
                .csrf(AbstractHttpConfigurer::disable)
                //  Habilita CORS con configuración previa
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                // Define sesiones sin estado (stateless)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Reglas de acceso por endpoint y rol
                .authorizeHttpRequests(req -> req
                        .requestMatchers("/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html").permitAll() // Docs públicas
                        .requestMatchers("/api/auth/**").permitAll() // Login/registro públicos
                        .requestMatchers("/api/store-it/**").permitAll() // Público store-it
                        .requestMatchers("/api/compra/webhook").permitAll() // ⚠️ Debe ir antes del patrón /api/compra/**


                        // ENDPOINTS para todos,
                        .requestMatchers("/api/producto/**")
                        .hasAnyAuthority("ROLE_CLIENTE", "ROLE_GESTOR_COMERCIAL", "ROLE_ADMIN_BODEGA",
                                "ROLE_AUXILIAR_BODEGA", "ROLE_GESTOR_INVENTARIO", "ROLE_GESTOR_BODEGA")


                        // --- ENDPOINTS USADOS POR TODOS LOS EMPLEADOS ---
                        .requestMatchers("/api/proveedor/**", "/api/sub-bodega/**", "/api/espacio-producto/**",
                                "/api/lote/**", "/api/notificaciones/**")
                        .hasAnyAuthority("ROLE_GESTOR_COMERCIAL", "ROLE_ADMIN_BODEGA",
                                "ROLE_AUXILIAR_BODEGA", "ROLE_GESTOR_INVENTARIO", "ROLE_GESTOR_BODEGA")


                        // --- ENDPOINTS COMPARTIDOS ENTRE GESTOR COMERCIAL Y ADMIN ---
                        .requestMatchers("/api/solicitud/**")
                        .hasAnyAuthority("ROLE_GESTOR_COMERCIAL", "ROLE_ADMIN_BODEGA")

                        // --- ENDPOINTS PARA EL CLIENTE
                        .requestMatchers("/api/compra/**","/api/carrito-compra/**")
                        .hasAnyAuthority("ROLE_CLIENTE")

                        // --- ENDPOINTS SOLO PARA GESTOR COMERCIAL ---
                        .requestMatchers("/api/gestor-comercial/**")
                        .hasAuthority("ROLE_GESTOR_COMERCIAL")

                        // --- MONITOREO PROMETHEUS ---
                        .requestMatchers("/actuator/prometheus").permitAll()

                        // Cualquier otra solicitud requiere autenticación
                        .anyRequest().authenticated()
                )

                // Manejo de errores de autenticación
                .exceptionHandling(ex -> ex.authenticationEntryPoint(new AuthenticationEntryPoint()))
                // Filtro JWT antes de auth por usuario/clave
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

        // Devuelve la cadena de seguridad configurada
        return http.build();
    }



    // Configuración de los CORS ( Cross - Origin Resource Sharing )
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // 🌍 Orígenes permitidos (frontend en Firebase y pruebas locales)
        config.setAllowedOrigins(List.of(
                "http://localhost:4200"));

        // ✅ Permitir envío de cookies, tokens y cabeceras de autenticación
        config.setAllowCredentials(true);

        // ✅ Métodos HTTP permitidos
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // ✅ Cabeceras permitidas
        config.setAllowedHeaders(List.of("Authorization", "Cache-Control", "Content-Type", "Accept", "Origin", "Stripe-Signature"));

        // ✅ Cabeceras expuestas (para que el frontend pueda leerlas)
        config.setExposedHeaders(List.of("Authorization"));

        // 🔧 Registro de configuración para todos los endpoints
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }




    // Define el bean que Spring usará para inyectar PasswordEncoder
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        // Devuelve el AuthenticationManager que Spring Security usa internamente
        return configuration.getAuthenticationManager();
    }
}
