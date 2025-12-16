package TP_Back.appSpringTP.repositorios;

import TP_Back.appSpringTP.modelo.habitacion.Habitacion;
import TP_Back.appSpringTP.modelo.reserva.Reserva;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface repositorioReserva extends JpaRepository<Reserva, Integer> {

       @Query("SELECT r FROM Reserva r WHERE r.habitacion IN :habitaciones " +
                     "AND (r.fechaInicio <= :fechaFin AND r.fechaFin >= :fechaInicio) AND r.estado <> 'CANCELADA'")
       List<Reserva> buscarPorListaHabitaciones(@Param("habitaciones") List<Habitacion> habitaciones,
                     @Param("fechaInicio") Date fechaInicio,
                     @Param("fechaFin") Date fechaFin);
        @Query("SELECT r FROM Reserva r WHERE UPPER(r.apellido) LIKE UPPER(CONCAT('%', :apellido, '%')) AND r.estado <> 'CANCELADA'")
        List<Reserva> findByApellidoContainingIgnoreCase(@Param("apellido") String apellido);

        @Query("SELECT r FROM Reserva r WHERE (UPPER(r.nombre) LIKE UPPER(CONCAT('%', :nombre, '%')) OR UPPER(r.apellido) LIKE UPPER(CONCAT('%', :apellido, '%'))) AND r.estado <> 'CANCELADA'")
        List<Reserva> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(@Param("nombre") String nombre,
                @Param("apellido") String apellido);
}