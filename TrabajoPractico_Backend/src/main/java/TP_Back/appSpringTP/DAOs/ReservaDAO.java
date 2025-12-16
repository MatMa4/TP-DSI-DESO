package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.DTOs.ReservaDTO;
import TP_Back.appSpringTP.modelo.reserva.Reserva;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public interface ReservaDAO{
    Optional<Reserva> findById(Integer idReserva);
    ReservaDTO save(Reserva reserva);
    List<ReservaDTO> saveAll(List<Reserva> reservas);
    List<ReservaDTO> findAll();
    List<ReservaDTO> findByApellidoContainingIgnoreCase(String apellido);
    List<ReservaDTO> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);
}
