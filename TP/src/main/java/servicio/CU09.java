
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
public class CU09 {
    private static final List<String> TIPOS_DOC = Arrays.asList("DNI", "LC", "LE", "Pasaporte", "Otro");
    private static final List<String> POS_IVA = Arrays.asList("Responsable Inscripto", "Monotributista", "Excento");

    public static void main(String[] args) {
        Scanner in = new Scanner(System.in);
        // Recolectar datos 
        //Paso 1
        //HuespedDTO huespedNuevo = recolectarHuesped(in);
        HuespedDTO huespedNuevo = huespedPrueba(in); // método de pruba
        while (true){ //Se repite mientras se quieran cargar huéspedes
            boolean repetir = false;
            System.out.println("\nSIGUIENTE / CANCELAR");//Paso 1

            String opcionInicial;
            while (true) {//Se repite hasta que ingrese una opción válida
                opcionInicial = in.nextLine().trim();
                //Paso 2
                if (opcionInicial.equalsIgnoreCase("SIGUIENTE")){
                    break;//Si selecciona SIGIENTE, continúa el flujo principal
                } else if (opcionInicial.equalsIgnoreCase("CANCELAR")){
                    //Paso 2.C
                    System.out.println("¿Desea cancelar el alta del huésped? [S/N].");
                    String respuesta;
                    while(true) { //Repite hasta recibir una respuesta válida
                        respuesta = in.nextLine().trim(); //Lee la respuesta
                        if(respuesta.equalsIgnoreCase("S")){
                            repetir = true;//Activa una bandera para saltar el resto de la iteración y comienza otra vez
                            break;
                        } else if(respuesta.equalsIgnoreCase("N")){
                            break;//Sale del bucle y vuelve a preguntar SIGUIENTE/CANCELAR
                        } else {//Si no seleccionó bien la respuesta se debe volver a pedir
                            System.out.println("Respuesta inválida. Ingrese 'S' para sí o 'N' para no.");
                        }
                    }
                    if (repetir){//Si desea cancelar sale del bucle
                        break;
                    }
                } else {
                    //No es un paso, pero se repite hasta que seleccione una opción válida
                    System.out.println("Opción inválida. Escriba: SIGUIENTE / CANCELAR");
                }
            }

            HuespedDTO huespedAntiguo = GestorDeHuesped.consultarDocumento(huespedNuevo.getTipoDocumento(), huespedNuevo.getNumeroDocumento());
            if (huespedAntiguo == null){
                //Paso 3
                GestorDeHuesped.registrarHuesped(huespedNuevo);
                System.out.println("El huésped "+ huespedNuevo.getNombre() +" "+ huespedNuevo.getApellido() +" ha sido satisfactoriamente cargado al sistema. ¿Desea cargar otro? [S/N]");
            } else {
                //Paso 2.B
                //Se repite hasta que ingrese una opción válida
                while (true) {
                    //Paso 2.B.1
                    System.out.println("\n“¡CUIDADO! El tipo y número de documento ya existen en el sistema");
                    //Paso 2,B,2
                    System.out.println("Aceptar Igualmente / Corregir");
                    String opcion = in.nextLine().trim();

                    if (opcion.equalsIgnoreCase("Aceptar Igualmente")) {
                        //Paso 2.B.2.1
                        //Paso 3
                        GestorDeHuesped.modificarHuesped(huespedNuevo, huespedAntiguo);
                        System.out.println("El huésped "+ huespedNuevo.getNombre() +" "+ huespedNuevo.getApellido() +" ha sido satisfactoriamente cargado al sistema. ¿Desea cargar otro? [S/N]");
                        break;
                    } else if (opcion.equalsIgnoreCase("Corregir")) {
                        //Paso 2.B.2.2
                        //Paso 2
                        // Volver a pedir todos los datos
                        huespedNuevo = editarHuesped(in, huespedNuevo);
                        repetir = true;
                        break;
                    } else {
                        System.out.println("Opción inválida, escriba 'Aceptar Igualmente', 'Corregir' o 'Cancelar'.");
                    }
                }
            }
            if (!repetir){
                String respuestaFinal;
                while(true) {
                    respuestaFinal = in.nextLine().trim();
                    if(respuestaFinal.equalsIgnoreCase("S")){
                        huespedNuevo = huespedPrueba(in);
                        break;
                    } else if(respuestaFinal.equalsIgnoreCase("N")){
                        System.out.println("Operación finalizada. Gracias.");
                        return;
                    } else {
                        System.out.println("Respuesta inválida. Ingrese 'S' para sí o 'N' para no.");
                    }
                }
            }

        // No cerramos 'in' para no cerrar System.in si otras partes lo usan
        }
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

    private static HuespedDTO editarHuesped(Scanner in, HuespedDTO original) {
        String apellido = leerTexto(in, "Apellido [" + original.getApellido() + "] : ", true);
        String nombre = leerTexto(in, "Nombre [" + original.getNombre() + "]: ", true);
        String tipoDocumento = modificarTipoDocumento(in, original.getTipoDocumento());
        String numeroDocumento = modificarNumeroDocumento(in, tipoDocumento, original.getNumeroDocumento());
        String cuit = leerSoloNumeros(in, "CUIT (no obligatorio) ["+ original.getCuit() +"]: ", false);
        String posicionIVA = modificarPosicionIVA(in, original.getPosicionIVA());
        LocalDate fechaNacimiento = leerFecha(in, "Fecha de nacimiento (YYYY-MM-DD) ["+ original.getFechaNacimiento() +"]: ");
        String telefono = leerTexto(in, "Teléfono ["+ original.getTelefono() +"]: ", true, "[0-9+ ]+");
        String email = leerEmail(in, "Email (no obligatorio) ["+ original.getEmail() +"]: ", false);
        String ocupacion = leerTexto(in, "Ocupación ["+ original.getOcupacion() +"]: ", true);
        String nacionalidad = leerTexto(in, "Nacionalidad ["+ original.getNacionalidad() +"]: ", true);
        String calle = leerTexto(in, "Dirección - Calle ["+ original.getDireccionHuesped().getCalle() +"]: ", true);
        String numero = leerSoloNumeros(in, "Dirección - Número ["+ original.getDireccionHuesped().getNumero() +"]: ", true);
        String departamento = leerTexto(in, "Dirección - Departamento ["+ original.getDireccionHuesped().getDepartamento() +"]: ", false);
        int piso = leerEntero(in, "Dirección - Piso ["+ original.getDireccionHuesped().getPiso() +"]: ");
        int codigoPostal = leerEntero(in, "Dirección - Código postal ["+ original.getDireccionHuesped().getCodigo() +"]: ");
        String localidad = leerTexto(in, "Dirección - Localidad ["+ original.getDireccionHuesped().getLocalidad() +"]: ", true);
        String provincia = leerTexto(in, "Dirección - Provincia ["+ original.getDireccionHuesped().getProvincia() +"]: ", true);
        String pais = leerTexto(in, "Dirección - País ["+ original.getDireccionHuesped().getPais() +"]: ", true);

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
                    System.out.println("Campo obligatorio, vuelva a ingresarlo.");
                    continue;
                } else {
                    return null;
                }
            }
            if (!valor.matches(regex)) {
                System.out.println("Formato inválido, solo se permiten letras/espacios.");
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
                    System.out.println("Campo obligatorio, vuelva a ingresarlo.");
                    continue;
                } else {
                    return null;
                }
            }
            if (!valor.matches("[0-9]+")) {
                System.out.println("Solo se permiten números.");
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
                System.out.println("Debe ingresar un número entero.");
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
                System.out.println("Formato inválido, use YYYY-MM-DD.");
            }
        }
    }

    private static String leerTipoDocumento(Scanner in) {
        while (true) {
            System.out.print("Tipo de documento [DNI, LE, LC, Pasaporte, Otro]: ");
            String valor = in.nextLine().trim();
            if (TIPOS_DOC.contains(valor)) return valor;
            System.out.println("Tipo inválido. Opciones: " + TIPOS_DOC);
        }
    }

    private static String leerNumeroDocumento(Scanner in, String tipoDoc) {
        while (true) {
            System.out.print("Número de documento: ");
            String valor = in.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.println("Campo obligatorio.");
                continue;
            }
            if (tipoDoc.equals("LE") || tipoDoc.equals("LC") || tipoDoc.equals("Otro")) {
                if (valor.matches("[a-zA-Z]{1}[0-9]+")) return valor;
                System.out.println("Debe comenzar con una letra seguida de números.");
            } else {
                if (valor.matches("[0-9]+")) return valor;
                System.out.println("Solo se permiten números.");
            }
        }
    }

    private static String leerPosicionIVA(Scanner in) {
        while (true) {
            System.out.print("Posición frente al IVA (Consumidor final por omisión): ");
            String valor = in.nextLine().trim();
            if (valor.isEmpty()) return "Consumidor final";
            if (POS_IVA.contains(valor)) return valor;
            System.out.println("Valor inválido. Opciones: " + POS_IVA + " o vacío (Consumidor final).");
        }
    }

    private static String leerEmail(Scanner in, String prompt, boolean obligatorio) {
        while (true) {
            System.out.print(prompt);
            String valor = in.nextLine().trim();

            if (valor.isEmpty()) {
                if (obligatorio) {
                    System.out.println("Campo obligatorio, vuelva a ingresarlo.");
                    continue;
                } else {
                    return null; // permitido vacío
                }
            }

            // Validación: exactamente un @ y no al inicio ni al final
            if (valor.matches("^[^@\\s]+@[^@\\s]+$")) {
                return valor;
            } else {
                System.out.println("El email debe contener exactamente un '@' y no puede estar vacío antes o después de él.");
            }
        }
    }

    private static String modificarTipoDocumento(Scanner in, String tipoActual) {
        while (true) {
            System.out.print("Tipo de documento [DNI, LE, LC, Pasaporte, Otro]: \n [Actual: " + tipoActual + "] : ");
            String valor = in.nextLine().trim();
            if (TIPOS_DOC.contains(valor)) return valor;
            System.out.println("Tipo inválido. Opciones: " + TIPOS_DOC);
        }
    }

    private static String modificarNumeroDocumento(Scanner in, String tipoDoc, String numeroActual) {
        while (true) {
            System.out.print("Número de documento ["+numeroActual+"]: ");
            String valor = in.nextLine().trim();
            if (valor.isEmpty()) {
                System.out.println("Campo obligatorio.");
                continue;
            }
            if (tipoDoc.equals("LE") || tipoDoc.equals("LC") || tipoDoc.equals("Otro")) {
                if (valor.matches("[a-zA-Z]{1}[0-9]+")) return valor;
                System.out.println("Debe comenzar con una letra seguida de números.");
            } else {
                if (valor.matches("[0-9]+")) return valor;
                System.out.println("Solo se permiten números.");
            }
        }
    }

    private static String modificarPosicionIVA(Scanner in, String posicionActual) {
        while (true) {
            System.out.print("Posición frente al IVA (Consumidor final por omisión) ["+ posicionActual +"]: ");
            String valor = in.nextLine().trim();
            if (valor.isEmpty()) return "Consumidor final";
            if (POS_IVA.contains(valor)) return valor;
            System.out.println("Valor inválido. Opciones: " + POS_IVA + " o vacío (Consumidor final).");
        }
    }

    private static HuespedDTO huespedPrueba(Scanner in) {
        String apellido = "apellidoPrueba";
        String nombre = "nombrePrueba";
        String tipoDocumento = "DNI";
        String numeroDocumento = "12345678";
        String cuit = "2012312443";
        String posicionIVA = "Responsable Inscripto";
        LocalDate fechaNacimiento = LocalDate.parse("2004-05-20");
        String telefono = "3421234567";
        String email = "e@mail.com";
        String ocupacion = "estudiante";
        String nacionalidad = "argentino";
        String calle = "callePrueba";
        String numero = "1234";
        String departamento = "A";
        int piso = 2;
        int codigoPostal = 3000;
        String localidad = "Santa Fe";
        String provincia = "Santa Fe";
        String pais = "Argentina";

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
}
