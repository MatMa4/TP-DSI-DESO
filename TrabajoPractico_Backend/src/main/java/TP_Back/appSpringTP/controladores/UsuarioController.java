package TP_Back.appSpringTP.controladores;

import TP_Back.appSpringTP.DTOs.UsuarioDTO;
import TP_Back.appSpringTP.excepciones.ContrasenaInvalidaException;
import TP_Back.appSpringTP.excepciones.UsuarioExistenteException;
import TP_Back.appSpringTP.excepciones.UsuarioNoEncontradoException;
import TP_Back.appSpringTP.gestores.GestorDeUsuario;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/usuario")
public class UsuarioController {

    @Autowired
    private GestorDeUsuario gestorDeUsuario;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            gestorDeUsuario.autenticarUsuario(usuarioDTO);
            return ResponseEntity.ok("Login exitoso");
        } catch (UsuarioNoEncontradoException | ContrasenaInvalidaException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    @PostMapping("/crear")
    public ResponseEntity<?> crear(@RequestBody UsuarioDTO usuarioDTO) {
        try {
            gestorDeUsuario.crearUsuario(usuarioDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body("Usuario creado exitosamente");
        } catch (UsuarioExistenteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
