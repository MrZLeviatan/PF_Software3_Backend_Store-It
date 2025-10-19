package co.edu.uniquindio.exceptions;

//Excepción personalizada para indicar que un elemento o valor no coincide con lo esperado en la lógica de la aplicación.
// Http: 401 Unauthorized
public class ElementoNoCoincideException extends Exception {
    // Constructor que recibe un mensaje para describir el error de no coincidencia.
    public ElementoNoCoincideException(String message) {
        super(message);
    }
}