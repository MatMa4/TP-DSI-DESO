/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package TrabajoPractico.TPBack;



import java.time.LocalDate;
import servicio.CU01;
import servicio.CU02;
import java.util.InputMismatchException;
import java.util.Scanner;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import repositorio.DireccionDTO;
import repositorio.HuespedDTO;
import dominio.Huesped;
import dominio.Direccion;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;

/**
 *
 * @author mateo
 */
@SpringBootApplication
public class TpBackApplication {
    private static Scanner scanner = new Scanner(System.in);
    
    public static void main(String[] args){
        SpringApplication.run(TpBackApplication.class, args);      
    }
    @Bean
    CommandLineRunner init(HuespedDAO huespedDAO) {
        return args -> {
            Huesped huespedNuevo = new Huesped();
            huespedNuevo.setNombre("Juan");
            huespedNuevo.setApellido("Pérez");
            huespedNuevo.setDni("12345678");

            huespedDAO.save(huespedNuevo);

            System.out.println("hola");
            System.out.println(huespedNuevo.getId());
        };
    }

    private static Huesped huespedPrueba() {
        String apellido = "apellido".toUpperCase();
        String nombre = "nombrePrueba".toUpperCase();
        String tipoDocumento = "DNI".toUpperCase();
        String numeroDocumento = "12345679".toUpperCase();
        String cuit = "2012312443".toUpperCase();
        String posicionIVA = "Responsable Inscripto".toUpperCase();
        LocalDate fechaNacimiento = LocalDate.parse("2004-05-20");
        String telefono = "3421234567";
        String email = "e@mail.com".toUpperCase();
        String ocupacion = "estudiante".toUpperCase();
        String nacionalidad = "argentino".toUpperCase();
        String calle = "callePrueba".toUpperCase();
        String numero = "1234";
        String departamento = "A".toUpperCase();
        int piso = 2;
        int codigoPostal = 3000;
        String localidad = "Santa Fe".toUpperCase();
        String provincia = "Santa Fe".toUpperCase();
        String pais = "Argentina".toUpperCase();

        Direccion direccion = new Direccion(calle, numero, departamento, piso, codigoPostal, localidad, provincia, pais);
        Huesped.Builder builder = new Huesped.Builder()
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
        return builder.build();
    }
    
/**
    public static void main(String[] args) {
       SpringApplication.run(TpBackApplication.class, args);
       if(CU01.ejecutar(scanner)){
              menuPrincipal();
         } else {
              System.out.println("\n Autenticacion fallida. Saliendo del sistema.");
       }
       scanner.close();
    }
**/    
    
    /**
     * Muestra el menú principal del sistema
     */
    private static void menuPrincipal() {
        boolean salir = false;
        
        while (!salir) {
            int opcion = leerOpcionMenu(1, 2);
            switch (opcion) {
                case 1:
                     CU02.correr(scanner);
                    break;
                case 2:
                    salir = true;
                  System.out.println("\n Cerrando sesion... Hasta pronto!");
                    break;
            }
        }
    }
    
    private static void menuGrafico() {
        try {
        // limpiar pantalla
        System.out.print("\033[H\033[2J");
        System.out.flush();
        } catch (Exception e) {
            System.err.println(e.getMessage());
        }
        
        System.out.println("\n=== MENU PRINCIPAL ===");
            System.out.println("1. Buscar huesped");
            System.out.println("2. Salir");
            System.out.print("Seleccione una opcion: ");
    }
    
    private static int leerOpcionMenu(int min, int max) {
        int opcion = -1;
        boolean entradaValida = false;
        
        while (!entradaValida) {
            menuGrafico();
            try {
                opcion = scanner.nextInt();
                scanner.nextLine(); // Limpiar el buffer
                
                if (opcion >= min && opcion <= max) {
                    entradaValida = true;
                } else {
                    System.out.print(" Opcion invalida. Ingrese un numero entre " + min + " y " + max + ": ");
                    esperarEnter();
                }
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Limpiar el buffer
                System.out.print(" Debe ingresar un numero. Intente nuevamente: ");
                esperarEnter();
            }
        }
        
        return opcion;
    }    
    
    /**
     * Espera a que el usuario presione Enter para continuar
     */
    private static void esperarEnter() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
}
