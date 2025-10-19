package co.edu.uniquindio.service.objects.solicitudes;

import co.edu.uniquindio.dto.objects.almacen.espacioProducto.RegistroEspacioProductoDto;
import co.edu.uniquindio.dto.objects.solicitudes.solicitud.RegistroSolicitudDto;
import co.edu.uniquindio.dto.objects.solicitudes.solicitud.SolicitudDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;
import co.edu.uniquindio.models.entities.objects.solicitudes.Solicitud;

import java.util.List;

public interface SolicitudService {


    void registrarSolicitudEspacio(EspacioProducto espacioProducto, RegistroEspacioProductoDto registroEspacioProductoDto)
            throws ElementoNoEncontradoException;

    void registrarSolicitud(RegistroSolicitudDto registroSolicitudDto) throws ElementoNoEncontradoException;

    Solicitud obtenerSolicitudId(Long id) throws ElementoNoEncontradoException;

    SolicitudDto obtenerSolicitudDto(Long id)
            throws ElementoNoEncontradoException;

    List<SolicitudDto> obtenerSolicitudesGestor(Long idGestor)
            throws ElementoNoEncontradoException;

    List<SolicitudDto> obtenerSolicitudes();



}
