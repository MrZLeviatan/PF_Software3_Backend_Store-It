package co.edu.uniquindio.exceptions;

// Se lanza cuando un elemento proporcionado no es válido según las reglas del negocio o las validaciones aplicadas.
// Http: 418 No soy una tetera
public class ElementoNoValidoException extends Exception {


    public ElementoNoValidoException(String message) {
        super(message);
    }

    // Crea una nueva instancia de la excepción que válida un mensaje y la causa asociada
    public ElementoNoValidoException(String message, Throwable cause) {super(message,cause);}

}
