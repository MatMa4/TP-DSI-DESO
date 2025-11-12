package servicio;

import java.util.Scanner;

import DAOs.UsuarioDAOImpl;
import Excepcion.ContrasenaInvalidaException;
import Excepcion.UsuarioNoEncontradoException;
import repositorio.UsuarioDTO;
import servicios.GestorDeUsuario;

public class CU01 {
    private static GestorDeUsuario gestor;

    public static boolean ejecutar(Scanner scanner) {

        System.out.println("=== SISTEMA DE GESTION HOTELERA ===\n");
        
        // Inicializar el gestor de usuarios
        gestor = new GestorDeUsuario(new UsuarioDAOImpl("TrabajoPractico/src/main/java/BDD/infoUsers.json"));

        // Intentar autenticar al usuario
        boolean autenticado = false;
        int intentos = 0;
        int maxIntentos = 3;
        
        while (!autenticado && intentos < maxIntentos) {
            try {
                autenticado = menuLogin(scanner);
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
            return false;
        }
        return true;
    }
    private static boolean menuLogin(Scanner scanner) throws UsuarioNoEncontradoException, ContrasenaInvalidaException {
        System.out.println("--- LOGIN ---");
        
        String username = leerTextoNoVacio("Ingrese su usuario: ", scanner);
        String password = leerTextoNoVacio("Ingrese su contrasenia: ", scanner);

        // Crear DTO con las credenciales
        UsuarioDTO userDto = new UsuarioDTO(username, password);
        
        // Intentar autenticar
        gestor.autenticarUsuario(userDto);
        
        System.out.println("✅ Autenticacion exitosa!\n");
        esperarEnter(scanner);
        return true;
    }
    private static String leerTextoNoVacio(String mensaje, Scanner scanner) {
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
    private static void esperarEnter(Scanner scanner) {
        System.out.print("\nPresione Enter para continuar...");
        scanner.nextLine();
    }
}
