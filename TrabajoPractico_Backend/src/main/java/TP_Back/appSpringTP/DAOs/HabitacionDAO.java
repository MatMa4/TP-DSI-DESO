/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import org.springframework.stereotype.Service;
import TP_Back.appSpringTP.DTOs.HabitacionDetalleDTO;
import java.util.Date;
import java.util.List;
import TP_Back.appSpringTP.modelo.habitacion.Habitacion;
import java.util.Optional;

/**
 *
 * @author mateo
 */
@Service
public interface HabitacionDAO {
    public Optional<Habitacion> findById(Integer numero);
    List<HabitacionDetalleDTO> getHabitacionesConDetalle(Date fechaInicio, Date fechaFin);
}