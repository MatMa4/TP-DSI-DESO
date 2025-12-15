/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo.pago;

import TP_Back.appSpringTP.modelo.direccion.Direccion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mateo
 */
@Entity
@DiscriminatorValue("PersonaJuridica")
@Getter
@Setter
public class PersonaJuridica extends ResponsableDePago{
    private String razonSocial;
    private String cuit;
    private String telefono;
    @jakarta.persistence.ManyToOne(cascade = jakarta.persistence.CascadeType.ALL)
    @JoinColumns({
        @JoinColumn(name = "jur_calle", referencedColumnName = "calle"),
        @JoinColumn(name = "jur_numero", referencedColumnName = "numero"),
        @JoinColumn(name = "jur_localidad", referencedColumnName = "localidad"),
        @JoinColumn(name = "jur_provincia", referencedColumnName = "provincia"),
        @JoinColumn(name = "jur_pais", referencedColumnName = "pais")
    })

    private Direccion direccion;
    

    public PersonaJuridica(){
    }

    public String getRazonSocial() {
        return this.razonSocial;
    }

    public String getCuit() {
        return this.cuit;
    }
    
    public void setRazonSocial(String razonSocial) {
        this.razonSocial = razonSocial;
    }

    public void setCuit(String cuit) {
        this.cuit = cuit;
    }
    
    public String getTelefono() {
        return this.telefono;
    }

    public Integer getIdResponsable() {
        return this.idResponsable;
    }
    
    public void setIdResponsable(Integer idResponsable) {
        this.idResponsable = idResponsable;
    }
    
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    
    private PersonaJuridica(Builder builder) {
        this.razonSocial = builder.razonSocial;
        this.cuit = builder.cuit;
        this.direccion = builder.direccion;
        this.telefono = builder.telefono;
    }

    public static Builder builder() { return new Builder(); }

    public static class Builder {
        private String razonSocial;
        private String cuit;
        private Direccion direccion;
        private String telefono;

        public Builder razonSocial(String razonSocial) { this.razonSocial = razonSocial; return this; }
        public Builder cuit(String cuit) { this.cuit = cuit; return this; }
        public Builder direccion(Direccion direccion) { this.direccion = direccion; return this; }
        public Builder telefono(String telefono) { this.telefono = telefono; return this; }

        public PersonaJuridica build() { return new PersonaJuridica(this); }
    }
}
