/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo.direccion;

/**
 *
 * @author JS
 */

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class DireccionId implements Serializable {
    @jakarta.persistence.Column(name = "calle")
    private String calle;

    @jakarta.persistence.Column(name = "numero")
    private Integer numero;
    
    @jakarta.persistence.Column(name = "localidad")
    private String localidad;
    
    @jakarta.persistence.Column(name = "provincia")
    private String provincia;
    
    @jakarta.persistence.Column(name = "pais")
    private String pais;

    public DireccionId() {}

    public DireccionId(String calle, Integer numero) {
        this.calle = calle;
        this.numero = numero;
    }
    
    

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
        private Integer numero;
        private String localidad;
        private String provincia;
        private String pais;

        public Builder calle(String calle) { this.calle = calle; return this; }
        public Builder numero(Integer numero) { this.numero = numero; return this; }
        public Builder localidad(String localidad) { this.localidad = localidad; return this; }
        public Builder provincia(String provincia) { this.provincia = provincia; return this; }
        public Builder pais(String pais) { this.pais = pais; return this; }

        public DireccionId build() {
            return new DireccionId(this);
        }
    }

}

