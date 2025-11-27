package TP_Back.appSpringTP.DTOs;

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
