/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.CascadeType;
import jakarta.persistence.EmbeddedId;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "huesped")
public class Huesped {
    
    @EmbeddedId
    @JsonUnwrapped
    private HuespedId id;
    
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

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
        @JoinColumn(name = "direccion_calle", referencedColumnName = "calle"),
        @JoinColumn(name = "direccion_numero", referencedColumnName = "numero"),
        @JoinColumn(name = "direccion_localidad", referencedColumnName = "localidad"),
        @JoinColumn(name = "direccion_provincia", referencedColumnName = "provincia"),
        @JoinColumn(name = "direccion_pais", referencedColumnName = "pais")
    })
    private Direccion direccionHuesped;
 
    public Huesped(){

    }

        //Getters
    public String getApellido() {
    return apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoDocumento() {
        return this.id.getTipoDocumento();
    }

    public String getNumeroDocumento() {
        return this.id.getNumeroDocumento();
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Direccion getDireccionHuesped() {
        return direccionHuesped;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public String getCuit() {
        return cuit;
    }

    public String getPosicionIVA() {
        return posicionIVA;
    }
    
    public boolean getAlojado(){
        return alojado;
    }
    private Huesped(Builder builder) {
        this.id = builder.id;
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

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private HuespedId id;
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
        private Direccion direccion;

        public Builder id(HuespedId id) { this.id = id; return this; }
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
        public Builder direccion(Direccion direccion) { this.direccion = direccion; return this; }

        public Huesped build() { return new Huesped(this); }
    }
}

