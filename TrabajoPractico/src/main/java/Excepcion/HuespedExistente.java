/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package Excepcion;

/**
 *
 * @author mateo
 */
public class HuespedExistente extends Exception {

    /**
     * Creates a new instance of <code>HuespedExistente</code> without detail
     * message.
     */
    public HuespedExistente() {
    }

    /**
     * Constructs an instance of <code>HuespedExistente</code> with the
     * specified detail message.
     *
     * @param msg the detail message.
     */
    public HuespedExistente(String msg) {
        super(msg);
    }
}
