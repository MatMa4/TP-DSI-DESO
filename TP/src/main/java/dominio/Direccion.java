/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

/**
 *
 * @author mateo
 */
public class Direccion {
    private String calle;
    private String numero;
    private String departamento;
    private Integer piso;
    private Integer codigo;
    private String localidad;
    private String provincia;
    private String pais;

    public Direccion(String calle, String numero, String departamento, Integer piso,
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


    public boolean equals(Direccion direc1, Direccion direc2){
        return direc1.getCalle().equals(direc2.getCalle()) &&
               direc1.getNumero().equals(direc2.getNumero()) &&
               direc1.getDepartamento().equals(direc2.getDepartamento()) &&
               direc1.getPiso().equals(direc2.getPiso()) &&
               direc1.getCodigo().equals(direc2.getCodigo()) &&
               direc1.getLocalidad().equals(direc2.getLocalidad()) &&
               direc1.getProvincia().equals(direc2.getProvincia()) &&
               direc1.getPais().equals(direc2.getPais());
    }

    //Getters
    public String getCalle() {
        return calle;
    }

    public String getNumero() {
        return numero;
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
