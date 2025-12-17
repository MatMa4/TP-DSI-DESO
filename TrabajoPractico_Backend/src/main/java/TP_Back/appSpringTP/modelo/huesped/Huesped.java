/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo.huesped;

import TP_Back.appSpringTP.modelo.direccion.Direccion;
import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.EmbeddedId;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "huesped")
@Getter
@Setter
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

    @ManyToOne
    @JoinColumns({
            @JoinColumn(name = "direccion_calle", referencedColumnName = "calle"),
            @JoinColumn(name = "direccion_localidad", referencedColumnName = "localidad"),
            @JoinColumn(name = "direccion_numero", referencedColumnName = "numero"),
            @JoinColumn(name = "direccion_pais", referencedColumnName = "pais"),
            @JoinColumn(name = "direccion_provincia", referencedColumnName = "provincia")
    })
    private Direccion direccionHuesped;

    public Huesped() {
        this.id = new HuespedId();
    }

    public String getTipoDocumento() {
        return this.id.getTipoDocumento();
    }

    public String getNumeroDocumento() {
        return this.id.getNumeroDocumento();
    }

    public void setTipoDocumento(String tipo) {
        this.id.setTipoDocumento(tipo);
    }

    public void setNumeroDocumento(String numero) {
        this.id.setNumeroDocumento(numero);
    }

    public Boolean getAlojado() {
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

    public static Builder builder() {
        return new Builder();
    }

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

        public Builder id(HuespedId id) {
            this.id = id;
            return this;
        }

        public Builder apellido(String apellido) {
            this.apellido = apellido;
            return this;
        }

        public Builder nombre(String nombre) {
            this.nombre = nombre;
            return this;
        }

        public Builder fechaNacimiento(LocalDate fechaNacimiento) {
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }

        public Builder telefono(String telefono) {
            this.telefono = telefono;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder ocupacion(String ocupacion) {
            this.ocupacion = ocupacion;
            return this;
        }

        public Builder nacionalidad(String nacionalidad) {
            this.nacionalidad = nacionalidad;
            return this;
        }

        public Builder cuit(String cuit) {
            this.cuit = cuit;
            return this;
        }

        public Builder posicionIVA(String posicionIVA) {
            this.posicionIVA = posicionIVA;
            return this;
        }

        public Builder alojado(boolean alojado) {
            this.alojado = alojado;
            return this;
        }

        public Builder direccion(Direccion direccion) {
            this.direccion = direccion;
            return this;
        }

        public Huesped build() {
            return new Huesped(this);
        }
    }
}
