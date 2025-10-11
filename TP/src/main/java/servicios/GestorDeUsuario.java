/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import DAOs.UsuarioDAO;
import Excepcion.ContrasenaInvalidaException;
import Excepcion.UsuarioNoEncontradoException;
import dominio.Usuario;
import repositorio.UsuarioDTO;

/**
 *
 * @author JS
 */
public class GestorDeUsuario {
    private UsuarioDAO usuarioDAO;

    public GestorDeUsuario(UsuarioDAO usuarioDAO) {
        this.usuarioDAO = usuarioDAO;
    }

    public Usuario autenticarUsuario(UsuarioDTO user)
            throws UsuarioNoEncontradoException, ContrasenaInvalidaException {
        Usuario usuario = usuarioDAO.obtenerUsuario(user.getUsername());

        if (!usuario.getContrasena().equals(user.getPassw())) {
            throw new ContrasenaInvalidaException("La contraseña no es válida.");
        }
        return usuario;
    }
}
