package co.edu.uniquindio.exceptions;

// Excepción personalizada para indicar que se intenta realizar una operación sobre un elemento que ya ha sido eliminado.
// Http: 410 Elemento Eliminado
public class ElementoEliminadoException extends Exception {
    // Constructor que recibe un mensaje detallado para describir el error.
    public ElementoEliminadoException(String message) {
        super(message);
    }
}