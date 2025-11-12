/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Excepcion;

/**
 *
 * @author PC Gamer
 */
public class HuespedNoEliminableException extends Exception {
    public HuespedNoEliminableException(){
        super("El huésped no puede ser eliminado pues se ha alojado en el Hotel en alguna oportunidad. "
                + "PRESIONE CUALQUIER TECLA PARA CONTINUAR…”");
        
        
    }
}
