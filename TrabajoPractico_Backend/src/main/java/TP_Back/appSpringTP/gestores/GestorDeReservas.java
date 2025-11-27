package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.HabitacionDAO;
import TP_Back.appSpringTP.DAOs.ReservaDAO;
import TP_Back.appSpringTP.DTOs.ReservaDTO;
import TP_Back.appSpringTP.modelo.habitacion.Habitacion;
import TP_Back.appSpringTP.modelo.reserva.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GestorDeReservas {

    @Autowired
    private ReservaDAO reservaDAO;

    @Autowired
    private HabitacionDAO habitacionDAO;

    public Reserva crearReserva(ReservaDTO reservaDTO) {
        Reserva reserva = new Reserva();
        reserva.setFechaInicio(reservaDTO.getFechaInicio());
        reserva.setFechaFin(reservaDTO.getFechaFin());
        reserva.setEstado("PENDIENTE"); // Default state
        reserva.setNombre(reservaDTO.getNombre());
        reserva.setApellido(reservaDTO.getApellido());
        reserva.setTelefono(reservaDTO.getTelefono());

        if (reservaDTO.getHabitacionNumero() != null) {
            Optional<Habitacion> habitacion = habitacionDAO.findById(reservaDTO.getHabitacionNumero());
            habitacion.ifPresent(reserva::setHabitacion);
        }

        return reservaDAO.save(reserva);
    }

    public void cancelarReserva(Integer idReserva) {
        reservaDAO.deleteById(idReserva);
    }

    public List<Reserva> buscarReservas() {
        return reservaDAO.findAll();
    }
}
