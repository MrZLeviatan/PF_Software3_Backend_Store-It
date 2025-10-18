package co.edu.uniquindio.exceptions;

// Excepción personalizada para manejar situaciones donde se intenta realizar una operación con un elemento que es nulo.
// Http: 400 - Bad Request
public class ElementoNulosException extends Exception {

    // Constructor de la excepción que recibe un mensaje detallado del error.
    public ElementoNulosException(String mensaje) {
        super(mensaje);
    }
}