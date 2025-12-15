/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DTOs;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mateo
 */
@Getter
@Setter
public class DireccionDTO {

    private String calle;
    private Integer numero;
    private String departamento;
    private Integer piso;
    private Integer codigo;
    private String localidad;
    private String provincia;
    private String pais;

    public DireccionDTO(String calle, Integer numero, String departamento, Integer piso,
                     Integer codigo, String localidad, String provincia, String pais) {
        this.calle = calle;
        this.numero = numero;
        this.departamento = departamento;
        this.piso = piso;
        this.codigo = codigo;
        this.localidad = localidad;
        this.provincia = provincia;
        this.pais = pais;
    }

    public DireccionDTO(){
        
    }
        private DireccionDTO(Builder builder) {
        this.calle = builder.calle;
        this.numero = builder.numero;
        this.departamento = builder.departamento;
        this.piso = builder.piso;
        this.codigo = builder.codigo;
        this.localidad = builder.localidad;
        this.provincia = builder.provincia;
        this.pais = builder.pais;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private String calle;
        private String localidad;
        private Integer numero;
        private String pais;   
        private String provincia; 
        private String departamento;
        private Integer piso;
        private Integer codigo;

        public Builder calle(String calle) { this.calle = calle; return this; }
        public Builder numero(Integer numero) { this.numero = numero; return this; }
        public Builder departamento(String departamento) { this.departamento = departamento; return this; }
        public Builder piso(Integer piso) { this.piso = piso; return this; }
        public Builder codigo(Integer codigo) { this.codigo = codigo; return this; }
        public Builder localidad(String localidad) { this.localidad = localidad; return this; }
        public Builder provincia(String provincia) { this.provincia = provincia; return this; }
        public Builder pais(String pais) { this.pais = pais; return this; }

        public DireccionDTO build() {
            return new DireccionDTO(this);
        }
    }

}
