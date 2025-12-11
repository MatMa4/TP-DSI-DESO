/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo.direccion;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import java.util.Objects;
import lombok.Getter;
import lombok.Setter;
/**
 *
 * @author mateo
 */
@Entity
@Getter
@Setter
public class Direccion {
    @EmbeddedId
    @JsonUnwrapped
    private DireccionId id;

    private String departamento;
    private Integer piso;
    private Integer codigo;


    public Direccion(String calle, Integer numero, String departamento, Integer piso,
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

    public void setId(String calle, Integer numero, String localidad, String provincia, String pais){
        this.id.setCalle(calle);
        this.id.setNumero(numero);
        this.id.setLocalidad(localidad);
        this.id.setProvincia(provincia);
        this.id.setPais(pais);
    }
    public String getCalle(){
        return this.id.getCalle();
    }
    public Integer getNumero(){
        return this.id.getNumero();
    }
    public String getLocalidad(){
        return this.id.getLocalidad();
    }
    public String getProvincia(){
        return this.id.getProvincia();
    }
    public String getPais(){
        return this.id.getPais();
    }
        public void setCalle(String calle){
        id.setCalle(calle);
    }
    public void setNumero(Integer numero){
        id.setNumero(numero);
    }
    public void setLocalidad(String localidad){
        id.setLocalidad(localidad);
    }
    public void setProvincia(String provincia){
        id.setProvincia(provincia);
    }
    public void setPais(String pais){
        id.setPais(pais);
    }
    public Boolean equals(Direccion otraDir){
        if(this.getCalle().equals(otraDir.getCalle())){
            if(Objects.equals(this.getNumero(), otraDir.getNumero())){
                if(this.getLocalidad().equals(otraDir.getLocalidad())){
                    if(this.getProvincia().equals(otraDir.getProvincia())){
                        if(this.getPais().equals(otraDir.getPais())){
                            return true;
                        }
                    }
                }
            }
        }
        return false;
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
