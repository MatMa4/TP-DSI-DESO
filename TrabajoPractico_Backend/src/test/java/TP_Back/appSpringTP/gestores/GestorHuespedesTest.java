/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.DireccionDAOImpl;
import TP_Back.appSpringTP.DAOs.HuespedDAOImpl;
import TP_Back.appSpringTP.DAOs.PersonaFisicaDAOImpl;
import TP_Back.appSpringTP.DAOs.ResponsablePagoDAOImpl;
import TP_Back.appSpringTP.DTOs.DireccionDTO;
import TP_Back.appSpringTP.DTOs.HuespedDTO;
import TP_Back.appSpringTP.excepciones.HuespedExistenteException;
import TP_Back.appSpringTP.excepciones.HuespedNoEliminableException;
import TP_Back.appSpringTP.excepciones.HuespedNoEncontradoException;
import TP_Back.appSpringTP.modelo.huesped.Huesped;
import TP_Back.appSpringTP.modelo.direccion.Direccion;
import TP_Back.appSpringTP.modelo.pago.PersonaFisica;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

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
    
    @Mock
    private PersonaFisicaDAOImpl personaFisicaDAO;
    
    @Mock
    private ResponsablePagoDAOImpl responsablePagoDAO;

    @InjectMocks
    private GestorHuespedes gestorHuespedes;

    @Test
    public void testRegistrarHuesped_casoIdeal() {
        //Creamos los datos de entrada
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

        //Creamos los datos que debería dar como resultado huespedDTO.save()
        //Debería retornar un objeto Huesped, en lugar de HuespedDTO, por eso debemos crearlo
        Direccion direccionGuardada = new Direccion();
        direccionGuardada.setCalle(dirDto.getCalle());
        direccionGuardada.setNumero(dirDto.getNumero());
        direccionGuardada.setLocalidad(dirDto.getLocalidad());
        direccionGuardada.setProvincia(dirDto.getProvincia());
        direccionGuardada.setPais(dirDto.getPais());        
        
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
        huespedGuardado.setDireccionHuesped(direccionGuardada);
        
        //Generamos el objeto de retorno de uno de los métodos utilzados
        PersonaFisica persona = new PersonaFisica();
        persona.setHuesped(huespedGuardado);

        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        when(huespedDAO.consultarDocumento("DNI", "35123457"))
                .thenReturn(Optional.of(dto));

        when(huespedDAO.save(any(Huesped.class))).thenReturn(huespedGuardado);
        
        when(personaFisicaDAO.save(any(PersonaFisica.class))).thenReturn(persona);

        when(personaFisicaDAO.getByHuesped(anyString(), anyString()))
                .thenReturn(Optional.of(persona));

        
        //Ejecutamos la prueba
        HuespedDTO resultado = gestorHuespedes.registrarHuesped(dto);

        //Comprobamos que se ejecuten los métodos
        verify(direccionDAO).save(any(Direccion.class));
        verify(huespedDAO).save(any(Huesped.class));
        verify(personaFisicaDAO).save(any(PersonaFisica.class));
        verify(personaFisicaDAO).getByHuesped(anyString(), anyString());
        verify(huespedDAO).consultarDocumento(dto.getTipoDocumento(), dto.getNumeroDocumento());
        
        //Verificamos que el DTO retornado tiene los datos esperados
        assertEquals("Carlos Alberto", resultado.getNombre());
        assertEquals("Gomez", resultado.getApellido());
        assertEquals("DNI", resultado.getTipoDocumento());
        assertEquals("35123457", resultado.getNumeroDocumento());
        assertEquals(LocalDate.of(1990, 5, 20), resultado.getFechaNacimiento());
        assertEquals("CABA", resultado.getDireccionHuesped().getLocalidad());
        assertTrue(resultado.getAlojado());
    }
    
    @Test
    public void testRegistrarHuesped_huespedSinDocumento() {
        //Creamos los datos de entrada
        DireccionDTO dirDto = new DireccionDTO();
                dirDto.setCalle("Av Corrientes");
                dirDto.setNumero(1234);
                dirDto.setLocalidad("CABA");
                dirDto.setProvincia("Buenos Aires");
                dirDto.setPais("Argentina");

        HuespedDTO dto = HuespedDTO.builder()
                .nombre("Carlos Alberto")
                .apellido("Gomez")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .telefono("3412345678")
                .ocupacion("Ingeniero")
                .nacionalidad("Argentina")
                .alojado(true)
                .direccion(dirDto)
                .build();

        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        when(huespedDAO.save(any(Huesped.class))).thenThrow(new RuntimeException("error al guardar en base de datos"));

        //Ejecutamos la prueba
        assertThrows(RuntimeException.class,
            () -> gestorHuespedes.registrarHuesped(dto));
        
        //Comprobamos que se ejecuten los métodos
        verify(direccionDAO).save(any(Direccion.class));
        verify(huespedDAO).save(any(Huesped.class));
        verify(huespedDAO, never()).consultarDocumento(dto.getTipoDocumento(), dto.getNumeroDocumento());
        verify(personaFisicaDAO, never()).save(any(PersonaFisica.class));
    }
    
    @Test
    public void testRegistrarHuesped_direccionVacia() {
        //Creamos los datos de entrada
        DireccionDTO dirDto = new DireccionDTO();

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

        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        when(direccionDAO.save(any(Direccion.class))).thenThrow(new RuntimeException("error al guardar en base de datos"));

        //Ejecutamos la prueba
        assertThrows(RuntimeException.class,
            () -> gestorHuespedes.registrarHuesped(dto));
        
        //Comprobamos que se ejecuten los métodos
        verify(direccionDAO).save(any(Direccion.class));
        verify(huespedDAO, never()).save(any(Huesped.class));
        verify(personaFisicaDAO, never()).save(any(PersonaFisica.class));
        verify(personaFisicaDAO, never()).getByHuesped(any(String.class), any(String.class));
        verify(huespedDAO, never()).consultarDocumento(dto.getTipoDocumento(), dto.getNumeroDocumento());
    }
    
    @Test
    public void testRegistrarHuesped_saveRealizadoPeroHuespedAusenteEnBD() {
        //Creamos los datos de entrada
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

        //Creamos los datos que debería dar como resultado huespedDTO.save()
        //Debería retornar un objeto Huesped, en lugar de HuespedDTO, por eso debemos crearlo
        Direccion direccionGuardada = new Direccion();
        direccionGuardada.setCalle(dirDto.getCalle());
        direccionGuardada.setNumero(dirDto.getNumero());
        direccionGuardada.setLocalidad(dirDto.getLocalidad());
        direccionGuardada.setProvincia(dirDto.getProvincia());
        direccionGuardada.setPais(dirDto.getPais());        
        
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
        huespedGuardado.setDireccionHuesped(direccionGuardada);

        //Generamos el objeto de retorno de uno de los métodos utilzados
        PersonaFisica persona = new PersonaFisica();
        persona.setHuesped(huespedGuardado);
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        when(huespedDAO.consultarDocumento("DNI", "35123457"))
                .thenThrow(new HuespedNoEncontradoException("huesped no encontrado en la base de datos"));

        when(huespedDAO.save(any(Huesped.class))).thenReturn(huespedGuardado);
        
        when(personaFisicaDAO.save(any(PersonaFisica.class))).thenReturn(persona);

        when(personaFisicaDAO.getByHuesped(anyString(), anyString()))
                .thenReturn(Optional.of(persona));
        //Ejecutamos la prueba
        assertThrows(HuespedNoEncontradoException.class,
            () -> gestorHuespedes.registrarHuesped(dto));

        //Comprobamos que se ejecuten los métodos
        verify(direccionDAO).save(any(Direccion.class));
        verify(huespedDAO).save(any(Huesped.class));
        verify(personaFisicaDAO).save(any(PersonaFisica.class));
        verify(personaFisicaDAO).getByHuesped(any(String.class), any(String.class));
        verify(huespedDAO).consultarDocumento(dto.getTipoDocumento(), dto.getNumeroDocumento());
    }
    
    @Test
    public void testRegistrarHuesped_falloAlGuardarPersonaFisica() {
        //Creamos los datos de entrada
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

        //Creamos los datos que debería dar como resultado huespedDTO.save()
        //Debería retornar un objeto Huesped, en lugar de HuespedDTO, por eso debemos crearlo
        Direccion direccionGuardada = new Direccion();
        direccionGuardada.setCalle(dirDto.getCalle());
        direccionGuardada.setNumero(dirDto.getNumero());
        direccionGuardada.setLocalidad(dirDto.getLocalidad());
        direccionGuardada.setProvincia(dirDto.getProvincia());
        direccionGuardada.setPais(dirDto.getPais());        
        
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
        huespedGuardado.setDireccionHuesped(direccionGuardada);

        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
         when(huespedDAO.save(any(Huesped.class))).thenReturn(huespedGuardado);
        
        when(personaFisicaDAO.save(any(PersonaFisica.class))).thenThrow(new RuntimeException("Error inesperado al registrar el responsable de pago"));

        //Ejecutamos la prueba
        assertThrows(RuntimeException.class,
            () -> gestorHuespedes.registrarHuesped(dto));

        //Comprobamos que se ejecuten los métodos
        verify(direccionDAO).save(any(Direccion.class));
        verify(huespedDAO).save(any(Huesped.class));
        verify(personaFisicaDAO).save(any(PersonaFisica.class));
        verify(personaFisicaDAO, never()).getByHuesped(any(String.class), any(String.class));        
        verify(huespedDAO, never()).consultarDocumento(dto.getTipoDocumento(), dto.getNumeroDocumento());
    }
    
        @Test
    public void testRegistrarHuesped_personaFisicaGuardadPeroNoEncontrada() {
        //Creamos los datos de entrada
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

        //Creamos los datos que debería dar como resultado huespedDTO.save()
        //Debería retornar un objeto Huesped, en lugar de HuespedDTO, por eso debemos crearlo
        Direccion direccionGuardada = new Direccion();
        direccionGuardada.setCalle(dirDto.getCalle());
        direccionGuardada.setNumero(dirDto.getNumero());
        direccionGuardada.setLocalidad(dirDto.getLocalidad());
        direccionGuardada.setProvincia(dirDto.getProvincia());
        direccionGuardada.setPais(dirDto.getPais());        
        
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
        huespedGuardado.setDireccionHuesped(direccionGuardada);

        PersonaFisica persona = new PersonaFisica();
        persona.setHuesped(huespedGuardado);
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        when(huespedDAO.save(any(Huesped.class))).thenReturn(huespedGuardado);
        
        when(personaFisicaDAO.save(any(PersonaFisica.class))).thenReturn(persona);
        
        when(personaFisicaDAO.getByHuesped(any(String.class), any(String.class))).thenThrow(new RuntimeException("Responsable de pago no guardado"));
        //Ejecutamos la prueba
        assertThrows(RuntimeException.class,
            () -> gestorHuespedes.registrarHuesped(dto));

        //Comprobamos que se ejecuten los métodos
        verify(direccionDAO).save(any(Direccion.class));
        verify(huespedDAO).save(any(Huesped.class));
        verify(personaFisicaDAO).save(any(PersonaFisica.class));
        verify(personaFisicaDAO).getByHuesped(any(String.class), any(String.class));
        verify(huespedDAO, never()).consultarDocumento(dto.getTipoDocumento(), dto.getNumeroDocumento());
    }

    /**
     * Test of modificarHuesped method, of class GestorHuespedes.
     */

    @Test
    public void testModificarHuesped_casoIdeal() {
        //Creamos los datos que vamos a ingresar a la prueba
        DireccionDTO dirDto = new DireccionDTO();
        dirDto.setCalle("Av Corrientes");
        dirDto.setNumero(1234);
        dirDto.setLocalidad("CABA");
        dirDto.setProvincia("Buenos Aires");
        dirDto.setPais("Argentina");
        
        //En la posición 0 el huesped modificado y en la 1 el existente
        List<HuespedDTO> huespedes = Arrays.asList(
        HuespedDTO.builder()
                .nombre("Carlos Adrian")
                .apellido("Gomez")
                .tipoDocumento("DNI")
                .numeroDocumento("35123457")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .telefono("3412345678")
                .ocupacion("Contador")
                .nacionalidad("Argentina")
                .alojado(true)
                .direccion(dirDto)
                .build(), 
        HuespedDTO.builder()
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
                .build()
        );
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        when(huespedDAO.consultarDocumento("DNI", "35123457"))
                .thenReturn(Optional.of(huespedes.get(0)));
        
        //Ejecutamos la prueba
        HuespedDTO resultado = gestorHuespedes.modificarHuesped(huespedes);
        
        //Comprueba que se llamen los métodos
        verify(huespedDAO).modificarIDHuesped(huespedes.get(0), huespedes.get(1));
        verify(direccionDAO).save(any(Direccion.class));
        verify(huespedDAO).save(any(Huesped.class));
        verify(huespedDAO).consultarDocumento("DNI", "35123457");


        //Comprobamos el retorno para verificar que corresponda con lo esperado
        assertEquals("Carlos Adrian", resultado.getNombre()); //Nombre cambiado
        assertEquals("Gomez", resultado.getApellido());
        assertEquals("DNI", resultado.getTipoDocumento());
        assertEquals("35123457", resultado.getNumeroDocumento());
        assertEquals("Contador", resultado.getOcupacion()); //Ocupacion cambiada
        assertEquals(LocalDate.of(1990, 5, 20), resultado.getFechaNacimiento());
        assertEquals("CABA", resultado.getDireccionHuesped().getLocalidad());
        assertTrue(resultado.getAlojado());
    }
    
    @Test
    public void testModificarHuesped_huespedNoEncontrado() {
        //Creamos los datos que vamos a ingresar a la prueba
        DireccionDTO dirDto = new DireccionDTO();
        dirDto.setCalle("Av Corrientes");
        dirDto.setNumero(1234);
        dirDto.setLocalidad("CABA");
        dirDto.setProvincia("Buenos Aires");
        dirDto.setPais("Argentina");
        
        //En la posición 0 el huesped modificado y en la 1 el existente
        List<HuespedDTO> huespedes = Arrays.asList(
        HuespedDTO.builder()
                .nombre("Carlos Adrian")
                .apellido("Gomez")
                .tipoDocumento("DNI")
                .numeroDocumento("35123457")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .telefono("3412345678")
                .ocupacion("Contador")
                .nacionalidad("Argentina")
                .alojado(true)
                .direccion(dirDto)
                .build(), 
        HuespedDTO.builder()
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
                .build()
        );
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        doThrow(new HuespedNoEncontradoException("huesped no encontrado"))
                .when(huespedDAO)
                .modificarIDHuesped(huespedes.get(0), huespedes.get(1));

        
        //Ejecutamos la prueba
        assertThrows(HuespedNoEncontradoException.class,
            () -> gestorHuespedes.modificarHuesped(huespedes));
        
        //Comprueba que se llamen los métodos
        verify(huespedDAO).modificarIDHuesped(huespedes.get(0), huespedes.get(1));
        verify(direccionDAO, never()).save(any(Direccion.class));
        verify(huespedDAO, never()).save(any(Huesped.class));
        verify(huespedDAO, never()).consultarDocumento("DNI", "35123457");
    }
    
    @Test
    public void testModificarHuesped_errorAlGuardarHuesped() {
        //Creamos los datos que vamos a ingresar a la prueba
        DireccionDTO dirDto = new DireccionDTO();
        dirDto.setCalle("Av Corrientes");
        dirDto.setNumero(1234);
        dirDto.setLocalidad("CABA");
        dirDto.setProvincia("Buenos Aires");
        dirDto.setPais("Argentina");
        
        //En la posición 0 el huesped modificado y en la 1 el existente
        List<HuespedDTO> huespedes = Arrays.asList(
        HuespedDTO.builder()
                .nombre("Carlos Adrian")
                .apellido("Gomez")
                .tipoDocumento("DNI")
                .numeroDocumento("35123457")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .telefono("3412345678")
                .ocupacion("Contador")
                .nacionalidad("Argentina")
                .alojado(true)
                .direccion(dirDto)
                .build(), 
        HuespedDTO.builder()
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
                .build()
        );
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        doThrow(new RuntimeException("Error inesperado al guardar el huésped modificado"))
                .when(huespedDAO)
                .save(any(Huesped.class));

        
        //Ejecutamos la prueba
        assertThrows(RuntimeException.class,
            () -> gestorHuespedes.modificarHuesped(huespedes));
        
        //Comprueba que se llamen los métodos
        verify(huespedDAO).modificarIDHuesped(huespedes.get(0), huespedes.get(1));
        verify(direccionDAO).save(any(Direccion.class));
        verify(huespedDAO).save(any(Huesped.class));
        verify(huespedDAO, never()).consultarDocumento("DNI", "35123457");
    }
    
    @Test
    public void testModificarHuesped_errorAlGuardarDireccion() {
        //Creamos los datos que vamos a ingresar a la prueba
        DireccionDTO dirDto = new DireccionDTO();
        dirDto.setCalle("Av Corrientes");
        dirDto.setNumero(1234);
        dirDto.setLocalidad("CABA");
        dirDto.setProvincia("Buenos Aires");
        dirDto.setPais("Argentina");
        
        //En la posición 0 el huesped modificado y en la 1 el existente
        List<HuespedDTO> huespedes = Arrays.asList(
        HuespedDTO.builder()
                .nombre("Carlos Adrian")
                .apellido("Gomez")
                .tipoDocumento("DNI")
                .numeroDocumento("35123457")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .telefono("3412345678")
                .ocupacion("Contador")
                .nacionalidad("Argentina")
                .alojado(true)
                .direccion(dirDto)
                .build(), 
        HuespedDTO.builder()
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
                .build()
        );
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        doThrow(new RuntimeException("Error inesperado al guardar el huésped modificado"))
                .when(direccionDAO)
                .save(any(Direccion.class));

        
        //Ejecutamos la prueba
        assertThrows(RuntimeException.class,
            () -> gestorHuespedes.modificarHuesped(huespedes));
        
        //Comprueba que se llamen los métodos
        verify(huespedDAO).modificarIDHuesped(huespedes.get(0), huespedes.get(1));
        verify(direccionDAO).save(any(Direccion.class));
        verify(huespedDAO, never()).save(any(Huesped.class));
        verify(huespedDAO, never()).consultarDocumento("DNI", "35123457");
    }
    
    
    @Test
    public void testModificarHuesped_noEncontradoDespesDeGuardar() {
        //Creamos los datos que vamos a ingresar a la prueba
        DireccionDTO dirDto = new DireccionDTO();
        dirDto.setCalle("Av Corrientes");
        dirDto.setNumero(1234);
        dirDto.setLocalidad("CABA");
        dirDto.setProvincia("Buenos Aires");
        dirDto.setPais("Argentina");
        
        //En la posición 0 el huesped modificado y en la 1 el existente
        List<HuespedDTO> huespedes = Arrays.asList(
        HuespedDTO.builder()
                .nombre("Carlos Adrian")
                .apellido("Gomez")
                .tipoDocumento("DNI")
                .numeroDocumento("35123457")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .telefono("3412345678")
                .ocupacion("Contador")
                .nacionalidad("Argentina")
                .alojado(true)
                .direccion(dirDto)
                .build(), 
        HuespedDTO.builder()
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
                .build()
        );
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        when(huespedDAO.consultarDocumento(huespedes.get(0).getTipoDocumento(), huespedes.get(0).getNumeroDocumento()))
                .thenReturn(Optional.empty());

        
        //Ejecutamos la prueba
        assertThrows(HuespedNoEncontradoException.class,
            () -> gestorHuespedes.modificarHuesped(huespedes));
        
        //Comprueba que se llamen los métodos
        verify(huespedDAO).modificarIDHuesped(huespedes.get(0), huespedes.get(1));
        verify(direccionDAO).save(any(Direccion.class));
        verify(huespedDAO).save(any(Huesped.class));
        verify(huespedDAO).consultarDocumento("DNI", "35123457");
    }
    
    /**
     * Test of buscarHuesped method, of class GestorHuespedes.
     */
    
    @Test
    public void testEliminarHuesped_casoIdeal(){
        //Creamos los datos que vamos a ingresar a la prueba
        DireccionDTO dirDto = new DireccionDTO();
        dirDto.setCalle("Av Corrientes");
        dirDto.setNumero(1234);
        dirDto.setLocalidad("CABA");
        dirDto.setProvincia("Buenos Aires");
        dirDto.setPais("Argentina");
        
        HuespedDTO huesped = HuespedDTO.builder()
                .nombre("Carlos Adrian")
                .apellido("Gomez")
                .tipoDocumento("DNI")
                .numeroDocumento("35123457")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .telefono("3412345678")
                .ocupacion("Contador")
                .nacionalidad("Argentina")
                .alojado(false)
                .direccion(dirDto)
                .build();
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        //La primera vez se retorna un Optional con un huesped y la segunda vez un Optional vacío
        when(huespedDAO.consultarDocumento(any(String.class), any(String.class)))
                .thenReturn(Optional.of(huesped))
                .thenReturn(Optional.empty());
        
        when(personaFisicaDAO.getIdResponsablePagoConHuesped(any(String.class), any(String.class)))
                .thenReturn(123);
        
        //Ejecutamos la prueba
        Boolean resultado = gestorHuespedes.eliminarHuesped(huesped);

        //Comprueba que se llamen los métodos
        verify(huespedDAO).eliminar(any(HuespedDTO.class));
        verify(huespedDAO, times(2)).consultarDocumento(any(String.class), any(String.class));
        verify(personaFisicaDAO).getIdResponsablePagoConHuesped(any(String.class), any(String.class));
        verify(responsablePagoDAO).eliminar(123);
        
        //Verificamos que el resultado sea el esperado
        assertEquals(true, resultado);
        
    }
    
        @Test
    public void testEliminarHuesped_huespedInexistente(){
        //Creamos los datos que vamos a ingresar a la prueba
        DireccionDTO dirDto = new DireccionDTO();
        dirDto.setCalle("Av Corrientes");
        dirDto.setNumero(1234);
        dirDto.setLocalidad("CABA");
        dirDto.setProvincia("Buenos Aires");
        dirDto.setPais("Argentina");
        
        HuespedDTO huesped = HuespedDTO.builder()
                .nombre("Carlos Adrian")
                .apellido("Gomez")
                .tipoDocumento("DNI")
                .numeroDocumento("35123457")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .telefono("3412345678")
                .ocupacion("Contador")
                .nacionalidad("Argentina")
                .alojado(false)
                .direccion(dirDto)
                .build();
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        //La primera vez se retorna un Optional con un huesped y la segunda vez un Optional vacío
        when(huespedDAO.consultarDocumento(any(String.class), any(String.class)))
                .thenReturn(Optional.empty());
        
        //Ejecutamos la prueba
        assertThrows(HuespedNoEncontradoException.class,
            () -> gestorHuespedes.eliminarHuesped(huesped));

        //Comprueba que se llamen los métodos
        verify(huespedDAO, never()).eliminar(any(HuespedDTO.class));
        verify(huespedDAO, times(1)).consultarDocumento(any(String.class), any(String.class));
        verify(personaFisicaDAO, never()).getIdResponsablePagoConHuesped(any(String.class), any(String.class));
        verify(responsablePagoDAO, never()).eliminar(123);
    }
   
    @Test
    public void testEliminarHuesped_huespedAlojado(){
        //Creamos los datos que vamos a ingresar a la prueba
        DireccionDTO dirDto = new DireccionDTO();
        dirDto.setCalle("Av Corrientes");
        dirDto.setNumero(1234);
        dirDto.setLocalidad("CABA");
        dirDto.setProvincia("Buenos Aires");
        dirDto.setPais("Argentina");
        
        HuespedDTO huesped = HuespedDTO.builder()
                .nombre("Carlos Adrian")
                .apellido("Gomez")
                .tipoDocumento("DNI")
                .numeroDocumento("35123457")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .telefono("3412345678")
                .ocupacion("Contador")
                .nacionalidad("Argentina")
                .alojado(true)
                .direccion(dirDto)
                .build();
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        //La primera vez se retorna un Optional con un huesped y la segunda vez un Optional vacío
        when(huespedDAO.consultarDocumento(any(String.class), any(String.class)))
                .thenReturn(Optional.of(huesped));
        
        //Ejecutamos la prueba
        assertThrows(HuespedNoEliminableException.class,
            () -> gestorHuespedes.eliminarHuesped(huesped));

        //Comprueba que se llamen los métodos
        verify(huespedDAO, never()).eliminar(any(HuespedDTO.class));
        verify(huespedDAO, times(1)).consultarDocumento(any(String.class), any(String.class));
        verify(personaFisicaDAO, never()).getIdResponsablePagoConHuesped(any(String.class), any(String.class));
        verify(responsablePagoDAO, never()).eliminar(123);
    }
    
    @Test
    public void testEliminarHuesped_existeFacturaRelacionada(){
        //Creamos los datos que vamos a ingresar a la prueba
        DireccionDTO dirDto = new DireccionDTO();
        dirDto.setCalle("Av Corrientes");
        dirDto.setNumero(1234);
        dirDto.setLocalidad("CABA");
        dirDto.setProvincia("Buenos Aires");
        dirDto.setPais("Argentina");
        
        HuespedDTO huesped = HuespedDTO.builder()
                .nombre("Carlos Adrian")
                .apellido("Gomez")
                .tipoDocumento("DNI")
                .numeroDocumento("35123457")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .telefono("3412345678")
                .ocupacion("Contador")
                .nacionalidad("Argentina")
                .alojado(false)
                .direccion(dirDto)
                .build();
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        //La primera vez se retorna un Optional con un huesped y la segunda vez un Optional vacío
        when(huespedDAO.consultarDocumento(any(String.class), any(String.class)))
                .thenReturn(Optional.of(huesped));
        
        when(personaFisicaDAO.getIdResponsablePagoConHuesped(any(String.class), any(String.class)))
                .thenReturn(123);
        
        doThrow(new DataIntegrityViolationException("el huesped tiene una factura asociada"))
                .when(responsablePagoDAO)
                .eliminar(123);
        
        //Ejecutamos la prueba
        assertThrows(DataIntegrityViolationException.class,
            () -> gestorHuespedes.eliminarHuesped(huesped));

        //Comprueba que se llamen los métodos
        verify(huespedDAO, never()).eliminar(any(HuespedDTO.class));
        verify(huespedDAO, times(1)).consultarDocumento(any(String.class), any(String.class));
        verify(personaFisicaDAO).getIdResponsablePagoConHuesped(any(String.class), any(String.class));
        verify(responsablePagoDAO).eliminar(123);
    }
    
    @Test
    public void testEliminarHuesped_huespedNoEliminado(){
        //Creamos los datos que vamos a ingresar a la prueba
        DireccionDTO dirDto = new DireccionDTO();
        dirDto.setCalle("Av Corrientes");
        dirDto.setNumero(1234);
        dirDto.setLocalidad("CABA");
        dirDto.setProvincia("Buenos Aires");
        dirDto.setPais("Argentina");
        
        HuespedDTO huesped = HuespedDTO.builder()
                .nombre("Carlos Adrian")
                .apellido("Gomez")
                .tipoDocumento("DNI")
                .numeroDocumento("35123457")
                .fechaNacimiento(LocalDate.of(1990, 5, 20))
                .telefono("3412345678")
                .ocupacion("Contador")
                .nacionalidad("Argentina")
                .alojado(false)
                .direccion(dirDto)
                .build();
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        //La primera vez se retorna un Optional con un huesped y la segunda vez un Optional vacío
        when(huespedDAO.consultarDocumento(any(String.class), any(String.class)))
                .thenReturn(Optional.of(huesped))
                .thenReturn(Optional.of(huesped));
        
        //Ejecutamos la prueba
        Boolean resultado = gestorHuespedes.eliminarHuesped(huesped);

        //Comprueba que se llamen los métodos
        verify(huespedDAO).eliminar(any(HuespedDTO.class));
        verify(huespedDAO, times(2)).consultarDocumento(any(String.class), any(String.class));
        verify(personaFisicaDAO).getIdResponsablePagoConHuesped(any(String.class), any(String.class));
        
        //Verificamos que el resultado sea el esperado
        assertEquals(false, resultado);
        
    }
    
    /**
     * Test of buscarHuesped method, of class GestorHuespedes.
     */
    
    @Test
    public void testBuscarHuesped_casoIdeal() {
        //Creamos los datos que vamos a ingresar a la prueba
        String tipo = "DNI";
        String numero = "35123457";
        String nombre = "";
        String apellido = "";
        
        //Creamos los datos de salida de la prueba
        DireccionDTO dirDto = new DireccionDTO();
        dirDto.setCalle("Av Corrientes");
        dirDto.setNumero(1234);
        dirDto.setLocalidad("CABA");
        dirDto.setProvincia("Buenos Aires");
        dirDto.setPais("Argentina");
        
        List<HuespedDTO> huespedes = Arrays.asList(
        HuespedDTO.builder()
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
                .build()
        );
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        when(huespedDAO.buscarHuesped(any(HuespedDTO.class))).thenReturn(huespedes);
        
        //Ejecutamos la prueba
        List<HuespedDTO> resultado = gestorHuespedes.buscarHuesped(tipo, numero, nombre, apellido);
        
        //Comprueba que se llamen los métodos
        verify(huespedDAO).buscarHuesped(any(HuespedDTO.class));
        
        //Comprobamos el retorno para verificar que corresponda con lo esperado
        assertEquals(1, resultado.size());
        assertEquals("Carlos Alberto", resultado.get(0).getNombre());
        assertEquals("Gomez", resultado.get(0).getApellido());
    }
    
    @Test
    public void testBuscarHuesped_huespedNoEncontrado() {
        //Creamos los datos que vamos a ingresar a la prueba
        String tipo = "DNI";
        String numero = "35123457";
        String nombre = "";
        String apellido = "";
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        when(huespedDAO.buscarHuesped(any(HuespedDTO.class)))
                .thenThrow(new HuespedNoEncontradoException("No se encontró huesped"));
        
        //Ejecutamos la prueba
        assertThrows(HuespedNoEncontradoException.class,
            () -> gestorHuespedes.buscarHuesped(tipo, numero, nombre, apellido));
        
        //Comprueba que se llamen los métodos
        verify(huespedDAO).buscarHuesped(any(HuespedDTO.class));
    }
    
    /**
     * Test of consultarDocumento method, of class GestorHuespedes.
     */
    
    @Test
    public void testConsultarDocumento_existeHuesped() {
        //Creamos el resultado de consultarDocumento
        DireccionDTO dirDto = new DireccionDTO();
        dirDto.setCalle("Av Corrientes");
        dirDto.setNumero(1234);
        dirDto.setLocalidad("CABA");
        dirDto.setProvincia("Buenos Aires");
        dirDto.setPais("Argentina");

        HuespedDTO huesped = HuespedDTO.builder()
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
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        when(huespedDAO.consultarDocumento("DNI", "35123457"))
                .thenReturn(Optional.of(huesped));
        
        //Ejecutamos la prueba
        assertThrows(HuespedExistenteException.class,
            () -> gestorHuespedes.consultarDocumento("DNI", "35123457"));
        
        //Comprueba que se llamen los métodos
        verify(huespedDAO).consultarDocumento("DNI", "35123457");
        
    }
    
    @Test
    public void testConsultarDocumento_noExisteHuesped() {        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        when(huespedDAO.consultarDocumento("DNI", "35123457"))
                .thenReturn(Optional.empty());
        
        //Ejecutamos la prueba
        gestorHuespedes.consultarDocumento("DNI", "35123457");
        
        //Comprueba que se llamen los métodos
        verify(huespedDAO).consultarDocumento("DNI", "35123457");
        
    }
    
    /**
     * Test of huespedExistente method, of class GestorHuespedes.
     */

    @Test
    public void testHuespedExistente_cambiaADocumentoExistente() {
        //Creamos el resultado de consultarDocumento
        DireccionDTO dirDto = new DireccionDTO();
        dirDto.setCalle("Av Corrientes");
        dirDto.setNumero(1234);
        dirDto.setLocalidad("CABA");
        dirDto.setProvincia("Buenos Aires");
        dirDto.setPais("Argentina");

        HuespedDTO huesped = HuespedDTO.builder()
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
        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        when(huespedDAO.consultarDocumento("DNI", "35123457"))
                .thenReturn(Optional.of(huesped));
        
        //Ejecutamos la prueba
        assertThrows(HuespedExistenteException.class,
            () -> gestorHuespedes.huespedExistente("DNI", "35123457", "DNI", "35123456"));
        
        //Comprueba que se llamen los métodos
        verify(huespedDAO).consultarDocumento("DNI", "35123457");
    }
    
        @Test
    public void testHuespedExistente_cambiaADocumentoDisponible() {        
        //Establecemos el valor de retorno del método para evitar depenencia en la prueba
        when(huespedDAO.consultarDocumento("DNI", "35123457"))
                .thenReturn(Optional.empty());
        
        //Ejecutamos la prueba
        gestorHuespedes.huespedExistente("DNI", "35123457", "DNI", "35123456");
        
        //Comprueba que se llamen los métodos
        verify(huespedDAO).consultarDocumento("DNI", "35123457");
    }
    
    @Test
    public void testHuespedExistente_noCambiaDocumento() {
        
        //Ejecutamos la prueba
        gestorHuespedes.huespedExistente("DNI", "35123457", "DNI", "35123457");
        
        //Comprueba que se llamen los métodos
        verify(huespedDAO, never()).consultarDocumento("DNI", "35123457");
    }
}
