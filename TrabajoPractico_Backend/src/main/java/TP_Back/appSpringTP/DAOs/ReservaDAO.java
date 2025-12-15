package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.modelo.reserva.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface ReservaDAO extends JpaRepository<Reserva, Integer> {
    List<Reserva> findByApellidoContainingIgnoreCase(String apellido);
    List<Reserva> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido);
}
