package servicio;

import Excepcion.HuespedNoEliminableException;
import Excepcion.HuespedNoEncontradoException;
import java.util.Scanner;
import repositorio.HuespedDTO;
import servicios.GestorDeHuesped;

public class CU11 {
    public static void main(String[] args) { //HuesedDTO
        Scanner in = new Scanner(System.in);
        //HuespedDTO huespedNuevo = recolectarHuesped(in);
        System.out.println("=== Dar Baja de Huésped ===");

        // Paso 1: El actor desea eliminar un huésped
        System.out.print("Tipo de documento (DNI, LE, LC, Pasaporte, Otro): ");
        String tipoDocumento = in.nextLine().trim().toUpperCase();

        System.out.print("Número de documento: ");
        String numeroDocumento = in.nextLine().trim();

        try {
            // Paso 2: El sistema constata si el huésped se ha alojado
            HuespedDTO huespedDTO = GestorDeHuesped.consultarDocumento(tipoDocumento, numeroDocumento);

            if (huespedDTO == null) {
                System.out.println("No se encontró el huésped en el sistema.");
                return;
            }

            if (huespedDTO.getAlojado()) {
                // --- Flujo Alternativo 2.A ---
                System.out.println("\nEl huésped no puede ser eliminado pues se ha alojado en el hotel en alguna oportunidad.");
                System.out.println("PRESIONE CUALQUIER TECLA PARA CONTINUAR...");
                in.nextLine(); // espera entrada
                return;
            }

            System.out.println("\nLos datos del huésped " + huespedDTO.getNombre() + " " + huespedDTO.getApellido() +
                    " (" + huespedDTO.getTipoDocumento() + " " + huespedDTO.getNumeroDocumento() + 
                    ") serán eliminados del sistema.");
            System.out.println("ELIMINAR / CANCELAR");

            String opcion = in.nextLine().trim().toUpperCase();

            if (opcion.equals("ELIMINAR")) {
                GestorDeHuesped.eliminarHuesped(huespedDTO);
                System.out.println("Los datos del huésped " + huespedDTO.getNombre() + " " + huespedDTO.getApellido() +
                        " (" + huespedDTO.getTipoDocumento() + " " + huespedDTO.getNumeroDocumento() + 
                        ") han sido eliminados del sistema.");
                System.out.println("PRESIONE CUALQUIER TECLA PARA CONTINUAR...");
                in.nextLine();
            } else if (opcion.equals("CANCELAR")) {

                System.out.println("Operación cancelada.");
            } else {
                System.out.println("Opción inválida.");
            }

        } catch (HuespedNoEncontradoException | HuespedNoEliminableException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Error inesperado: " + e.getMessage());
        } finally {
            in.close();
        }

        System.out.println("=============================");
    }
}
