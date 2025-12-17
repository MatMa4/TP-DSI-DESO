/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo.huesped;

/**
 *
 * @author JS
 */

import jakarta.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class HuespedId implements Serializable {

    private String numeroDocumento;
    private String tipoDocumento;

    // Constructor vacío obligatorio
    public HuespedId() {
    }

    public HuespedId(String numeroDocumento, String tipoDocumento) {
        this.numeroDocumento = numeroDocumento;
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof HuespedId))
            return false;
        HuespedId that = (HuespedId) o;
        return Objects.equals(numeroDocumento, that.numeroDocumento) &&
                Objects.equals(tipoDocumento, that.tipoDocumento);
    }

    @Override
    public int hashCode() {
        return Objects.hash(numeroDocumento, tipoDocumento);
    }

    // ================================
    // Constructor por Builder (opcional)
    // ================================
    private HuespedId(Builder builder) {
        this.numeroDocumento = builder.numeroDocumento;
        this.tipoDocumento = builder.tipoDocumento;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String numeroDocumento;
        private String tipoDocumento;

        public Builder numeroDocumento(String numeroDocumento) {
            this.numeroDocumento = numeroDocumento;
            return this;
        }

        public Builder tipoDocumento(String tipoDocumento) {
            this.tipoDocumento = tipoDocumento;
            return this;
        }

        public HuespedId build() {
            return new HuespedId(this);
        }
    }
}
