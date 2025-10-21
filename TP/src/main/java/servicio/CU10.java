/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicio;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;


import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import repositorio.DireccionDTO;
import repositorio.HuespedDTO;
import servicios.GestorDeHuesped;


/**
 *
 * @author mateo
 */
public class CU10 {
    private static final List<String> TIPOS_DOC = Arrays.asList("DNI", "LC", "LE", "Pasaporte", "Otro");
    private static final List<String> POS_IVA = Arrays.asList("Responsable Inscripto", "Monotributista", "Excento");

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);

        // Recolectar datos en un método reutilizable
        HuespedDTO huespedNuevo = recolectarHuesped(in);

        System.out.println("\nDatos cargados correctamente.");

        HuespedDTO huespedAntiguo = GestorDeHuesped.consultarDocumento(huespedNuevo.getTipoDocumento(), huespedNuevo.getNumeroDocumento());
        if (huespedAntiguo == null){
            GestorDeHuesped.registrarHuesped(huespedNuevo);
            System.out.println("Huésped registrado exitosamente.");
        } else {
            System.out.println("\n“¡CUIDADO! El tipo y número de documento ya existen en el sistema");
            while (true) {
                System.out.println("Aceptar Igualmente / Corregir");
                String opcion = in.nextLine().trim();

                if (opcion.equalsIgnoreCase("Aceptar Igualmente")) {
                    GestorDeHuesped.modificarHuesped(huespedNuevo, huespedAntiguo);
                    System.out.println("Huésped modificado exitosamente.");
                    break;
                } else if (opcion.equalsIgnoreCase("Modificar") || opcion.equalsIgnoreCase("Corregir")) {
                    // Volver a pedir todos los datos reutilizando el mismo Scanner
                    huespedNuevo = recolectarHuesped(in);
                    // Revalidar si hay conflicto con el documento nuevo
                    huespedAntiguo = GestorDeHuesped.consultarDocumento(huespedNuevo.getTipoDocumento(), huespedNuevo.getNumeroDocumento());
                    if (huespedAntiguo == null) {
                        GestorDeHuesped.registrarHuesped(huespedNuevo);
                        System.out.println("Huésped registrado exitosamente.");
                        break;
                    } else {
                        System.out.println("Sigue existiendo un huésped con esos datos. Elija otra opción.");
                        // el loop continúa: puede aceptar igualmente o corregir nuevamente
                    }
                } else {
                    System.out.println("Opción inválida, escriba 'Aceptar Igualmente' o 'Corregir'.");
                }
            }
        }

        // No cerramos 'in' para no cerrar System.in si otras partes lo usan
    }

    // Nuevo método que agrupa toda la recolección y construcción del DTO
    private static HuespedDTO recolectarHuesped(Scanner in) {
        String apellido = leerTexto(in, "Apellido: ", true);
        String nombre = leerTexto(in, "Nombre: ", true);
        String tipoDocumento = leerTipoDocumento(in);
        String numeroDocumento = leerNumeroDocumento(in, tipoDocumento);
        String cuit = leerSoloNumeros(in, "CUIT (no obligatorio): ", false);
        String posicionIVA = leerPosicionIVA(in);
        LocalDate fechaNacimiento = leerFecha(in, "Fecha de nacimiento (YYYY-MM-DD): ");
        String telefono = leerTexto(in, "Teléfono: ", true, "[0-9+ ]+");
        String email = leerEmail(in, "Email (no obligatorio): ", false);
        String ocupacion = leerTexto(in, "Ocupación: ", true);
        String nacionalidad = leerTexto(in, "Nacionalidad: ", true);
        String calle = leerTexto(in, "Dirección - Calle: ", true);
        String numero = leerSoloNumeros(in, "Dirección - Número: ", true);
        String departamento = leerTexto(in, "Dirección - Departamento: ", false);
        int piso = leerEntero(in, "Dirección - Piso: ");
        int codigoPostal = leerEntero(in, "Dirección - Código postal: ");
        String localidad = leerTexto(in, "Dirección - Localidad: ", true);
        String provincia = leerTexto(in, "Dirección - Provincia: ", true);
        String pais = leerTexto(in, "Dirección - País: ", true);

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
        return builder.build();
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
        
    private static String leerTexto(Scanner in, String prompt, boolean obligatorio) {
        return leerTexto(in, prompt, obligatorio, "[a-zA-ZáéíóúÁÉÍÓÚñÑ ]+");
    }

    private static String leerTexto(Scanner in, String prompt, boolean obligatorio, String regex) {
        while (true) {
            System.out.print(prompt);
            String valor = in.nextLine().trim();
            if (valor.isEmpty()) {
                if (obligatorio) {
                    System.out.println("⚠️ Campo obligatorio, vuelva a ingresarlo.");
                    continue;
                } else {
                    return null;
                }
            }
            if (!valor.matches(regex)) {
                System.out.println("⚠️ Formato inválido, solo se permiten letras/espacios.");
                continue;
            }
            return valor;
        }
    }

    private static String leerSoloNumeros(Scanner in, String prompt, boolean obligatorio) {
        while (true) {
            System.out.print(prompt);
            String valor = in.nextLine().trim();
            if (valor.isEmpty()) {
                if (obligatorio) {
                    System.out.println("⚠️ Campo obligatorio, vuelva a ingresarlo.");
                    continue;
                } else {
                    return null;
                }
            }
            if (!valor.matches("[0-9]+")) {
                System.out.println("⚠️ Solo se permiten números.");
                continue;
            }
            return valor;
        }
    }

    private static int leerEntero(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String valor = in.nextLine().trim();
            try {
                return Integer.parseInt(valor);
            } catch (NumberFormatException e) {
                System.out.println("⚠️ Debe ingresar un número entero.");
            }
        }
    }

    private static LocalDate leerFecha(Scanner in, String prompt) {
        while (true) {
            System.out.print(prompt);
            String valor = in.nextLine().trim();
            try {
                return LocalDate.parse(valor);
            } catch (DateTimeParseException e) {
                System.out.println("⚠️ Formato inválido, use YYYY-MM-DD.");
            }
        }
    }

    private static String leerTipoDocumento(Scanner in) {
        while (true) {
            System.out.print("Tipo de documento [DNI, LE, LC, Pasaporte, Otro]: ");
            String valor = in.nextLine().trim();
            if (TIPOS_DOC.contains(valor)) return valor;
            System.out.println("⚠️ Tipo inválido. Opciones: " + TIPOS_DOC);
        }
    }

    private static String leerNumeroDocumento(Scanner in, String tipoDoc) {
        while (true) {
            System.out.print("Número de documento: ");
            String valor = in.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.println("⚠️ Campo obligatorio.");
                continue;
            }
            if (tipoDoc.equals("LE") || tipoDoc.equals("LC") || tipoDoc.equals("Otro")) {
                if (valor.matches("[a-zA-Z]{1}[0-9]+")) return valor;
                System.out.println("⚠️ Debe comenzar con una letra seguida de números.");
            } else {
                if (valor.matches("[0-9]+")) return valor;
                System.out.println("⚠️ Solo se permiten números.");
            }
        }
    }

    private static String leerPosicionIVA(Scanner in) {
        while (true) {
            System.out.print("Posición frente al IVA (Consumidor final por omisión): ");
            String valor = in.nextLine().trim();
            if (valor.isEmpty()) return "Consumidor final";
            if (POS_IVA.contains(valor)) return valor;
            System.out.println("⚠️ Valor inválido. Opciones: " + POS_IVA + " o vacío (Consumidor final).");
        }
    }

    private static String leerEmail(Scanner in, String prompt, boolean obligatorio) {
        while (true) {
            System.out.print(prompt);
            String valor = in.nextLine().trim();

            if (valor.isEmpty()) {
                if (obligatorio) {
                    System.out.println("⚠️ Campo obligatorio, vuelva a ingresarlo.");
                    continue;
                } else {
                    return null; // permitido vacío
                }
            }

            // Validación: exactamente un @ y no al inicio ni al final
            if (valor.matches("^[^@\\s]+@[^@\\s]+$")) {
                return valor;
            } else {
                System.out.println("⚠️ El email debe contener exactamente un '@' y no puede estar vacío antes o después de él.");
            }
        }
    }

}
