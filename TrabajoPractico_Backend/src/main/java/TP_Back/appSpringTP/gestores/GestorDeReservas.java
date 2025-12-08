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

    public Reserva crearReserva(ReservaDTO dto) {
        Reserva reserva = new Reserva();
        
        reserva.setFechaInicio(dto.getFechaInicio());
        reserva.setFechaFin(dto.getFechaFin());
        reserva.setEstado(dto.getEstado());
        reserva.setNombre(dto.getNombre());
        reserva.setApellido(dto.getApellido());
        reserva.setTelefono(dto.getTelefono());

        if (dto.getHabitacionNumero() != null) {
            Habitacion habitacion = habitacionDAO.findById(dto.getHabitacionNumero())
                    .orElseThrow(() -> new RuntimeException("Habitación no encontrada con número: " + dto.getHabitacionNumero()));
            reserva.setHabitacion(habitacion);
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
