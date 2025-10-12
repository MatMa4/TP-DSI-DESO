/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.Scanner;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import dominio.Huesped;
import repositorio.DireccionDTO;
import repositorio.HuespedDTO;
import servicios.GestorDeHuesped;


/**
 *
 * @author mateo
 */
public class CU10 {
    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        System.out.print("Apellido: ");
        String apellido = in.nextLine();
        System.out.print("Nombre: ");
        String nombre = in.nextLine();
        System.out.print("Tipo de documento [DNI, LE, LC, Pasaporte, Otro]: ");
        String tipoDocumento = in.nextLine();
        System.out.print("Número de documento: ");
        String numeroDocumento = in.nextLine();
        System.out.print("CUIT (no obligatorio) (1): ");
        String cuit = in.nextLine();
        System.out.print("Posición frente al IVA (Consumidor final por omisión): ");
        String posicionIVA = in.nextLine();
        System.out.print("Fecha de nacimiento (YYYY-MM-DD): ");
        LocalDate fechaNacimiento = stringADate(in.nextLine());
        System.out.print("Teléfono: ");
        String telefono = in.nextLine();
        System.out.print("Email (no obligatorio): ");
        String email = in.nextLine();
        System.out.print("Ocupación: ");
        String ocupacion = in.nextLine();
        System.out.print("Nacionalidad: ");
        String nacionalidad = in.nextLine();
        System.out.print("Dirección - Calle: ");
        String calle = in.nextLine();
        System.out.print("Dirección - Número: ");
        String numero = in.nextLine();
        System.out.print("Dirección - Departamento: ");
        String departamento = in.nextLine();
        System.out.print("Dirección - Piso: ");
        int piso = stringAInt(in.nextLine());
        System.out.print("Dirección - Código postal: ");
        int codigoPostal = stringAInt(in.nextLine());
        System.out.print("Dirección - Localidad: ");
        String localidad = in.nextLine();
        System.out.print("Dirección - Provincia: ");
        String provincia = in.nextLine();
        System.out.print("Dirección - País: ");
        String pais = in.nextLine();


        DireccionDTO direccion = new DireccionDTO(calle, numero, departamento, piso, codigoPostal, localidad, provincia, pais);
        HuespedDTO.Builder builder = new HuespedDTO.Builder()
            .apellido(apellido)
            .nombre(nombre)
            .tipoDocumento(tipoDocumento)
            .numeroDocumento(numeroDocumento)
            .fechaNacimiento(fechaNacimiento)
            .direccionHuesped(direccion)
            .telefono(telefono)
            .ocupacion(ocupacion)
            .nacionalidad(nacionalidad);

        if (email != null) {
            builder.email(email);
        }
        if (cuit != null) {
            builder.cuit(cuit);
        }
        if (posicionIVA != null) {
            builder.posicionIVA(posicionIVA);
        }
        HuespedDTO huespedNuevo = builder.build();

        HuespedDTO huespedAntiguo = GestorDeHuesped.consultarDocumento(tipoDocumento, numeroDocumento);
        if (huespedAntiguo == null){
            GestorDeHuesped.registrarHuesped(huespedNuevo);
            System.out.println("Huésped registrado exitosamente.");
        } else {//Hay un huesped con ese documento, hacer la lógica de todo eso
            
            System.out.println("Huésped modificado exitosamente.");
        }
    }
    public static int stringAInt(String texto) throws NumberFormatException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new NumberFormatException("El texto está vacío o es nulo.");
        }
        try {
            return Integer.parseInt(texto.trim());
        } catch (NumberFormatException e) {
            throw new NumberFormatException("Formato inválido para entero: '" + texto + "'");
        }
    }
    public static LocalDate stringADate(String texto) throws DateTimeParseException {
        if (texto == null || texto.trim().isEmpty()) {
            throw new DateTimeParseException("El texto está vacío o es nulo.", texto, 0);
        }
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            return LocalDate.parse(texto.trim(), formatter);
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException("Formato de fecha inválido: '" + texto + "'", texto, e.getErrorIndex());

        }
    }

}
