package co.edu.uniquindio.service.objects.solicitudes;

import co.edu.uniquindio.dto.objects.solicitudes.resolucion.RegistroResolucionDto;
import co.edu.uniquindio.dto.objects.solicitudes.resolucion.ResolucionDto;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;

public interface ResolucionService {


    void solicitudAprobada(RegistroResolucionDto registroResolucionDto) throws ElementoNoEncontradoException;

    void solicitudRechazada(ResolucionDto resolucionDto);

}
