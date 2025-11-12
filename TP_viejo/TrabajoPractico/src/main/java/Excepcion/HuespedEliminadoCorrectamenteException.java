/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Excepcion;

/**
 *
 * @author PC Gamer
 */
public class HuespedEliminadoCorrectamenteException extends RuntimeException {
    public HuespedEliminadoCorrectamenteException(String nombre, String apellido,String tipoDocumento, String numeroDocumento){
        super("Los datos del huésped" + nombre +", "+ apellido + ",cuyo tipo de documento es " + tipoDocumento +" numero "+ numeroDocumento + "han sido eliminados del sistema. PRESIONE CUALQUIER TECLA PARA CONTINUAR…");   
    } 
    
}
