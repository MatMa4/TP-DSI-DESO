/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Exception.java to edit this template
 */
package Excepcion;

/**
 *
 * @author mateo
 */
public class CampoObligatorioException extends RuntimeException {
    public CampoObligatorioException(String campo) {
        super("El campo obligatorio '" + campo + "' no puede ser null ni vacío.");
    }
}

