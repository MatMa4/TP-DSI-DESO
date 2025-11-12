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
    private String localidad;
    private String provincia;
    private String pais;

    public Direccion(String calle, String numero, String departamento, Integer piso,
                     Integer codigo, String localidad, String provincia, String pais) {
        this.id.setCalle(calle);
        this.id.setNumero(numero);
        this.departamento = departamento;
        this.piso = piso;
        this.codigo = codigo;
        this.localidad = localidad;
        this.provincia = provincia;
        this.pais = pais;
    }

    public Direccion(){
        
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
        return localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public String getPais() {
        return pais;
    }
}
