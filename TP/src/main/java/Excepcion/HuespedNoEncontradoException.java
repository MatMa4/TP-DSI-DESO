/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package Excepcion;

/**
 *
 * @author mateo
 */
public class HuespedNoEncontradoException extends RuntimeException {
    public HuespedNoEncontradoException(String tipoDocumento, String numeroDocumento) {
        super("No se encontró ningún huésped con " +
              "tipo de documento '" + tipoDocumento + "' y número '" + numeroDocumento + "'.");
    }
}

