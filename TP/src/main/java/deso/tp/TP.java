/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package deso.tp;

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

    public static void main(String[] args) throws UsuarioNoEncontradoException, ContrasenaInvalidaException {
        System.out.println("Hola");
        
        GestorDeUsuario gestor = new GestorDeUsuario(new UsuarioDAOImpl("src/main/java/BDD/info.csv"));
        UsuarioDTO userDto = new UsuarioDTO("juan","1234");
        gestor.autenticarUsuario(userDto);
        System.out.print("se pudo");
        
    }
}
