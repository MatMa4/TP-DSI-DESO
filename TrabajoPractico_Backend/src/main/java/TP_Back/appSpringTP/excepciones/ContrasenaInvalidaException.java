package TP_Back.appSpringTP.excepciones;

public class ContrasenaInvalidaException extends Exception {
    public ContrasenaInvalidaException(String mensaje) {
        super(mensaje);
    }
}
