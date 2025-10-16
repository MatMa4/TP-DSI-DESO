/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package deso.tp;

import DAOs.UsuarioDAOImpl;
import Excepcion.ContrasenaInvalidaException;
import Excepcion.UsuarioNoEncontradoException;
import java.io.IOException;
import repositorio.UsuarioDTO;
import servicios.GestorDeUsuario;
import java.util.InputMismatchException;
import java.util.Scanner;

/**
 *
 * @author mateo
 */
public class TP {
    private static Scanner scanner = new Scanner(System.in);
    private static GestorDeUsuario gestor;
    
    public static void main(String[] args) {
        System.out.println("=== SISTEMA DE GESTION HOTELERA ===\n");
        
        // Inicializar el gestor de usuarios
        gestor = new GestorDeUsuario(new UsuarioDAOImpl("src/main/java/BDD/infoUsers.json"));
        
        // Intentar autenticar al usuario
        boolean autenticado = false;
        int intentos = 0;
        int maxIntentos = 3;
        
        while (!autenticado && intentos < maxIntentos) {
            try {
                autenticado = menuLogin();
            } catch (UsuarioNoEncontradoException e) {
                intentos++;
                System.out.println("❌ " + e.getMessage());
                System.out.println("Intentos restantes: " + (maxIntentos - intentos) + "\n");
            } catch (ContrasenaInvalidaException e) {
                intentos++;
                System.out.println("❌ " + e.getMessage());
                System.out.println("Intentos restantes: " + (maxIntentos - intentos) + "\n");
            } catch (Exception e) {
                System.out.println("❌ Error inesperado: " + e.getMessage() + "\n");
                intentos++;
            }
        }
        
        if (!autenticado) {
            System.out.println("❌ Numero maximo de intentos alcanzado. El sistema se cerrara.");
            scanner.close();
            return;
        }
        
        // Si se autenticó correctamente, mostrar menú principal
        menuPrincipal();
        
        scanner.close();
    }
    
    /**
     * Muestra el menú de login y solicita credenciales
     * @return true si la autenticación fue exitosa
     * @throws UsuarioNoEncontradoException si el usuario no existe
     * @throws ContrasenaInvalidaException si la contraseña es incorrecta
     */
    private static boolean menuLogin() throws UsuarioNoEncontradoException, ContrasenaInvalidaException {
        System.out.println("--- LOGIN ---");
        
        String username = leerTextoNoVacio("Ingrese su usuario: ");
        String password = leerTextoNoVacio("Ingrese su contrasenia: ");
        
        // Crear DTO con las credenciales
        UsuarioDTO userDto = new UsuarioDTO(username, password);
        
        // Intentar autenticar
        gestor.autenticarUsuario(userDto);
        
        System.out.println("✅ Autenticacion exitosa!\n");
        esperarEnter();
        return true;
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
    
    /**
     * Lee una opción del menú validando que sea un número dentro del rango
     * @param min valor mínimo permitido
     * @param max valor máximo permitido
     * @return la opción válida seleccionada
     */
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
     * Lee un texto no vacío de la consola
     * @param mensaje el mensaje a mostrar al usuario
     * @return el texto ingresado (no vacío)
     */
    private static String leerTextoNoVacio(String mensaje) {
        String texto = "";
        boolean entradaValida = false;
        
        while (!entradaValida) {
            System.out.print(mensaje);
            texto = scanner.nextLine().trim();
            
            if (!texto.isEmpty()) {
                entradaValida = true;
            } else {
                System.out.println("❌ El campo no puede estar vacio. Intente nuevamente.");
            }
        }
        
        return texto;
    }
    
    /**
     * Espera a que el usuario presione Enter para continuar
     */
    private static void esperarEnter() {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
}
