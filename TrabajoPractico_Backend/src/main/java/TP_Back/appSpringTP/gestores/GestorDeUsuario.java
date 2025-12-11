package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.UsuarioDAO;
import TP_Back.appSpringTP.DTOs.UsuarioDTO;
import TP_Back.appSpringTP.excepciones.ContrasenaInvalidaException;
import TP_Back.appSpringTP.excepciones.UsuarioExistenteException;
import TP_Back.appSpringTP.excepciones.UsuarioNoEncontradoException;
import TP_Back.appSpringTP.modelo.usuario.Usuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

@Service
public class GestorDeUsuario {

    @Autowired
    private UsuarioDAO usuarioDAO;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void autenticarUsuario(UsuarioDTO user) throws UsuarioNoEncontradoException, ContrasenaInvalidaException {
        Usuario usuario = usuarioDAO.findById(user.getUsername())
                .orElseThrow(() -> new UsuarioNoEncontradoException("Usuario " + user.getUsername() + " no encontrado."));

        if (!passwordEncoder.matches(user.getPassw(), usuario.getContrasena())) {
            throw new ContrasenaInvalidaException("La contraseña no es válida.");
        }
    }

    public Usuario crearUsuario(UsuarioDTO userDTO) throws UsuarioExistenteException {
        if (usuarioDAO.existsById(userDTO.getUsername())) {
            throw new UsuarioExistenteException("El usuario " + userDTO.getUsername() + " ya existe.");
        }
        Usuario nuevoUsuario = new Usuario();
        nuevoUsuario.setUsuario(userDTO.getUsername());
        nuevoUsuario.setContrasena(passwordEncoder.encode(userDTO.getPassw()));
        nuevoUsuario.setRol(userDTO.getRol());
        return usuarioDAO.save(nuevoUsuario);
    }
}
