/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package repositorio;

import dominio.Direccion;
import java.time.LocalDate;

/**
 *
 * @author mateo
 */
public class HuespedDTO {
    private String apellido;
    private String nombre;
    private String tipoDocumento;
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private DireccionDTO direccionHuesped;
    private String telefono;
    private String email;
    private String ocupacion;
    private String nacionalidad;
    private String cuit;
    private String posicionIVA;
    private boolean alojado;
    
    private HuespedDTO(Builder builder){
        this.apellido = builder.apellido;
        this.nombre = builder.nombre;
        this.tipoDocumento = builder.tipoDocumento;
        this.numeroDocumento = builder.numeroDocumento;
        this.fechaNacimiento = builder.fechaNacimiento;
        this.direccionHuesped = builder.direccionHuesped;
        this.telefono = builder.telefono;
        this.email = builder.email; //No obligatorio
        this.ocupacion = builder.ocupacion;
        this.nacionalidad = builder.nacionalidad;
        this.cuit = builder.cuit; //No obligatorio
        this.posicionIVA = builder.posicionIVA; //Consumidor final por omisión
        this.alojado = builder.alojado;
    }
    
    public static class Builder{
        private String apellido;
        private String nombre;
        private String tipoDocumento;
        private String numeroDocumento;
        private LocalDate fechaNacimiento;
        private DireccionDTO direccionHuesped;
        private String telefono;
        private String email;
        private String ocupacion;
        private String nacionalidad;
        private String cuit;
        private String posicionIVA;
        private boolean alojado;
        
        //Hay que poner una validación de si es null y crear una excepción para cada campo obligatorio
        //Lo mismo para los setters
        public Builder apellido(String apellido){
            this.apellido = apellido;
            return this;
        }
        public Builder nombre(String nombre){
            this.nombre = nombre;
            return this;
        }
        public Builder tipoDocumento(String tipoDocumento){
            this.tipoDocumento = tipoDocumento;
            return this;
        }
        public Builder numeroDocumento(String numeroDocumento){
            this.numeroDocumento = numeroDocumento;
            return this;
        }
        public Builder fechaNacimiento(LocalDate fechaNacimiento){
            this.fechaNacimiento = fechaNacimiento;
            return this;
        }
        public Builder direccionHuesped(DireccionDTO direccionHuesped){
            this.direccionHuesped = direccionHuesped;
            return this;
        }
        public Builder direccionHuesped(Direccion direccionHuesped){ //Para cuando le asignan un objeto Direccion
            DireccionDTO dirDTO = new DireccionDTO(direccionHuesped.getCalle(), direccionHuesped.getNumero(),
                                                 direccionHuesped.getDepartamento(), direccionHuesped.getPiso(),
                                                 direccionHuesped.getCodigo(), direccionHuesped.getLocalidad(),
                                                 direccionHuesped.getProvincia(), direccionHuesped.getPais());
            this.direccionHuesped = dirDTO;
            return this;
        }
        public Builder telefono(String telefono){
            this.telefono = telefono;
            return this;
        }
        public Builder email(String email){
            this.email = email;
            return this;
        }
        public Builder ocupacion(String ocupacion){
            this.ocupacion = ocupacion;
            return this;
        }
        public Builder nacionalidad(String nacionalidad){
            this.nacionalidad = nacionalidad;
            return this;
        }
        public Builder cuit(String cuit){
            this.cuit = cuit;
            return this;
        }
        public Builder posicionIVA(String posicionIVA){
            this.posicionIVA = posicionIVA;
            return this;
        }
        
        public Builder alojado(boolean alojado){
            this.alojado = alojado;
            return this;
        }
        
        public HuespedDTO build(){
            return new HuespedDTO(this);
        }



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

    public DireccionDTO getDireccionHuesped() {
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
     public boolean getAlojado() {
        return alojado;
    }
}
