package co.edu.uniquindio.service.objects.solicitudes.impl;


import co.edu.uniquindio.dto.objects.almacen.espacioProducto.RegistroEspacioProductoDto;
import co.edu.uniquindio.dto.objects.solicitudes.solicitud.RegistroSolicitudDto;
import co.edu.uniquindio.dto.objects.solicitudes.solicitud.SolicitudDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.objects.solicitudes.SolicitudMapper;
import co.edu.uniquindio.models.entities.objects.almacen.EspacioProducto;
import co.edu.uniquindio.models.entities.objects.solicitudes.Solicitud;
import co.edu.uniquindio.models.entities.users.GestorComercial;
import co.edu.uniquindio.models.enums.entities.EstadoProceso;
import co.edu.uniquindio.models.enums.entities.TipoSolicitud;
import co.edu.uniquindio.repository.objects.almacen.EspacioProductoRepo;
import co.edu.uniquindio.repository.objects.solicitudes.SolicitudRepo;
import co.edu.uniquindio.repository.users.GestorComercialRepo;
import co.edu.uniquindio.service.objects.solicitudes.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SolicitudServiceImpl implements SolicitudService {


    private final GestorComercialRepo gestorComercialRepo;
    private final EspacioProductoRepo espacioProductoRepo;

    private final SolicitudRepo solicitudRepo;
    private final SolicitudMapper solicitudMapper;




    @Override
    public void registrarSolicitudEspacio(EspacioProducto espacioProducto, RegistroEspacioProductoDto registroEspacioProductoDto)
            throws ElementoNoEncontradoException {

        Solicitud solicitud = new Solicitud();
        solicitud.setTipoSolicitud(TipoSolicitud.ABRIR_ESPACIO_PRODUCTO);
        solicitud.setDescripcion(registroEspacioProductoDto.descripcion());
        solicitud.setFechaSolicitud(LocalDate.now());
        solicitud.setEstadoSolicitud(EstadoProceso.PENDIENTE);
        solicitud.setEspacioSolicitado(espacioProducto.getUnidadAlmacenamiento().getVolumenTotal());
        solicitud.setGestorComercial(
                obtenerGestorComercialId(registroEspacioProductoDto.idGestor()));
        solicitud.setEspacioProducto(espacioProducto);

        obtenerGestorComercialId(registroEspacioProductoDto.idGestor()).agregarSolicitud(solicitud);
        espacioProducto.agregarSolicitud(solicitud);
        solicitudRepo.save(solicitud);

    }

    @Override
    public void registrarSolicitud(RegistroSolicitudDto registroSolicitudDto)
            throws ElementoNoEncontradoException {

        //1. Se mapea la solicitud
        Solicitud solicitud = solicitudMapper.toEntity(registroSolicitudDto);

        // 2. Se asigna el Gestor y Espacio a Ampliar
        GestorComercial gestorComercial = obtenerGestorComercialId(registroSolicitudDto.idGestorComercial());
        EspacioProducto espacioProducto = obtenerEspacioProducto(registroSolicitudDto.idEspacio());

        solicitud.setGestorComercial(gestorComercial);
        solicitud.setEspacioProducto(espacioProducto);

        espacioProducto.agregarSolicitud(solicitud);
        gestorComercial.agregarSolicitud(solicitud);

        // Se guarda en la BD
        solicitudRepo.save(solicitud);
    }

    @Override
    public Solicitud obtenerSolicitudId(Long id) throws ElementoNoEncontradoException {
        return solicitudRepo.findById(id)
                .orElseThrow(() -> new ElementoNoEncontradoException("Solicitud no encontrada"));
    }


    @Override
    public SolicitudDto obtenerSolicitudDto(Long id) throws ElementoNoEncontradoException {
        return solicitudMapper.toDto(obtenerSolicitudId(id));
    }


    @Override
    public List<SolicitudDto> obtenerSolicitudesGestor(Long idGestor) throws ElementoNoEncontradoException {
        GestorComercial gestorComercial = obtenerGestorComercialId(idGestor);

        return gestorComercial.getSolicitudes()
                .stream()
                .map(solicitudMapper::toDto)
                .toList();
    }


    public EspacioProducto obtenerEspacioProducto(Long id) throws ElementoNoEncontradoException {
        return espacioProductoRepo.findById(id)
                .orElseThrow(() -> new ElementoNoEncontradoException("Espacio producto no encontrado"));

    }

    public GestorComercial obtenerGestorComercialId(Long id) throws ElementoNoEncontradoException {
        return gestorComercialRepo.findById(id)
                .orElseThrow(() -> new ElementoNoEncontradoException("Gestor comercial no encontrado"));
    }
}
