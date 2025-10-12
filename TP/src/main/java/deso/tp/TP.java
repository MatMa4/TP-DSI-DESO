/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package deso.tp;
import java.time.LocalDate;

import DAOs.HuespedDAO;
import DAOs.HuespedDAOImpl;
import dominio.Huesped;
import dominio.Direccion;
import repositorio.HuespedDTO;
import DAOs.UsuarioDAOImpl;
import Excepcion.ContrasenaInvalidaException;
import Excepcion.UsuarioNoEncontradoException;
import repositorio.UsuarioDTO;
import servicios.GestorDeUsuario;

/**
 *
 * @author mateo
 */
public class TP {

    public static void main(String[] args) {
        HuespedDAOImpl huespedDAO= HuespedDAOImpl.getHuespedDAO();
        Direccion direccion = new Direccion("Calle Falsa", "123", "D", 23, 62704, "Springfield", "Illinois", "USA");
        Huesped huesped = new Huesped.Builder()
            .apellido("Gómez")
            .nombre("Mateo")
            .tipoDocumento("DNI")
            .numeroDocumento("12345678")
            .fechaNacimiento(LocalDate.of(1990, 5, 20))
            .direccionHuesped(direccion)
            .telefono("3421234567")
            .ocupacion("Ingeniero")
            .nacionalidad("Argentina")
            .email("mateo.gomez@example.com") // opcional
            .cuit("20-12345678-9")            // opcional
            .posicionIVA("Responsable Inscripto") // opcional, si no se pone se asigna "Consumidor Final"
            .build();


        huespedDAO.guardarHuesped(huesped);
        HuespedDTO dto = huespedDAO.consultarDocumento("DNI", "12345678");
        System.out.println("Huésped encontrado: " + dto.getNombre() + " " + dto.getApellido());
        System.out.println("Hola");
        
        GestorDeUsuario gestor = new GestorDeUsuario(new UsuarioDAOImpl("src/main/java/BDD/info.csv"));
        UsuarioDTO userDto = new UsuarioDTO("juan","1234");
        gestor.autenticarUsuario(userDto);
        System.out.print("se pudo");

    }
}
