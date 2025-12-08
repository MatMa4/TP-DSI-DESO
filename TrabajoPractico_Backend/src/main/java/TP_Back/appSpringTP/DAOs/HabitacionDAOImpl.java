package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.DTOs.HabitacionDetalleDTO;
import TP_Back.appSpringTP.modelo.habitacion.Habitacion;
import TP_Back.appSpringTP.modelo.ocupacion.Ocupacion;
import TP_Back.appSpringTP.modelo.reserva.Reserva;
import TP_Back.appSpringTP.repositorios.repositorioHabitacion;
import TP_Back.appSpringTP.repositorios.repositorioOcupacion;
import TP_Back.appSpringTP.repositorios.repositorioReserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import TP_Back.appSpringTP.mappers.HabitacionMapper;
import TP_Back.appSpringTP.DTOs.HabitacionDTO;
import TP_Back.appSpringTP.DTOs.ocupacion.OcupacionDTO;
import TP_Back.appSpringTP.DTOs.ReservaDTO;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class HabitacionDAOImpl implements HabitacionDAO {

    @Autowired
    private repositorioHabitacion repoHabitacion;
    
    @Autowired
    private repositorioOcupacion repoOcupacion;
    
    @Autowired
    private repositorioReserva repoReserva;
    
    @Autowired
    private HabitacionMapper habitacionMapper;

    @Override
    public Optional<Habitacion> findById(Integer numero) {
        return repoHabitacion.findById(numero);
    }

    @Override
    public List<HabitacionDetalleDTO> getHabitacionesConDetalle(Date fechaInicio, Date fechaFin) {
        // 1. Traer habitaciones
        List<Habitacion> habitaciones = repoHabitacion.findByEstado("HABITABLE");
        
        if (habitaciones.isEmpty()) {
            return new ArrayList<>();
        }

        // 2. Traer Reservas y Ocupaciones (Batch Fetching)
        List<Reserva> reservas = repoReserva.buscarPorListaHabitaciones(habitaciones, fechaInicio, fechaFin);
        List<Ocupacion> ocupaciones = repoOcupacion.buscarPorListaHabitaciones(habitaciones, fechaInicio, fechaFin);

        // 3. Armar DTOs en memoria con Mapeo y Truncado
        List<HabitacionDetalleDTO> resultado = new ArrayList<>();
        
        for (Habitacion h : habitaciones) {
            
            // Mapeamos Habitacion -> HabitacionDTO
            HabitacionDTO hDTO = habitacionMapper.toDTO(h);

            // Filtramos y Mapeamos Reservas -> ReservaDTO (con truncado)
            List<ReservaDTO> resDTOs = reservas.stream()
                .filter(r -> r.getHabitacion().getNumero().equals(h.getNumero()))
                .map(r -> mapReservaDTO(r, fechaInicio, fechaFin))
                .collect(Collectors.toList());
                
            // Filtramos y Mapeamos Ocupaciones -> OcupacionDTO (con truncado)
            List<OcupacionDTO> ocuDTOs = ocupaciones.stream()
                .filter(o -> o.getHabitacion().getNumero().equals(h.getNumero()))
                .map(o -> mapOcupacionDTO(o, fechaInicio, fechaFin))
                .collect(Collectors.toList());
            
            resultado.add(new HabitacionDetalleDTO(hDTO, ocuDTOs, resDTOs));
        }
        
        return resultado;
    }

    private ReservaDTO mapReservaDTO(Reserva r, Date rangoInicio, Date rangoFin) {
        ReservaDTO dto = new ReservaDTO();
        dto.setIdReserva(r.getIdReserva());
        // Truncado de fechas (Math.max para inicio, Math.min para fin)
        dto.setFechaInicio(r.getFechaInicio().before(rangoInicio) ? rangoInicio : r.getFechaInicio());
        dto.setFechaFin(r.getFechaFin().after(rangoFin) ? rangoFin : r.getFechaFin());
        
        dto.setEstado(r.getEstado());
        dto.setNombre(r.getNombre());
        dto.setApellido(r.getApellido());
        dto.setTelefono(r.getTelefono());
        dto.setHabitacionNumero(r.getHabitacion().getNumero());
        return dto;
    }

    private OcupacionDTO mapOcupacionDTO(Ocupacion o, Date rangoInicio, Date rangoFin) {
        OcupacionDTO dto = new OcupacionDTO();
        // Nota: OcupacionDTO requiere HabitacionDTO, pero aquí para evitar ciclos o complejidad extra 
        // podríamos ponerle null o mapear la habitación "light". Por performance/diseño, a veces se deja null 
        // si ya está dentro de una HabitacionDetalleDTO. Asignamos null por ahora para evitar re-mapear recursivamente.
        dto.setHabitacion(null); 
        
        // Truncado de fechas
        dto.setFechaInicio(o.getFechaInicio().before(rangoInicio) ? rangoInicio : o.getFechaInicio());
        dto.setFechaFin(o.getFechaFin().after(rangoFin) ? rangoFin : o.getFechaFin());
        
        dto.setCheckIn(o.getCheckIn());
        dto.setCheckOut(o.getCheckOut());
        // Mapeo simple de listas vacías o lo que corresponda (aquí null o vacía si no se traen consumos eager)
        dto.setConsumos(new ArrayList<>()); 
        dto.setHuespedes(new ArrayList<>()); 
        
        return dto;
    }
}
