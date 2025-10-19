package co.edu.uniquindio.service.users;

import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.models.entities.users.GestorComercial;

public interface GestorComercialService {


    GestorComercial obtenerGestorComercialId(Long id)
            throws ElementoNoEncontradoException;
}
