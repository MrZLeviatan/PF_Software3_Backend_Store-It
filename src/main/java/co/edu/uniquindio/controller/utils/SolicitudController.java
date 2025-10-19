package co.edu.uniquindio.controller.utils;

import co.edu.uniquindio.dto.MensajeDto;
import co.edu.uniquindio.dto.objects.solicitudes.solicitud.SolicitudDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.service.objects.solicitudes.SolicitudService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/solicitud")
@RequiredArgsConstructor
public class SolicitudController {

    private final SolicitudService solicitudService;

    @GetMapping("/{idSolicitud}")
    public ResponseEntity<MensajeDto<SolicitudDto>> obtenerSolicitudId(@PathVariable Long idSolicitud)
            throws ElementoNoEncontradoException {

        SolicitudDto solicitudDto = solicitudService.obtenerSolicitudDto(idSolicitud);
        return ResponseEntity.ok().body(new MensajeDto<>(false, solicitudDto));
    }







}
