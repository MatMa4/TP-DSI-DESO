package TP_Back.appSpringTP.DTOs;

import java.util.Date;

public class ReservaDTO {
    private Integer idReserva;
    private Date fechaInicio;
    private Date fechaFin;
    private String estado;
    private String nombre;
    private String apellido;
    private String telefono;
    private Integer habitacionNumero;

    public ReservaDTO() {
    }

    // Getters and Setters
    public Integer getIdReserva() { return idReserva; }
    public void setIdReserva(Integer idReserva) { this.idReserva = idReserva; }

    public Date getFechaInicio() { return fechaInicio; }
    public void setFechaInicio(Date fechaInicio) { this.fechaInicio = fechaInicio; }

    public Date getFechaFin() { return fechaFin; }
    public void setFechaFin(Date fechaFin) { this.fechaFin = fechaFin; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public Integer getHabitacionNumero() { return habitacionNumero; }
    public void setHabitacionNumero(Integer habitacionNumero) { this.habitacionNumero = habitacionNumero; }
}
