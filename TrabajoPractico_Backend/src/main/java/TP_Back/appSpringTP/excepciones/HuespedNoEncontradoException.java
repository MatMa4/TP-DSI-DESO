/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package TP_Back.appSpringTP.excepciones;

/**
 *
 * @author mateo
 */
public class HuespedNoEncontradoException extends RuntimeException {

    /**
     * Creates a new instance of <code>HuespedNoEncontradoException</code>
     * without detail message.
     */
    public HuespedNoEncontradoException() {
    }

    /**
     * Constructs an instance of <code>HuespedNoEncontradoException</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public HuespedNoEncontradoException(String msg) {
        super(msg);
    }
}
