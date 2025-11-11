package servicio;

import Excepcion.HuespedNoEliminableException;
import Excepcion.HuespedNoEncontradoException;
import java.util.Scanner;
import repositorio.HuespedDTO;
import servicios.GestorDeHuesped;

public class CU11 {
    public static void run(HuespedDTO huespedExistente, Scanner in) {
        System.out.println("=== Dar Baja de Huésped ===");

        try {
            //El sistema constata si el huésped se ha alojado

            if (huespedExistente == null) {
                System.out.println("No se encontró el huésped en el sistema.");
                return;
            }

            if (huespedExistente.getAlojado()) {
                
                System.out.println("\nEl huésped no puede ser eliminado pues se ha alojado en el hotel en alguna oportunidad.");
                System.out.println("PRESIONE CUALQUIER TECLA PARA CONTINUAR...");
                in.nextLine(); // espera entrada
                return;
            }

            System.out.println("\nLos datos del huésped " + huespedExistente.getNombre() + " " + huespedExistente.getApellido() +
                    " (" + huespedExistente.getTipoDocumento() + " " + huespedExistente.getNumeroDocumento() + 
                    ") serán eliminados del sistema.");
            System.out.println("ELIMINAR / CANCELAR");

            String opcion = in.nextLine().trim().toUpperCase();

            if (opcion.equals("ELIMINAR")) {
                GestorDeHuesped.eliminarHuesped(huespedExistente);
                System.out.println("Los datos del huésped " + huespedExistente.getNombre() + " " + huespedExistente.getApellido() +
                        " (" + huespedExistente.getTipoDocumento() + " " + huespedExistente.getNumeroDocumento() + 
                        ") han sido eliminados del sistema.");
                System.out.println("PRESIONE CUALQUIER TECLA PARA CONTINUAR...");
                in.nextLine();
                return;
            } else if (opcion.equals("CANCELAR")) {

                System.out.println("Operación cancelada.");
                return;
            } else {
                System.out.println("Opción inválida.");
                return;
            }

        } catch (HuespedNoEncontradoException | HuespedNoEliminableException e) {
            System.out.println(e.getMessage());
            
        } finally {
            in.close();
           
        }
        System.out.println("=============================");
        return;
    }
}
