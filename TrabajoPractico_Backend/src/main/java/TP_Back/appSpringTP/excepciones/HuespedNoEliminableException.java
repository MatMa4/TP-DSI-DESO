/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package TP_Back.appSpringTP.excepciones;

/**
 *
 * @author mateo
 */
public class HuespedNoEliminableException extends RuntimeException {

    /**
     * Creates a new instance of <code>HuespedNoEliminable</code> without detail
     * message.
     */
    public HuespedNoEliminableException() {
    }

    /**
     * Constructs an instance of <code>HuespedNoEliminable</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public HuespedNoEliminableException(String msg) {
        super(msg);
    }
}
