package co.edu.uniquindio.service.users;

import co.edu.uniquindio.dto.common.auth.VerificacionCodigoDto;
import co.edu.uniquindio.dto.common.google.GoogleUserResponse;
import co.edu.uniquindio.dto.users.cliente.RegistroClienteDto;
import co.edu.uniquindio.dto.users.cliente.RegistroClienteGoogleDto;
import co.edu.uniquindio.exceptions.*;

public interface ClienteService {


    // Registro de nuevos clientes mediante el método tradicional
    void registrarCliente(RegistroClienteDto registroClienteDto)
            throws ElementoNoValidoException, ElementoNulosException, ElementoRepetidoException, ElementoEliminadoException;


    // Validación Token de la cuenta Google (Antes del Registro Mediante Google)
    GoogleUserResponse validarToken(String token) throws ElementoIncorrectoException;

    // Registro de nuevos clientes mediante el método Google
    void registroClienteGoogle(RegistroClienteGoogleDto registroClienteGoogleDto)
            throws ElementoNoValidoException, ElementoNulosException, ElementoRepetidoException, ElementoEliminadoException;


    // Verificación código
    void verificacionCliente(VerificacionCodigoDto verificacionCodigoDto)
            throws ElementoNoEncontradoException, ElementoIncorrectoException, ElementoNoCoincideException;




}
