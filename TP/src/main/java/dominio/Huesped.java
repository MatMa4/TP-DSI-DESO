/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dominio;
import dominio.Direccion;
import java.time.LocalDate;



public class Huesped {
    private String apellido;
    private String nombre;
    private String tipoDocumento;
    private String numeroDocumento;
    private LocalDate fechaNacimiento;
    private Direccion direccionHuesped;
    private String telefono;
    private String email;
    private String ocupación;
    private String nacionalidad;
    private String cuit;
    private String posicionIVA;

    public Huesped(String apellido, String nombre, String tipoDocumento, String numeroDocumento, LocalDate fechaNacimiento, Direccion direccionHuesped, String telefono, String email, String ocupación, String nacionalidad, String cuit, String posicionIVA){
        this.apellido = apellido;
        this.nombre = nombre;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.fechaNacimiento = fechaNacimiento;
        this.direccionHuesped = direccionHuesped;
        this.telefono = telefono;
        this.email = email; //No obligatorio
        this.ocupación = ocupación;
        this.nacionalidad = nacionalidad;
        this.cuit = cuit; //No obligatorio
        this.posicionIVA = posicionIVA; //Consumidor final por omisión
    }
}
