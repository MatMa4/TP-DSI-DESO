package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.modelo.reserva.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

@Repository
public interface ReservaDAO extends JpaRepository<Reserva, Integer> {
    @Query("SELECT r FROM Reserva r WHERE UPPER(r.apellido) LIKE UPPER(CONCAT('%', :apellido, '%')) AND r.estado <> 'CANCELADA'")
    List<Reserva> findByApellidoContainingIgnoreCase(@Param("apellido") String apellido);

    @Query("SELECT r FROM Reserva r WHERE (UPPER(r.nombre) LIKE UPPER(CONCAT('%', :nombre, '%')) OR UPPER(r.apellido) LIKE UPPER(CONCAT('%', :apellido, '%'))) AND r.estado <> 'CANCELADA'")
    List<Reserva> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(@Param("nombre") String nombre,
            @Param("apellido") String apellido);
}
