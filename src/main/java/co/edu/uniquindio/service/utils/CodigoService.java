package co.edu.uniquindio.service.utils;

import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.exceptions.ElementoNoCoincideException;
import co.edu.uniquindio.exceptions.ElementoNoEncontradoException;
import co.edu.uniquindio.models.embeddable.Codigo;


public interface CodigoService {

    // Genera un código único de verificación para procesos de autenticación de dos factores (2FA).
    Codigo generarCodigoVerificacion2AF();

    // Genera un código único para el proceso de restablecimiento de contraseña.
    Codigo generarCodigoRestablecerPassword();

    // Verifica que el código ingresado coincida con el generado previamente.
    void autentificarCodigo(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoNoCoincideException;
}
