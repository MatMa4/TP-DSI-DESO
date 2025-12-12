/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.ConsumoDAO;
import TP_Back.appSpringTP.DAOs.OcupacionDAO;
import TP_Back.appSpringTP.DTOs.ocupacion.OcupacionDTO;
import TP_Back.appSpringTP.mappers.HabitacionMapper;
import TP_Back.appSpringTP.mappers.HuespedMapper;
import TP_Back.appSpringTP.modelo.ocupacion.Ocupacion;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mateo
 */
@Service
@AllArgsConstructor
public class GestorDeOcupaciones {
    @Autowired
    private final OcupacionDAO ocupacionDAO;
    @Autowired
    private final ConsumoDAO consumoDAO;
    @Autowired
    private final HabitacionMapper habitacionMapper;
    @Autowired
    private final HuespedMapper huespedMapper;
    
    @Autowired
    private final TP_Back.appSpringTP.mappers.OcupacionMapper ocupacionMapper;

    public void crearOcupacion(OcupacionDTO ocupacion){
        Ocupacion ocupacionNueva = new Ocupacion();
        ocupacionNueva.setHabitacion(habitacionMapper.toEntity(ocupacion.getHabitacion()));
        ocupacionNueva.setFechaInicio(ocupacion.getFechaInicio());
        ocupacionNueva.setFechaFin(ocupacion.getFechaFin());
        ocupacionNueva.setCheckIn(ocupacion.getCheckIn());
        ocupacionNueva.setCheckOut(ocupacion.getCheckOut());
        ocupacionNueva.setHuespedes(huespedMapper.toEntityList(ocupacion.getHuespedes()));
        ocupacionDAO.crearOcupacion(ocupacionNueva);
    }

    public OcupacionDTO obtenerOcupacionActual(int numeroHabitacion, java.time.LocalTime hora) {
        // Assume 'today' is the current date
        java.util.Date fechaActual = new java.util.Date();
        Ocupacion ocupacion = ocupacionDAO.getOcupacionPorHabitacionYFecha(numeroHabitacion, fechaActual);
        
        if (ocupacion != null) {
            OcupacionDTO dto = ocupacionMapper.toDTO(ocupacion);
            
            // Calculate total price
            long diffInMillies = Math.abs(ocupacion.getFechaFin().getTime() - ocupacion.getFechaInicio().getTime());
            long diff = java.util.concurrent.TimeUnit.DAYS.convert(diffInMillies, java.util.concurrent.TimeUnit.MILLISECONDS);
            
            double costoEstadia = diff * dto.getHabitacion().getCostoPorNoche();
            
            double totalConsumos = 0;
            if (dto.getConsumos() != null) {
                totalConsumos = dto.getConsumos().stream()
                        .mapToDouble(TP_Back.appSpringTP.DTOs.ocupacion.ConsumoDTO::getMonto)
                        .sum();
            }
            
            dto.setPrecioTotal(costoEstadia + totalConsumos);
            
            return dto;
        }
        return null;
    }
}
