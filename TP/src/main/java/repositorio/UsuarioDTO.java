/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositorio;

/**
 *
 * @author JS
 */
public class UsuarioDTO {
    private String username;
    private String passw;

    public UsuarioDTO(String user, String pass) {
        this.username = user;
        this.passw = pass;
    }

    //Getters
    public String getUsername() {
        return username;
    }

    public String getPassw() {
        return passw;
    }
}
