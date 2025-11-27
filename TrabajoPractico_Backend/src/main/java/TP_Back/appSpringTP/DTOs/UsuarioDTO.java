package TP_Back.appSpringTP.DTOs;

public class UsuarioDTO {
    private String username;
    private String passw;
    private String rol;

    public UsuarioDTO() {
    }

    public UsuarioDTO(String user, String pass) {
        this.username = user;
        this.passw = pass;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPassw(String passw) {
        this.passw = passw;
    }

    //Getters
    public String getUsername() {
        return username;
    }

    public String getPassw() {
        return passw;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
