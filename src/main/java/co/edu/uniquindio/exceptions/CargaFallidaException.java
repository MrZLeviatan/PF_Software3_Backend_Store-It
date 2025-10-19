package co.edu.uniquindio.exceptions;

// Excepción personalizada que se lanza cuando ocurre un error durante la carga de un archivo, recurso o datos en el sistema.
//
public class CargaFallidaException extends RuntimeException{

    public CargaFallidaException(String mensaje, Throwable cause) {
        super(mensaje, cause);
    }

}
