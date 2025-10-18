package co.edu.uniquindio.dto.users;

import co.edu.uniquindio.dto.common.user.UserDto;
import co.edu.uniquindio.dto.objects.notificacion.NotificacionDto;

import java.util.List;

public record PersonaDto(

        Long id,
        // Nombre completo del cliente.
        String nombre,
        // Teléfono principal del cliente.
        String telefono,
        // Teléfono secundario del cliente.
        String telefonoSecundario,
        // Objeto DTO que contiene la información del usuario (email, estado de la cuenta, etc.).
        UserDto user,
        // Lista de notificaciones asociado
        List<NotificacionDto> notificaciones

) {
}
