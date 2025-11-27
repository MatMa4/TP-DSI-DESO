package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.UsuarioDAO;
import TP_Back.appSpringTP.DTOs.UsuarioDTO;
import TP_Back.appSpringTP.excepciones.ContrasenaInvalidaException;
import TP_Back.appSpringTP.excepciones.UsuarioNoEncontradoException;
import TP_Back.appSpringTP.modelo.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GestorDeUsuario {

    @Autowired
    private UsuarioDAO usuarioDAO;

    public void autenticarUsuario(UsuarioDTO user) throws UsuarioNoEncontradoException, ContrasenaInvalidaException {
        Usuario usuario = usuarioDAO.findById(user.getUsername())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario " + user.getUsername() + " no encontrado."));

        if (!usuario.getContrasena().equals(user.getPassw())) {
            throw new ContrasenaInvalidaException("La contraseña no es válida.");
        }
    }
}
