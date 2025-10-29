package co.edu.uniquindio.dto.common.google;

/**
 * Su propósito es encapsular los datos básicos del usuario, como su email, nombre y
 * URL de la imagen de perfil, de manera segura y eficiente.
 */
public record GoogleUserResponse(
        // El email del usuario.
        String email,
        // El nombre completo del usuario.
        String name,
        // La URL de la imagen de perfil del usuario.
        String picture
) {
}