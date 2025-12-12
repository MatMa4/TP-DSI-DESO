package TP_Back.appSpringTP.repositorios;

import TP_Back.appSpringTP.modelo.habitacion.Habitacion;
import TP_Back.appSpringTP.modelo.ocupacion.Ocupacion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Date;
import java.util.List;

public interface repositorioOcupacion extends JpaRepository<Ocupacion, Integer>{
    
    @Query("SELECT o FROM Ocupacion o WHERE o.habitacion IN :habitaciones " +
           "AND (o.fechaInicio <= :fechaFin AND o.fechaFin >= :fechaInicio)")
    List<Ocupacion> buscarPorListaHabitaciones(@Param("habitaciones") List<Habitacion> habitaciones, 
                                             @Param("fechaInicio") Date fechaInicio, 
                                             @Param("fechaFin") Date fechaFin);

    @Query("SELECT o FROM Ocupacion o WHERE o.habitacion.numero = :numero " +
           "AND :fecha BETWEEN o.fechaInicio AND o.fechaFin")
    List<Ocupacion> findOcupacionPorHabitacionYFecha(@Param("numero") int numero, 
                                                   @Param("fecha") Date fecha);
}
