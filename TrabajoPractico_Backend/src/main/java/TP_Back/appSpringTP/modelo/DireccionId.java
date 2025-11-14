/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo;

/**
 *
 * @author JS
 */

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class DireccionId implements Serializable {
    private String calle;
    private String numero;
    private String localidad;
    private String provincia;
    private String pais;

    public DireccionId() {}

    public DireccionId(String calle, String numero) {
        this.calle = calle;
        this.numero = numero;
    }

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }
    
    public String getLocalidad() { return localidad; }
    public void setLocalidad(String localidad) { this.localidad = localidad; }
    
    public String getProvincia() { return provincia; }
    public void setProvincia(String provincia ) { this.provincia =provincia ; }
    
    public String getPais() { return pais; }
    public void setPais(String pais ) { this.pais =pais ; }
    
    

    // ✅ Obligatorio para claves compuestas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DireccionId)) return false;
        DireccionId that = (DireccionId) o;
        return Objects.equals(calle, that.calle) && Objects.equals(numero, that.numero) && Objects.equals(localidad, that.localidad) && Objects.equals(provincia, that.provincia) && Objects.equals(pais, that.pais);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calle, numero, localidad, provincia, pais);
    }
    
        private DireccionId(Builder builder) {
        this.calle = builder.calle;
        this.numero = builder.numero;
        this.localidad = builder.localidad;
        this.provincia = builder.provincia;
        this.pais = builder.pais;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String calle;
        private String numero;
        private String localidad;
        private String provincia;
        private String pais;

        public Builder calle(String calle) { this.calle = calle; return this; }
        public Builder numero(String numero) { this.numero = numero; return this; }
        public Builder localidad(String localidad) { this.localidad = localidad; return this; }
        public Builder provincia(String provincia) { this.provincia = provincia; return this; }
        public Builder pais(String pais) { this.pais = pais; return this; }

        public DireccionId build() {
            return new DireccionId(this);
        }
    }

}

