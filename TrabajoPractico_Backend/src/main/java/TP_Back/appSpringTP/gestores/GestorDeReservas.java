package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.HabitacionDAO;
import TP_Back.appSpringTP.DAOs.ReservaDAO;
import TP_Back.appSpringTP.DTOs.ReservaDTO;
import TP_Back.appSpringTP.modelo.habitacion.Habitacion;
import TP_Back.appSpringTP.modelo.reserva.Reserva;
import java.util.ArrayList;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GestorDeReservas {

    @Autowired
    private ReservaDAO reservaDAO;

    @Autowired
    private HabitacionDAO habitacionDAO;

    public List<Reserva> crearReserva(List<ReservaDTO> listaDto) {
        List<Reserva> reservas = new ArrayList<>();

        for (ReservaDTO dto : listaDto) {
            Reserva reserva = new Reserva();
            reserva.setFechaInicio(dto.getFechaInicio());
            reserva.setFechaFin(dto.getFechaFin());
            reserva.setEstado(dto.getEstado());
            reserva.setNombre(dto.getNombre().toUpperCase());
            reserva.setApellido(dto.getApellido().toUpperCase());
            reserva.setTelefono(dto.getTelefono());

            if (dto.getHabitacionNumero() != null) {
                Habitacion habitacion = habitacionDAO.findById(dto.getHabitacionNumero())
                        .orElseThrow(() -> new RuntimeException("Habitación no encontrada con número: " + dto.getHabitacionNumero()));
                reserva.setHabitacion(habitacion);
            }

            reservas.add(reserva);
        }

        return reservaDAO.saveAll(reservas);
    }


    public void cancelarReserva(Integer idReserva) {
        reservaDAO.deleteById(idReserva);
    }

    public List<Reserva> buscarReservas() {
        return reservaDAO.findAll();
    }

    public List<Reserva> buscarReservas(String nombre, String apellido) {
        if (apellido == null || apellido.trim().isEmpty()) {
            throw new IllegalArgumentException("El apellido es obligatorio.");
        }
        
        if (nombre == null || nombre.trim().isEmpty()) {
            return reservaDAO.findByApellidoContainingIgnoreCase(apellido);
        } else {
            return reservaDAO.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(nombre, apellido);
        }
    }
}
