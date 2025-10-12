/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package deso.tp;
import java.time.LocalDate;

import DAOs.HuespedDAO;
import DAOs.HuespedDAOImpl;
import dominio.Huesped;
import dominio.Direccion;
import repositorio.HuespedDTO;
import DAOs.UsuarioDAOImpl;
import Excepcion.ContrasenaInvalidaException;
import Excepcion.UsuarioNoEncontradoException;
import repositorio.UsuarioDTO;
import servicios.GestorDeUsuario;

/**
 *
 * @author mateo
 */
public class TP {

    public static void main(String[] args) {
        
        System.out.println("Hola");
        
        GestorDeUsuario gestor = new GestorDeUsuario(new UsuarioDAOImpl("src/main/java/BDD/info.csv"));
        UsuarioDTO userDto = new UsuarioDTO("juan","1234");
        try{gestor.autenticarUsuario(userDto);}
        catch(ContrasenaInvalidaException | UsuarioNoEncontradoException e){
            System.out.println(e.getMessage());
        }
        System.out.print("se pudo");

    }
}
