/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package servicio;

/**
 *
 * @author Lucas
 */
import java.util.List;
import java.util.Scanner;
import dominio.Huesped;
import repositorio.HuespedDTO;
import DAOs.HuespedDAO;
import DAOs.HuespedDAOImpl;
import Excepcion.HuespedNoEncontradoException;

public class CU02 {

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        HuespedDAO huespedDAO = HuespedDAOImpl.getHuespedDAO();

        System.out.println("=== BÚSQUEDA DE HUÉSPED ===");
        System.out.print("Ingrese apellido (o presione Enter para omitir): ");
        String apellido = in.nextLine();

        System.out.print("Ingrese nombre (o presione Enter para omitir): ");
        String nombre = in.nextLine();

        System.out.print("Ingrese tipo de documento (DNI, LE, LC, Pasaporte, Otro) o presione Enter para omitir: ");
        String tipoDoc = in.nextLine();

        System.out.print("Ingrese número de documento (o presione Enter para omitir): ");
        String nroDoc = in.nextLine();

        try {
            // Buscar huéspedes según los filtros
            List<HuespedDTO> resultado = huespedDAO.buscarHuesped(apellido, nombre, tipoDoc, nroDoc);

            if (!resultado.isEmpty()) {
                System.out.println("\n=== RESULTADOS ENCONTRADOS ===");
                System.out.printf("%-5s %-15s %-15s %-12s %-12s%n", "N°", "Apellido", "Nombre", "Tipo Doc", "Nro Doc");
                System.out.println("-------------------------------------------------------------");

                int i = 1;
                for (HuespedDTO h : resultado) {
                    System.out.printf("%-5d %-15s %-15s %-12s %-12s%n",
                            i++, h.getApellido(), h.getNombre(),
                            h.getTipoDocumento(), h.getNumeroDocumento());
                }

                //  Selección del huésped
                System.out.print("\nSeleccione el número del huésped que desea (o presione Enter para ninguno): ");
                String seleccion = in.nextLine();

                if (seleccion.isEmpty()) {
                    System.out.println("\nNo se seleccionó ningún huésped.");
                    System.out.println("→ Redirigiendo al CU11: Dar alta de huésped...");
                    // new CU11().ejecutar();
                    return;
                }

                int indiceSeleccionado;
                try {
                    indiceSeleccionado = Integer.parseInt(seleccion);
                } catch (NumberFormatException e) {
                    System.out.println("Entrada inválida. Debe ingresar un número.");
                    return;
                }

                if (indiceSeleccionado < 1 || indiceSeleccionado > resultado.size()) {
                    System.out.println("Número fuera de rango. Operación cancelada.");
                    return;
                }

                HuespedDTO huespedSeleccionado = resultado.get(indiceSeleccionado - 1);
                System.out.println("\nHa seleccionado a:");
                System.out.println(huespedSeleccionado.getNombre() + " " + huespedSeleccionado.getApellido());

                // Esperar confirmación con "SIGUIENTE"
                System.out.print("\nEscriba 'SIGUIENTE' para continuar: ");
                String siguiente = in.nextLine();

                if ("SIGUIENTE".equalsIgnoreCase(siguiente)) {
                    System.out.println("→ Pasando al CU10: Modificar Huésped...");
                    // new CU10().ejecutar(huespedSeleccionado);
                } else {
                    System.out.println("Operación cancelada.");
                }

            }

        } catch (HuespedNoEncontradoException e) {
            // 🔁 Si no hay coincidencias → CU11 (Dar alta de huésped)
            System.out.println(e.getMessage());
            System.out.println("→ Redirigiendo al CU11: Dar alta de huésped...");
            // new CU11().ejecutar();
        }
    }
}