package co.edu.uniquindio.service.utils;


import co.edu.uniquindio.exceptions.*;
import co.edu.uniquindio.models.entities.users.Persona;

public interface PersonaUtilService {


    // Válida que el correo electrónico no se encuentre repetido en el sistema.
    void validarEmailNoRepetido(String email)
            throws ElementoRepetidoException, ElementoEliminadoException;

    // Válida que los teléfonos (principal y secundario) no estén repetidos
    void validarTelefonoNoRepetido(String telefono, String telefonoSecundario)
            throws ElementoRepetidoException, ElementoNulosException, ElementoNoValidoException;

    // Busca y devuelve una persona registrada según su correo electrónico.
    Persona buscarPersonaPorEmail(String email)
            throws ElementoNoEncontradoException;

    Persona buscarPersonaPorId(Long id) throws ElementoNoEncontradoException;

    // Persiste una entidad Persona en la base de datos.
    void guardarPersonaBD(Persona persona);




}

