/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package deso.tp;

import servicio.CU01;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author mateo
 */
public class TP {
    private static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
         
       if(CU01.ejecutar(scanner)){
              menuPrincipal();
         } else {
              System.out.println("\n❌ Autenticacion fallida. Saliendo del sistema.");
       }
       scanner.close();
    }
    
    
    /**
     * Muestra el menú principal del sistema
     */
    private static void menuPrincipal() {
        boolean salir = false;
        
        while (!salir) {
            
            int opcion = leerOpcionMenu(1, 4);
            
            switch (opcion) {
                case 1:
                    buscarHuesped();
                    break;
                case 2:
                    modificarHuesped();
                    break;
                case 3:
                    eliminarHuesped();
                    break;
                case 4:
                    salir = true;
                    System.out.println("\n👋 Cerrando sesion... Hasta pronto!");
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
            System.out.println("2. Modificar huesped");
            System.out.println("3. Eliminar huesped");
            System.out.println("4. Salir");
            System.out.print("Seleccione una opcion: ");
    }
    
    /**
     * Funcionalidad en desarrollo: Buscar huésped
     */
    private static void buscarHuesped() {
        System.out.println("\n🔍 [BUSCAR HUESPED] - Funcionalidad en desarrollo");
        esperarEnter();
    }
    
    /**
     * Funcionalidad en desarrollo: Modificar huésped
     */
    private static void modificarHuesped() {
        System.out.println("\n✏️ [MODIFICAR HUESPED] - Funcionalidad en desarrollo");
        esperarEnter();
    }
    
    /**
     * Funcionalidad en desarrollo: Eliminar huésped
     */
    private static void eliminarHuesped() {
        System.out.println("\n🗑️ [ELIMINAR HUESPED] - Funcionalidad en desarrollo");
        esperarEnter();
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
                    System.out.print("❌ Opcio4n invalida. Ingrese un numero entre " + min + " y " + max + ": ");
                    esperarEnter();
                }
            } catch (InputMismatchException e) {
                scanner.nextLine(); // Limpiar el buffer
                System.out.print("❌ Debe ingresar un numero. Intente nuevamente: ");
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
