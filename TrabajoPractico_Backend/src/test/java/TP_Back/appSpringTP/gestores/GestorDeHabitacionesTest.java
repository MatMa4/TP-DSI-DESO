/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/UnitTests/JUnit5TestClass.java to edit this template
 */
package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.HabitacionDAOImpl;
import TP_Back.appSpringTP.DTOs.HabitacionDTO;
import TP_Back.appSpringTP.DTOs.HabitacionDetalleDTO;
import TP_Back.appSpringTP.DTOs.ReservaDTO;
import TP_Back.appSpringTP.DTOs.ocupacion.OcupacionDTO;
import java.sql.Date;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.extension.ExtendWith;
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
public class GestorDeHabitacionesTest {
    
    public GestorDeHabitacionesTest() {
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
    
    @Mock
    private HabitacionDAOImpl habitacionDAO;
    
    @InjectMocks
    private GestorDeHabitaciones gestorHabitaciones;

    /**
     * Test of getHabitacionesConDetalle method, of class GestorDeHabitaciones.
     */
    @Test
    void testGetHabitacionesConDetalle_casoIdeal() {
        // Datos de entrada
        Date fechaInicio = Date.valueOf("2025-01-01");
        Date fechaFin = Date.valueOf("2025-01-10");

        // Creamos objetos simulados
        HabitacionDTO habitacion = new HabitacionDTO();
        habitacion.setNumero(101);
        habitacion.setCostoPorNoche(1500f);
        habitacion.setCapacidad(2);
        habitacion.setEstado("Disponible");
        habitacion.setDescripcion("Habitación doble con vista al mar");

        OcupacionDTO ocupacion = new OcupacionDTO();
        ocupacion.setId(1);
        ocupacion.setHabitacion(habitacion);
        ocupacion.setFechaInicio(fechaInicio);
        ocupacion.setFechaFin(fechaFin);

        ReservaDTO reserva = new ReservaDTO();
        reserva.setIdReserva(10);
        reserva.setFechaInicio(fechaInicio);
        reserva.setFechaFin(fechaFin);
        reserva.setEstado("Confirmada");
        reserva.setNombre("Carlos");
        reserva.setApellido("Gomez");
        reserva.setTelefono("3412345678");
        reserva.setHabitacionNumero(101);

        HabitacionDetalleDTO detalle = new HabitacionDetalleDTO();
        detalle.setHabitacion(habitacion);
        detalle.setOcupaciones(List.of(ocupacion));
        detalle.setReservas(List.of(reserva));

        List<HabitacionDetalleDTO> listaEsperada = List.of(detalle);

        // Establecemos el valor de retorno del método para evitar depenencia en la prueba
        when(habitacionDAO.getHabitacionesConDetalle(fechaInicio, fechaFin))
                .thenReturn(listaEsperada);

        // Ejecución del método a probar
        List<HabitacionDetalleDTO> resultado =
                gestorHabitaciones.getHabitacionesConDetalle(fechaInicio, fechaFin);

        // Comprobamos que se ejecuten los métodos
        verify(habitacionDAO).getHabitacionesConDetalle(fechaInicio, fechaFin);
        
        // Verificamos el resultado obtenido
        assertEquals(listaEsperada, resultado); 
    }

    
}
