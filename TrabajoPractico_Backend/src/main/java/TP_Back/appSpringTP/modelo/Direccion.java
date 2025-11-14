/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
/**
 *
 * @author mateo
 */
@Entity
public class Direccion {
    @EmbeddedId
    @JsonUnwrapped
    private DireccionId id;

    private String departamento;
    private Integer piso;
    private Integer codigo;


    public Direccion(String calle, String numero, String departamento, Integer piso,
                     Integer codigo, String localidad, String provincia, String pais) {
        this.id.setCalle(calle);
        this.id.setNumero(numero);
        this.departamento = departamento;
        this.piso = piso;
        this.codigo = codigo;
        this.id.setLocalidad(localidad);
        this.id.setProvincia(provincia);
        this.id.setPais(pais);
    }

    public Direccion(){
        this.id = new DireccionId();
    }
    //Getters
    public String getCalle() {
        return id.getCalle();
    }

    public String getNumero() {
        return id.getNumero();
    }

    public String getDepartamento() {
        return departamento;
    }

    public Integer getPiso() {
        return piso;
    }

    public Integer getCodigo() {
        return codigo;
    }

    public String getLocalidad() {
        return id.getLocalidad();
    }

    public String getProvincia() {
        return id.getProvincia();
    }

    public String getPais() {
        return id.getPais();
    }
    
    private Direccion(Builder builder) {
        this.id = builder.id;
        this.departamento = builder.departamento;
        this.piso = builder.piso;
        this.codigo = builder.codigo;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private DireccionId id;
        private String departamento;
        private Integer piso;
        private Integer codigo;

        public Builder id(DireccionId id) { this.id = id; return this; }
        public Builder departamento(String departamento) { this.departamento = departamento; return this; }
        public Builder piso(Integer piso) { this.piso = piso; return this; }
        public Builder codigo(Integer codigo) { this.codigo = codigo; return this; }

        public Direccion build() {
            return new Direccion(this);
        }
    }

}
