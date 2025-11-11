/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package TrabajoPractico.TPBack;

import servicio.CU01;
import servicio.CU02;
import java.util.InputMismatchException;
import java.util.Scanner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 *
 * @author mateo
 */
@SpringBootApplication
public class TpBackApplication {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
       SpringApplication.run(TpBackApplication.class, args);
       if(CU01.ejecutar(scanner)){
              menuPrincipal();
         } else {
              System.out.println("\n Autenticacion fallida. Saliendo del sistema.");
       }
       scanner.close();
    }
    
    
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
