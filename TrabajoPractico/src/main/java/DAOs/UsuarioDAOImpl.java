/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Excepcion.UsuarioNoEncontradoException;
import dominio.Usuario;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;

/**
 *
 * @author JS
 */
public class UsuarioDAOImpl implements UsuarioDAO {
    private String archivo;
    private ObjectMapper objectMapper;

    public UsuarioDAOImpl(String archivo) {
        this.archivo = archivo;
        this.objectMapper = new ObjectMapper();
    }

    @Override
    public Usuario obtenerUsuario(String username) throws UsuarioNoEncontradoException {
        try {
            // Leer el archivo JSON como un array de Usuario
            Usuario[] usuarios = objectMapper.readValue(new File(archivo), Usuario[].class);
            
            // Buscar el usuario por username
            for (Usuario usuario : usuarios) {
                if (usuario.getUsuario().equals(username)) {
                    return usuario;
                }
            }
            
            // Si no encuentra el usuario, lanza excepción
            throw new UsuarioNoEncontradoException("Usuario " + username + " no encontrado.");
        } catch (IOException e) {
            e.printStackTrace();
            throw new UsuarioNoEncontradoException("Error al leer el archivo: " + e.getMessage());
        }
    }
}
