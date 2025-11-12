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

    public DireccionId() {}

    public DireccionId(String calle, String numero) {
        this.calle = calle;
        this.numero = numero;
    }

    public String getCalle() { return calle; }
    public void setCalle(String calle) { this.calle = calle; }

    public String getNumero() { return numero; }
    public void setNumero(String numero) { this.numero = numero; }

    // ✅ Obligatorio para claves compuestas
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof DireccionId)) return false;
        DireccionId that = (DireccionId) o;
        return Objects.equals(calle, that.calle) && Objects.equals(numero, that.numero);
    }

    @Override
    public int hashCode() {
        return Objects.hash(calle, numero);
    }
    
        private DireccionId(Builder builder) {
        this.calle = builder.calle;
        this.numero = builder.numero;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String calle;
        private String numero;

        public Builder calle(String calle) { this.calle = calle; return this; }
        public Builder numero(String numero) { this.numero = numero; return this; }

        public DireccionId build() {
            return new DireccionId(this);
        }
    }

}

