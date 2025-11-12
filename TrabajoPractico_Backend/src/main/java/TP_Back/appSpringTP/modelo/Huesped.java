/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo;
import jakarta.persistence.CascadeType;
import java.time.LocalDate;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;


@Entity
@Table(name = "huesped")
public class Huesped {
    
    @Id
    private String numeroDocumento;

    private String tipoDocumento;
    private String apellido;
    private String nombre;
    private LocalDate fechaNacimiento;
    private String telefono;
    private String email;
    private String ocupacion;
    private String nacionalidad;
    private String cuit;
    private String posicionIVA;
    private boolean alojado;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumns({
        @JoinColumn(name = "direccion_calle", referencedColumnName = "calle"),
        @JoinColumn(name = "direccion_numero", referencedColumnName = "numero")
    })
    private Direccion direccionHuesped;
 
    public Huesped(){

    }
        //Getters
    public String getApellido() {
    return apellido;
    }

    public String getNombre() {
        return nombre;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public Direccion getDireccionHuesped() {
        return direccionHuesped;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getEmail() {
        return email;
    }

    public String getOcupacion() {
        return ocupacion;
    }

    public String getNacionalidad() {
        return nacionalidad;
    }

    public String getCuit() {
        return cuit;
    }

    public String getPosicionIVA() {
        return posicionIVA;
    }
    public boolean getAlojado(){
        return alojado;
    }
}
