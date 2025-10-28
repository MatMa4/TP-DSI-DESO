package servicio;

import java.util.Scanner;
import repositorio.HuespedDTO;
import servicios.GestorDeHuesped;
import Excepcion.HuespedNoEliminableException;
import Excepcion.HuespedNoEncontradoException;

/**
 * Caso de Uso 11 - Dar baja de huésped
 * Autor: Señor Wayne
 */
public class CU11 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        GestorDeHuesped gestor = new GestorDeHuesped();

        System.out.println("=== CASO DE USO 11: Dar Baja de Huésped ===");

        System.out.print("Tipo de documento (DNI, LE, LC, Pasaporte, Otro): ");
        String tipoDocumento = in.nextLine().trim().toUpperCase();

        System.out.print("Número de documento: ");
        String numeroDocumento = in.nextLine().trim();

        try {
            // Paso 1: Buscar el huésped
            HuespedDTO huespedDTO;
            huespedDTO = gestor.consultarDocumento(tipoDocumento, numeroDocumento);

            System.out.println("\nSe encontró al huésped: " 
                + huespedDTO.getNombre() + " " + huespedDTO.getApellido());

            if (huespedDTO.getAlojado()) {
                // Caso alternativo: el huésped ya se alojó anteriormente
                System.out.println("❌ Este huésped no se puede borrar (ya se alojó anteriormente).");
                return;
            }

            // Caso principal: huésped no alojado
            System.out.println("⚠️  Este huésped será eliminado del sistema.");
            System.out.print("¿Desea continuar? (S/N): ");
            String respuesta = in.nextLine().trim().toUpperCase();

            if (respuesta.equals("S")) {
                gestor.eliminarHuesped(huespedDTO);
            } else {
                System.out.println("Operación cancelada por el usuario.");
            }

        } catch (HuespedNoEncontradoException e) {
            System.out.println("️ No existe un huésped con ese documento.");
        } catch (HuespedNoEliminableException e) {
            System.out.println(" No se puede eliminar el huésped: ya se ha alojado anteriormente.");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            in.close();
        }

        System.out.println("=== Fin del Caso de Uso 11 ===");
    }
}
