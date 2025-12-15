package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.modelo.reserva.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservaDAO extends JpaRepository<Reserva, Integer> {
}
