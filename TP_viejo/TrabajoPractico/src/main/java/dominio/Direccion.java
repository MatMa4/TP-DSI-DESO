/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;

import repositorio.DireccionDTO;

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

    public Direccion(){
        
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
    public boolean equalsDTO(DireccionDTO dirDTO){
        return calle==dirDTO.getCalle() && numero==dirDTO.getNumero() && departamento==dirDTO.getDepartamento() &&
               piso==dirDTO.getPiso() && codigo==dirDTO.getCodigo() && localidad==dirDTO.getLocalidad() &&
               provincia==dirDTO.getProvincia() && pais==dirDTO.getPais();
    }
}
