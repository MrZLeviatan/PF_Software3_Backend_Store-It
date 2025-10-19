package co.edu.uniquindio.service.objects.notificacion;

import co.edu.uniquindio.dto.objects.notificacion.NotificacionDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.models.entities.objects.inventario.documento.DocumentoIngreso;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.MovimientoIngreso;
import co.edu.uniquindio.models.entities.objects.solicitudes.Solicitud;

import java.util.List;

public interface NotificacionService {


    void crearNotificacionSolicitudAmpliar(Solicitud solicitud);


    void crearNotificacionSolicitudRegistroEspacio(Solicitud solicitud);

    void crearNotificacionMovimientoIngreso(DocumentoIngreso documentoIngreso, MovimientoIngreso movimientoIngreso);


    // Obtiene todas las notificaciones de un usuario por su ID
    List<NotificacionDto> obtenerNotificacionesPorUsuario(Long idUsuario)
            throws ElementoNoEncontradoException;

    // Marca una notificación como leída
    void marcarNotificacionLeida(Long idNotificacion)
            throws ElementoNoEncontradoException;

}
