/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import Excepcion.UsuarioNoEncontradoException;
import dominio.Usuario;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 *
 * @author JS
 */
public class UsuarioDAOImpl implements UsuarioDAO {
    private String archivo;

    public UsuarioDAOImpl(String archivo) {
        this.archivo = archivo;
    }

    @Override
    public Usuario obtenerUsuario(String username) throws UsuarioNoEncontradoException {
        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                if (datos[0].equals(username)) {
                    return new Usuario(datos[0], datos[1], datos[2]);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        throw new UsuarioNoEncontradoException("Usuario " + username + " no encontrado.");
    }
}
