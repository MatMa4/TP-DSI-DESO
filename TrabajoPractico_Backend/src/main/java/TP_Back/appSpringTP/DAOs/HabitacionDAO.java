/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import org.springframework.stereotype.Service;
import TP_Back.appSpringTP.DTOs.HabitacionDetalleDTO;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mateo
 */
@Service
public interface HabitacionDAO {
    List<HabitacionDetalleDTO> getHabitacionesConDetalle(Date fechaInicio, Date fechaFin);
}