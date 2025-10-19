package co.edu.uniquindio.exceptions;

//Excepción personalizada para indicar que un elemento o valor no es del tipo o formato correcto para una operación.con las condiciones necesarias para ser procesado.
// Http: (422 - UNPROCESSABLE_ENTITY.)
public class ElementoIncorrectoException extends Exception{
    // Constructor que recibe un mensaje detallado para describir la incorrección del elemento.
    public ElementoIncorrectoException(String mensaje) {

        super(mensaje);
    }
}
