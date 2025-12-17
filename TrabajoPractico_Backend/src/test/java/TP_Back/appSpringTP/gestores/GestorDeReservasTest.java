package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.HabitacionDAO;
import TP_Back.appSpringTP.DAOs.ReservaDAO;
import TP_Back.appSpringTP.DTOs.ReservaDTO;
import TP_Back.appSpringTP.modelo.habitacion.Habitacion;
import TP_Back.appSpringTP.modelo.reserva.Reserva;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GestorDeReservasTest {

    @Mock
    private ReservaDAO reservaDAO;

    @Mock
    private HabitacionDAO habitacionDAO;

    @InjectMocks
    private GestorDeReservas gestorDeReservas;

    // --- Tests para crearReserva ---

    @Test
    public void testCrearReservaExito() {
        // Arrange
        ReservaDTO dto = new ReservaDTO();
        dto.setFechaInicio(java.sql.Date.valueOf(LocalDate.now()));
        dto.setFechaFin(java.sql.Date.valueOf(LocalDate.now().plusDays(3)));
        dto.setEstado("PENDIENTE");
        dto.setNombre("Juan");
        dto.setApellido("Perez");
        dto.setHabitacionNumero(101);

        List<ReservaDTO> listaDto = Collections.singletonList(dto);
        Habitacion habitacion = new Habitacion() {
            @Override
            public float getCostoPorNoche() {
                return 100.0f;
            }
        };
        habitacion.setNumero(101);

        when(habitacionDAO.findById(101)).thenReturn(Optional.of(habitacion));
        when(reservaDAO.saveAll(anyList())).thenAnswer(invocation -> {
            List<Reserva> reservas = invocation.getArgument(0);
            List<ReservaDTO> dtos = new ArrayList<>();
            for (Reserva r : reservas) {
                ReservaDTO d = new ReservaDTO();
                d.setIdReserva(r.getIdReserva());
                d.setFechaInicio(r.getFechaInicio());
                d.setFechaFin(r.getFechaFin());
                d.setEstado(r.getEstado());
                d.setNombre(r.getNombre());
                d.setApellido(r.getApellido());
                d.setTelefono(r.getTelefono());
                if (r.getHabitacion() != null) {
                    d.setHabitacionNumero(r.getHabitacion().getNumero());
                }
                dtos.add(d);
            }
            return dtos;
        });

        // Act
        List<ReservaDTO> resultado = gestorDeReservas.crearReserva(listaDto);

        // Assert
        assertNotNull(resultado);
        assertEquals(1, resultado.size());
        assertEquals("JUAN", resultado.get(0).getNombre());
        assertEquals(habitacion.getNumero(), resultado.get(0).getHabitacionNumero());
        verify(habitacionDAO).findById(101);
        verify(reservaDAO).saveAll(anyList());
    }

    @Test
    public void testCrearReservaListaNula() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorDeReservas.crearReserva(null);
        });
        assertEquals("La lista de reservas no puede ser nula o vacía.", exception.getMessage());
    }

    @Test
    public void testCrearReservaListaVacia() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorDeReservas.crearReserva(new ArrayList<>());
        });
        assertEquals("La lista de reservas no puede ser nula o vacía.", exception.getMessage());
    }

    @Test
    public void testCrearReservaHabitacionNoEncontrada() {
        // Arrange
        ReservaDTO dto = new ReservaDTO();
        dto.setNombre("Ana");
        dto.setApellido("Lopez");
        dto.setHabitacionNumero(999);
        List<ReservaDTO> listaDto = Collections.singletonList(dto);

        when(habitacionDAO.findById(999)).thenReturn(Optional.empty());

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gestorDeReservas.crearReserva(listaDto);
        });
        assertEquals("Habitación no encontrada con número: 999", exception.getMessage());
    }

    // --- Tests para cancelarReserva ---

    @Test
    public void testCancelarReservaExito() {
        // Arrange
        Integer idReserva = 1;
        Reserva reserva = new Reserva();
        reserva.setIdReserva(idReserva);
        reserva.setEstado("CONFIRMADA");

        when(reservaDAO.findById(idReserva)).thenReturn(Optional.of(reserva));

        // Act
        gestorDeReservas.cancelarReserva(idReserva);

        // Assert
        assertEquals("CANCELADA", reserva.getEstado());
        verify(reservaDAO).save(reserva);
    }

    @Test
    public void testCancelarReservaIdNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorDeReservas.cancelarReserva(null);
        });
        assertEquals("El ID de la reserva es obligatorio.", exception.getMessage());
    }

    @Test
    public void testCancelarReservaNoEncontrada() {
        when(reservaDAO.findById(1)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gestorDeReservas.cancelarReserva(1);
        });
        assertEquals("Reserva no encontrada con id: 1", exception.getMessage());
    }

    // --- Tests para cancelarReservas (Lista) ---

    @Test
    public void testCancelarReservasExito() {
        Reserva r1 = new Reserva();
        r1.setIdReserva(1);
        Reserva r2 = new Reserva();
        r2.setIdReserva(2);

        ReservaDTO r1Dto = new ReservaDTO();
        r1Dto.setIdReserva(1);
        ReservaDTO r2Dto = new ReservaDTO();
        r2Dto.setIdReserva(2);
        List<ReservaDTO> lista = Arrays.asList(r1Dto, r2Dto);

        when(reservaDAO.findById(1)).thenReturn(Optional.of(r1));
        when(reservaDAO.findById(2)).thenReturn(Optional.of(r2));

        gestorDeReservas.cancelarReservas(lista);

        verify(reservaDAO).save(r1);
        verify(reservaDAO).save(r2);
    }

    @Test
    public void testCancelarReservasListaNula() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorDeReservas.cancelarReservas(null);
        });
        assertEquals("La lista de reservas a cancelar no puede ser nula o vacía.", exception.getMessage());
    }

    // --- Tests para buscarReservas ---

    @Test
    public void testBuscarReservasApellidoNulo() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorDeReservas.buscarReservas("Juan", null);
        });
        assertEquals("El apellido es obligatorio.", exception.getMessage());
    }

    @Test
    public void testBuscarReservasApellidoVacio() {
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            gestorDeReservas.buscarReservas("Juan", "   ");
        });
        assertEquals("El apellido es obligatorio.", exception.getMessage());
    }

    @Test
    public void testBuscarReservasSoloApellido() {
        gestorDeReservas.buscarReservas(null, "Perez");
        verify(reservaDAO).findByApellidoContainingIgnoreCase("Perez");
    }

    @Test
    public void testBuscarReservasNombreYApellido() {
        gestorDeReservas.buscarReservas("Juan", "Perez");
        verify(reservaDAO).findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase("Juan", "Perez");
    }
}
