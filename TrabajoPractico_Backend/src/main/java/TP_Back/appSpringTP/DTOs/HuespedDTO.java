/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DTOs;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mateo
 */
@Getter
@Setter
public class HuespedDTO {
  
  
    private String numeroDocumento;
    private String tipoDocumento;
    private String apellido;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String email;
    private String ocupacion;
    private String nacionalidad;
    private String cuit;
    private String posicionIVA;
    private boolean alojado;
    private DireccionDTO direccionHuesped;
 
    public HuespedDTO() {
    }
    
    public boolean getAlojado(){
        return alojado;
    }

    private HuespedDTO(Builder builder) {
        this.numeroDocumento = builder.numeroDocumento;
        this.tipoDocumento = builder.tipoDocumento;
        this.apellido = builder.apellido;
        this.nombre = builder.nombre;
        this.fechaNacimiento = builder.fechaNacimiento;
        this.telefono = builder.telefono;
        this.email = builder.email;
        this.ocupacion = builder.ocupacion;
        this.nacionalidad = builder.nacionalidad;
        this.cuit = builder.cuit;
        this.posicionIVA = builder.posicionIVA;
        this.alojado = builder.alojado;
        this.direccionHuesped = builder.direccion;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String numeroDocumento;
        private String tipoDocumento;
        private String apellido;
        private String nombre;
        private LocalDate fechaNacimiento;
        private String telefono;
        private String email;
        private String ocupacion;
        private String nacionalidad;
        private String cuit;
        private String posicionIVA;
        private boolean alojado;
        private DireccionDTO direccion;

        public Builder numeroDocumento(String numeroDocumento) { this.numeroDocumento = numeroDocumento; return this; }
        public Builder tipoDocumento(String tipoDocumento) { this.tipoDocumento = tipoDocumento; return this; }
        public Builder apellido(String apellido) { this.apellido = apellido; return this; }
        public Builder nombre(String nombre) { this.nombre = nombre; return this; }
        public Builder fechaNacimiento(LocalDate fechaNacimiento) { this.fechaNacimiento = fechaNacimiento; return this; }
        public Builder telefono(String telefono) { this.telefono = telefono; return this; }
        public Builder email(String email) { this.email = email; return this; }
        public Builder ocupacion(String ocupacion) { this.ocupacion = ocupacion; return this; }
        public Builder nacionalidad(String nacionalidad) { this.nacionalidad = nacionalidad; return this; }
        public Builder cuit(String cuit) { this.cuit = cuit; return this; }
        public Builder posicionIVA(String posicionIVA) { this.posicionIVA = posicionIVA; return this; }
        public Builder alojado(boolean alojado) { this.alojado = alojado; return this; }
        public Builder direccion(DireccionDTO direccion) { this.direccion = direccion; return this; }

        public HuespedDTO build() {
            return new HuespedDTO(this);
        }
    }


}
