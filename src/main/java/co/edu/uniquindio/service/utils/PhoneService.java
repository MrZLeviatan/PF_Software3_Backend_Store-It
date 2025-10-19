package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.exceptions.ElementoNoValidoException;
import co.edu.uniquindio.exceptions.ElementoNulosException;

public interface PhoneService {

    // Devuelve el número de teléfono formateado de acuerdo al código de pais.
    String obtenerTelefonoFormateado(String telefono, String codigoPais)
            throws ElementoNulosException, ElementoNoValidoException;

}
