/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.HabitacionDAO;
import TP_Back.appSpringTP.DTOs.HabitacionDetalleDTO;
import java.util.Date;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mateo
 */
@Service
@AllArgsConstructor
public class GestorDeHabitaciones {
    @Autowired
    private final HabitacionDAO habitacionDAO;
    
    public List<HabitacionDetalleDTO> getHabitacionesConDetalle(Date fechaInicio, Date fechaFin){
        return habitacionDAO.getHabitacionesConDetalle(fechaInicio, fechaFin);
    }
}
