/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;
import java.time.LocalDate;

import Excepcion.CampoObligatorioException;



public class Huesped {
    private String apellido;
    private String nombre;
    private String tipoDocumento;
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private Direccion direccionHuesped;
    private String telefono;
    private String email;
    private String ocupacion;
    private String nacionalidad;
    private String cuit;
    private String posicionIVA;
    private boolean alojado;
    
    //============Creacion=============
    private Huesped(Builder builder){
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
        this.alojado= builder.alojado;
    }
 
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
    
    //===============Builder==============
    public static class Builder{
        private String apellido;
        private String nombre;
        private String tipoDocumento;
        private String numeroDocumento;
        private LocalDate fechaNacimiento;
        private Direccion direccionHuesped;
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
        public Builder direccionHuesped(Direccion direccionHuesped){
            this.direccionHuesped = direccionHuesped;
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
        public Huesped build() {
            if (apellido == null || apellido.trim().isEmpty()) {
                throw new CampoObligatorioException("apellido");
            }
            if (nombre == null || nombre.trim().isEmpty()) {
                throw new CampoObligatorioException("nombre");
            }
            if (tipoDocumento == null || tipoDocumento.trim().isEmpty()) {
                throw new CampoObligatorioException("tipoDocumento");
            }
            if (numeroDocumento == null || numeroDocumento.trim().isEmpty()) {
                throw new CampoObligatorioException("numeroDocumento");
            }
            if (fechaNacimiento == null) {
                throw new CampoObligatorioException("fechaNacimiento");
            }
            if (direccionHuesped == null) {
                throw new CampoObligatorioException("direccionHuesped");
            }
            if (telefono == null || telefono.trim().isEmpty()) {
                throw new CampoObligatorioException("telefono");
            }
            if (ocupacion == null || ocupacion.trim().isEmpty()) {
                throw new CampoObligatorioException("ocupacion");
            }
            if (nacionalidad == null || nacionalidad.trim().isEmpty()) {
                throw new CampoObligatorioException("nacionalidad");
            }

            // Valor por omisión si no se especifica
            if (posicionIVA == null) {
                posicionIVA = "Consumidor Final";
            }

            return new Huesped(this);
        }
    }
    
    //Modificadores
    public void setApellido(String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new CampoObligatorioException("apellido");
        }
        this.apellido = apellido;
    }
    public void setNombre(String nombre) {
        if (nombre == null || nombre.trim().isEmpty()) {
            throw new CampoObligatorioException("apellido");
        }
        this.nombre = nombre;
    }
    public void setTipoDocumento(String tipoDocumento) {
        if (tipoDocumento == null || tipoDocumento.trim().isEmpty()) {
            throw new CampoObligatorioException("apellido");
        }
        this.tipoDocumento = tipoDocumento;
    }
    public void setNumeroDocumento(String numeroDocumento) {
        if (numeroDocumento == null || numeroDocumento.trim().isEmpty()) {
            throw new CampoObligatorioException("apellido");
        }
        this.numeroDocumento = numeroDocumento;
    }
    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        if (fechaNacimiento == null) {
            throw new CampoObligatorioException("apellido");
        }
        this.fechaNacimiento = fechaNacimiento;
    }
    public void setDireccionHuesped(Direccion direccionHuesped) {
        if (direccionHuesped == null) {
            throw new CampoObligatorioException("apellido");
        }
        this.direccionHuesped = direccionHuesped;
    }
    public void setTelefono(String telefono) {
        if (telefono == null || telefono.trim().isEmpty()) {
            throw new CampoObligatorioException("apellido");
        }
        this.telefono = telefono;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public void setOcupacion(String ocupacion) {
        if (ocupacion == null || ocupacion.trim().isEmpty()) {
            throw new CampoObligatorioException("apellido");
        }
        this.ocupacion = ocupacion;
    }
    public void setNacionalidad(String nacionalidad) {
        if (nacionalidad == null || nacionalidad.trim().isEmpty()) {
            throw new CampoObligatorioException("apellido");
        }
        this.nacionalidad = nacionalidad;
    }
    public void setCuit(String cuit) {
        this.cuit = cuit;
    }
    public void setPosicionIVA(String posicionIVA) {
        if (apellido == null || apellido.trim().isEmpty()) {
            this.posicionIVA = "Consumidor Final";
        }
        this.posicionIVA = posicionIVA;
    }
    public void setAlojado(boolean alojado){
        this.alojado=alojado;
    }

}
