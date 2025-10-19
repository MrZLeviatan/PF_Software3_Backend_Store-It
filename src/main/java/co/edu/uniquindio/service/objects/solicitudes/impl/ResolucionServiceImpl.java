package co.edu.uniquindio.service.objects.solicitudes.impl;

import co.edu.uniquindio.dto.objects.solicitudes.resolucion.RegistroResolucionDto;
import co.edu.uniquindio.dto.objects.solicitudes.resolucion.ResolucionDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.mapper.objects.solicitudes.ResolucionMapper;
import co.edu.uniquindio.models.entities.objects.solicitudes.Resolucion;
import co.edu.uniquindio.models.entities.objects.solicitudes.Solicitud;
import co.edu.uniquindio.models.entities.users.AdminBodega;
import co.edu.uniquindio.models.enums.entities.EstadoProceso;
import co.edu.uniquindio.repository.objects.solicitudes.ResolucionRepo;
import co.edu.uniquindio.repository.objects.solicitudes.SolicitudRepo;
import co.edu.uniquindio.repository.users.AdminBodegaRepo;
import co.edu.uniquindio.service.objects.solicitudes.ResolucionService;
import co.edu.uniquindio.service.objects.solicitudes.SolicitudService;
import co.edu.uniquindio.service.utils.PersonaUtilService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ResolucionServiceImpl implements ResolucionService {


    private final SolicitudRepo solicitudRepo;
    private final SolicitudService solicitudService;
    private final PersonaUtilService personaUtilService;

    private final ResolucionRepo resolucionRepo;
    private final ResolucionMapper resolucionMapper;


    @Override
    public void solicitudAprobada(RegistroResolucionDto registroResolucionDto) throws ElementoNoEncontradoException {

        // 1. Mapeamos la resolución
        Resolucion resolucion = resolucionMapper.toEntity(registroResolucionDto);

        // Buscamos el admin que aprueba
        AdminBodega adminBodega = (AdminBodega) personaUtilService.buscarPersonaPorId(registroResolucionDto.idAdmin());

        // Vinculamos la resolución con el admin
        adminBodega.agregarResolucion(resolucion);

        Solicitud solicitud = solicitudService.obtenerSolicitudId(registroResolucionDto.idSolicitud());

        solicitud.setEstadoSolicitud(EstadoProceso.APROBADA);
        solicitud.setResolucion(resolucion);
        resolucion.setSolicitud(solicitud);

        // Guardar en la BD
        solicitudRepo.save(solicitud);
        resolucionRepo.save(resolucion);
    }



    @Override
    public void solicitudRechazada(ResolucionDto resolucionDto) {

    }



}
