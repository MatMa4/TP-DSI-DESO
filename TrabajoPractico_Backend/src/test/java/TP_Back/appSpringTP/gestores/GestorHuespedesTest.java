/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.DireccionDAO;
import TP_Back.appSpringTP.DAOs.DireccionDAOImpl;
import TP_Back.appSpringTP.DAOs.HuespedDAO;
import TP_Back.appSpringTP.DAOs.HuespedDAOImpl;
import TP_Back.appSpringTP.DTOs.DireccionDTO;
import TP_Back.appSpringTP.DTOs.HuespedDTO;
import TP_Back.appSpringTP.modelo.huesped.Huesped;
import TP_Back.appSpringTP.modelo.direccion.Direccion;
import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

/**
 *
 * @author mateo
 */
@ExtendWith(MockitoExtension.class)
public class GestorHuespedesTest {
    
    public GestorHuespedesTest() {
    }
    
    @BeforeAll
    public static void setUpClass() {
    }
    
    @AfterAll
    public static void tearDownClass() {
    }
    
    @BeforeEach
    public void setUp() {
    }
    
    @AfterEach
    public void tearDown() {
    }

    /**
     * Test of registrarHuesped method, of class GestorHuespedes.
     */
    
    @Mock
    private HuespedDAOImpl huespedDAO;

    @Mock
    private DireccionDAOImpl direccionDAO;

    @InjectMocks
    private GestorHuespedes gestorHuespedes;

    @Test
    public void testRegistrarHuesped() {
        // Arrange: DTO de entrada
        DireccionDTO dirDto = new DireccionDTO();
        dirDto.setCalle("Av Corrientes");
        dirDto.setNumero(1234);
        dirDto.setLocalidad("CABA");
        dirDto.setProvincia("Buenos Aires");
        dirDto.setPais("Argentina");

        HuespedDTO dto = HuespedDTO.builder()
                .nombre("Carlos Alberto")
                .apellido("Gomez")
                .tipoDocumento("DNI")
                .numeroDocumento("35123457")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .telefono("3412345678")
                .ocupacion("Ingeniero")
                .nacionalidad("Argentina")
                .alojado(true)
                .direccion(dirDto)
                .build();

        // Mock: simulamos lo que devuelve huespedDAO.save(...)
        Huesped huespedGuardado = new Huesped();
        huespedGuardado.setNombre(dto.getNombre());
        huespedGuardado.setApellido(dto.getApellido());
        huespedGuardado.setTipoDocumento(dto.getTipoDocumento());
        huespedGuardado.setNumeroDocumento(dto.getNumeroDocumento());
        huespedGuardado.setFechaNacimiento(dto.getFechaNacimiento());
        huespedGuardado.setTelefono(dto.getTelefono());
        huespedGuardado.setOcupacion(dto.getOcupacion());
        huespedGuardado.setNacionalidad(dto.getNacionalidad());
        huespedGuardado.setAlojado(dto.getAlojado());

        Direccion direccionGuardada = new Direccion();
        direccionGuardada.setCalle(dirDto.getCalle());
        direccionGuardada.setNumero(dirDto.getNumero());
        direccionGuardada.setLocalidad(dirDto.getLocalidad());
        direccionGuardada.setProvincia(dirDto.getProvincia());
        direccionGuardada.setPais(dirDto.getPais());
        huespedGuardado.setDireccionHuesped(direccionGuardada);

        when(huespedDAO.save(any(Huesped.class))).thenReturn(huespedGuardado);

        // Act
        HuespedDTO resultado = gestorHuespedes.registrarHuesped(dto);

        // Assert: verificamos que se invocaron los DAOs
        verify(direccionDAO).save(any(Direccion.class));
        verify(huespedDAO).save(any(Huesped.class));

        // Assert: verificamos que el DTO retornado tiene los datos esperados
        assertEquals("Carlos Alberto", resultado.getNombre());
        assertEquals("Gomez", resultado.getApellido());
        assertEquals("DNI", resultado.getTipoDocumento());
        assertEquals("35123457", resultado.getNumeroDocumento());
        assertEquals(LocalDate.of(1990, 5, 20), resultado.getFechaNacimiento());
        assertEquals("CABA", resultado.getDireccionHuesped().getLocalidad());
        assertTrue(resultado.getAlojado());

    }

    /**
     * Test of modificarHuesped method, of class GestorHuespedes.
     */

    @Test
    
    public void testModificarHuesped() {
        System.out.println("modificarHuesped");
        List<HuespedDTO> huespedes = null;
        GestorHuespedes instance = null;
        Boolean expResult = null;
        Boolean result = instance.modificarHuesped(huespedes);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

    /**
     * Test of obtenerTodos method, of class GestorHuespedes.
     */
    
    @Test
    public void testObtenerTodos() {
        System.out.println("obtenerTodos");
        GestorHuespedes instance = null;
        List<HuespedDTO> expResult = null;
        List<HuespedDTO> result = instance.obtenerTodos();
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    /**
     * Test of buscarHuesped method, of class GestorHuespedes.
     */
    
    @Test
    public void testBuscarHuesped() {
        System.out.println("buscarHuesped");
        String tipo = "";
        String numero = "";
        String nombre = "";
        String apellido = "";
        GestorHuespedes instance = null;
        List<HuespedDTO> expResult = null;
        List<HuespedDTO> result = instance.buscarHuesped(tipo, numero, nombre, apellido);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    /**
     * Test of obtenerHuesped method, of class GestorHuespedes.
     */
    
    @Test
    public void testObtenerHuesped() {
        System.out.println("obtenerHuesped");
        String tipo = "";
        String numero = "";
        GestorHuespedes instance = null;
        Huesped expResult = null;
        Huesped result = instance.obtenerHuesped(tipo, numero);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    /**
     * Test of consultarDocumento method, of class GestorHuespedes.
     */
    
    @Test
    public void testConsultarDocumento() {
        System.out.println("consultarDocumento");
        String tipoDocumento = "";
        String numeroDocumento = "";
        GestorHuespedes instance = null;
        boolean expResult = false;
        boolean result = instance.consultarDocumento(tipoDocumento, numeroDocumento);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
    /**
     * Test of huespedExistente method, of class GestorHuespedes.
     */

    @Test
    public void testHuespedExistente() {
        System.out.println("huespedExistente");
        String tipoModificado = "";
        String numeroModificado = "";
        String tipoOriginal = "";
        String numeroOriginal = "";
        GestorHuespedes instance = null;
        boolean expResult = false;
        boolean result = instance.huespedExistente(tipoModificado, numeroModificado, tipoOriginal, numeroOriginal);
        assertEquals(expResult, result);
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }
}
