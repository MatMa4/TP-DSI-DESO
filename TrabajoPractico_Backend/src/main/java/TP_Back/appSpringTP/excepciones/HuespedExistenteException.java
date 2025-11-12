/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package TP_Back.appSpringTP.excepciones;

/**
 *
 * @author mateo
 */
public class HuespedExistenteException extends RuntimeException {

    /**
     * Creates a new instance of <code>HuespedExistenteException</code> without
     * detail message.
     */
    public HuespedExistenteException() {
    }

    /**
     * Constructs an instance of <code>HuespedExistenteException</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public HuespedExistenteException(String msg) {
        super(msg);
    }
}
