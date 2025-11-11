/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Excepcion.UsuarioNoEncontradoException;
import dominio.Usuario;

/**
 *
 * @author JS
 */
public interface UsuarioDAO {
    Usuario obtenerUsuario(String username) throws UsuarioNoEncontradoException;
}
