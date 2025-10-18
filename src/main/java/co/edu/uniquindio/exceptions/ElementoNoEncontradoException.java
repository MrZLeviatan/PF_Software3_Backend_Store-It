package co.edu.uniquindio.exceptions;

// Excepción personalizada para indicar que un elemento o recurso específico no fue encontrado en la aplicación.
// Http: 404 No encontrado
public class ElementoNoEncontradoException extends Exception {
    // Constructor que recibe un mensaje detallado para describir el error.
    public ElementoNoEncontradoException(String message) {
        super(message);
    }
}