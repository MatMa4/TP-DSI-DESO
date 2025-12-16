/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.DTOs.ReservaDTO;
import TP_Back.appSpringTP.modelo.habitacion.DobleEstandar;
import TP_Back.appSpringTP.modelo.habitacion.DobleSuperior;
import TP_Back.appSpringTP.modelo.habitacion.IndividualEstandar;
import TP_Back.appSpringTP.modelo.habitacion.Suite;
import TP_Back.appSpringTP.modelo.habitacion.SuperiorFamilyPlan;
import TP_Back.appSpringTP.modelo.reserva.Reserva;
import TP_Back.appSpringTP.repositorios.repositorioReserva;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mateo
 */
@Service
public class ReservaDAOImpl implements ReservaDAO{
    @Autowired
    private repositorioReserva repoReserva;
    
    @Override
    public Optional<Reserva> findById(Integer idReserva){
        return repoReserva.findById(idReserva);
    }
    
    @Override
    public ReservaDTO save (Reserva reserva){
        return toDTO(reserva);
    }
    
    @Override
    public List<ReservaDTO> saveAll(List<Reserva> reservas){
        return toDTOList(repoReserva.saveAll(reservas));
    }
    
    @Override
    public List<ReservaDTO> findAll(){
        return toDTOList(repoReserva.findAll());
        
    }
    @Override
    public List<ReservaDTO> findByApellidoContainingIgnoreCase(String apellido){
        return toDTOList(repoReserva.findByApellidoContainingIgnoreCase(apellido));
    }
    @Override
    public List<ReservaDTO> findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(String nombre, String apellido){
        return toDTOList(repoReserva.findByNombreContainingIgnoreCaseOrApellidoContainingIgnoreCase(nombre, apellido));
    }

    public ReservaDTO toDTO(Reserva reserva) {
        ReservaDTO dto = new ReservaDTO();
        dto.setIdReserva(reserva.getIdReserva());
        dto.setFechaInicio(reserva.getFechaInicio());
        dto.setFechaFin(reserva.getFechaFin());
        dto.setEstado(reserva.getEstado());
        dto.setNombre(reserva.getNombre());
        dto.setApellido(reserva.getApellido());
        dto.setTelefono(reserva.getTelefono());

        if (reserva.getHabitacion() != null) {
            dto.setHabitacionNumero(reserva.getHabitacion().getNumero());
            if (reserva.getHabitacion() instanceof Suite h) {
                dto.setTipoHabitacion("Suite");
            } else if (reserva.getHabitacion() instanceof DobleEstandar h) {
                dto.setTipoHabitacion("DobleEstándar");
            } else if (reserva.getHabitacion() instanceof DobleSuperior h) {
                dto.setTipoHabitacion("DobleSuperior");
            } else if (reserva.getHabitacion() instanceof IndividualEstandar h) {
                dto.setTipoHabitacion("IndividualEstándar");
            } else if (reserva.getHabitacion() instanceof SuperiorFamilyPlan h) {
                dto.setTipoHabitacion("SuperiorFamilyPlan");
            }
        }

        return dto;
    }
    
    public List<ReservaDTO> toDTOList(List<Reserva> reservas) {
        return reservas.stream()
                       .map(this::toDTO)
                       .collect(Collectors.toList());
    }


}
