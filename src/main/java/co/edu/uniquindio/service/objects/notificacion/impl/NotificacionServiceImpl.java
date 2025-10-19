package co.edu.uniquindio.service.objects.notificacion.impl;

import co.edu.uniquindio.controller.utils.NotificacionWebSocketController;
import co.edu.uniquindio.dto.objects.notificacion.NotificacionDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.objects.notificaciones.NotificacionMapper;
import co.edu.uniquindio.models.entities.objects.inventario.documento.DocumentoIngreso;
import co.edu.uniquindio.models.entities.objects.inventario.movimiento.MovimientoIngreso;
import co.edu.uniquindio.models.entities.objects.notificaciones.Notificacion;
import co.edu.uniquindio.models.entities.objects.solicitudes.Solicitud;
import co.edu.uniquindio.models.entities.users.AdminBodega;
import co.edu.uniquindio.models.entities.users.PersonalBodega;
import co.edu.uniquindio.models.enums.users.TipoPersonalBodega;
import co.edu.uniquindio.repository.objects.notificacion.NotificacionRepo;
import co.edu.uniquindio.repository.users.AdminBodegaRepo;
import co.edu.uniquindio.repository.users.PersonaRepo;
import co.edu.uniquindio.repository.users.PersonalBodegaRepo;
import co.edu.uniquindio.service.objects.notificacion.NotificacionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NotificacionServiceImpl implements NotificacionService {

    private final AdminBodegaRepo adminBodegaRepo;
    private final PersonaRepo personaRepo;
    private final PersonalBodegaRepo personalBodegaRepo;

    private final NotificacionWebSocketController notificacionWebSocketController;

    private final NotificacionRepo notificacionRepo;
    private final NotificacionMapper notificacionMapper;



    // Crea y envía una notificación cuando se realiza una solicitud de ampliación de espacio.
    @Override
    public void crearNotificacionSolicitudAmpliar(Solicitud solicitud) {

        // Se crea una notificación base
        Notificacion notificacion = new Notificacion();
        notificacion.setTitulo("Solicitud de Ampliar Espacio: " + solicitud.getEspacioProducto().getProducto().getNombre());
        notificacion.setDescripcion("El gestor comercial solicitó ampliar el espacio del producto " +
                solicitud.getEspacioProducto().getProducto().getNombre() + " en " + solicitud.getEspacioSolicitado() + " m³.");
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setReceptor(solicitud.getGestorComercial());
        notificacion.agregarEntidadAsociada(solicitud, solicitud.getId());

        // Guardar notificación en base de datos
        notificacionRepo.save(notificacion);


        NotificacionDto notificacionDto = notificacionMapper.toDto(notificacion);

        // Enviar notificación en tiempo real al gestor comercial
        notificacionWebSocketController.notificarReceptor(
                "/queue/notificaciones/" + solicitud.getGestorComercial().getId(),
                notificacionDto
        );

        // Enviar notificación a todos los administradores de la misma bodega
        List<AdminBodega> administradores = adminBodegaRepo.findByDatosLaborales_Bodega_Id(
                solicitud.getEspacioProducto().getSubBodega().getBodega().getId()
        );

        for (AdminBodega admin : administradores) {
            notificacionWebSocketController.notificarReceptor(
                    "/queue/notificaciones/" + admin.getId(),
                    notificacionDto
            );
        }
    }


    @Override
    public void crearNotificacionSolicitudRegistroEspacio(Solicitud solicitud) {

        // Se crea una notificación base
        Notificacion notificacion = new Notificacion();
        notificacion.setTitulo("Solicitud de registro nuevo Espacio Producto: " + solicitud.getEspacioProducto().getProducto().getNombre());
        notificacion.setDescripcion("El gestor comercial solicitó el registro del nuevo espacio para el producto " +
                solicitud.getEspacioProducto().getProducto().getNombre() + " con una area de " + solicitud.getEspacioSolicitado() + " m³.");
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.setReceptor(solicitud.getGestorComercial());
        notificacion.agregarEntidadAsociada(solicitud, solicitud.getId());

        // Guardar notificación en base de datos
        notificacionRepo.save(notificacion);

        NotificacionDto notificacionDto = notificacionMapper.toDto(notificacion);

        // Enviar notificación en tiempo real al gestor comercial
        notificacionWebSocketController.notificarReceptor(
                "/queue/notificaciones/" + solicitud.getGestorComercial().getId(),
                notificacionDto
        );

        // Enviar notificación a todos los administradores de la misma bodega
        List<AdminBodega> administradores = adminBodegaRepo.findByDatosLaborales_Bodega_Id(
                solicitud.getEspacioProducto().getSubBodega().getBodega().getId()
        );

        for (AdminBodega admin : administradores) {
            notificacionWebSocketController.notificarReceptor(
                    "/queue/notificaciones/" + admin.getId(),
                    notificacionDto
            );
        }

    }


    @Override
    public void crearNotificacionMovimientoIngreso(DocumentoIngreso documentoIngreso, MovimientoIngreso movimientoIngreso) {

        // Se crea una notificación base
        Notificacion notificacion = new Notificacion();
        notificacion.setTitulo("Movimiento de Ingreso: " + movimientoIngreso.getLote().getEspacioProducto().getProducto().getNombre());
        notificacion.setFechaEnvio(LocalDateTime.now());
        notificacion.agregarEntidadAsociada(movimientoIngreso, movimientoIngreso.getId());

        // -------- Determinar tipo de notificación según estado ----------
        String descripcion = "";
        List<PersonalBodega> receptores = new ArrayList<>();

        switch (movimientoIngreso.getEstadoMovimientoIngreso()) {

            case PENDIENTE -> {
                descripcion = "Nuevo movimiento de ingreso pendiente. Se requiere revisión inicial de inventario.";
                // Buscar personal con rol GESTOR_INVENTARIO en la misma bodega
                receptores = personalBodegaRepo.findByDatosLaborales_Bodega_IdAndTipoPersonalBodega(
                        movimientoIngreso.getLote().getEspacioProducto().getSubBodega().getBodega().getId(),
                        TipoPersonalBodega.GESTOR_INVENTARIO
                );
            }

            case EN_REVISION_INVENTARIO -> {
                descripcion = "Movimiento en revisión de inventario. El auxiliar debe verificar el lote.";
                // Buscar personal con rol AUXILIAR_BODEGA en la misma bodega
                receptores = personalBodegaRepo.findByDatosLaborales_Bodega_IdAndTipoPersonalBodega(
                        movimientoIngreso.getLote().getEspacioProducto().getSubBodega().getBodega().getId(),
                        TipoPersonalBodega.AUXILIAR_BODEGA
                );
            }

            case CALIDAD_VERIFICADA -> {
                descripcion = "Inventario verificado. El gestor de bodega debe confirmar la calidad del lote.";
                // Buscar personal con rol GESTOR_BODEGA en la misma bodega
                receptores = personalBodegaRepo.findByDatosLaborales_Bodega_IdAndTipoPersonalBodega(
                        movimientoIngreso.getLote().getEspacioProducto().getSubBodega().getBodega().getId(),
                        TipoPersonalBodega.GESTOR_BODEGA
                );
            }

            default -> {
                // Otros estados no generan notificación específica
                return;
            }
        }

        notificacion.setDescripcion(descripcion);

        // Guardar notificación en BD
        notificacionRepo.save(notificacion);

        // Convertir a DTO para enviar por WebSocket
        NotificacionDto notificacionDto = notificacionMapper.toDto(notificacion);

        // Enviar notificación a cada receptor
        for (PersonalBodega receptor : receptores) {
            notificacion.setReceptor(receptor);
            notificacionRepo.save(notificacion);

            notificacionWebSocketController.notificarReceptor(
                    "/queue/notificaciones/" + receptor.getId(),
                    notificacionDto
            );
        }

        // Notificar también a los administradores de la misma bodega
        List<AdminBodega> administradores = adminBodegaRepo.findByDatosLaborales_Bodega_Id(
                movimientoIngreso.getLote().getEspacioProducto().getSubBodega().getBodega().getId()
        );

        for (AdminBodega admin : administradores) {
            notificacionWebSocketController.notificarReceptor(
                    "/queue/notificaciones/" + admin.getId(),
                    notificacionDto
            );
        }
    }



    @Override
    public List<NotificacionDto> obtenerNotificacionesPorUsuario(Long idUsuario) throws ElementoNoEncontradoException {
        if (!personaRepo.existsById(idUsuario)) {
            throw new ElementoNoEncontradoException("No se encontró la persona con ID " + idUsuario);
        }
        return notificacionRepo.findByReceptorIdOrderByFechaEnvioDesc(idUsuario)
                .stream()
                .map(notificacionMapper::toDto)
                .toList();
    }


    @Override
    public void marcarNotificacionLeida(Long idNotificacion) throws ElementoNoEncontradoException {
        Notificacion notificacion = notificacionRepo.findById(idNotificacion)
                .orElseThrow(() -> new ElementoNoEncontradoException("No se encontró la notificación con ID " + idNotificacion));

        notificacion.setEsLeida(true);
        notificacionRepo.save(notificacion);
    }
}
