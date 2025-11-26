/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.DTOs.HabitacionDTO;
import TP_Back.appSpringTP.DTOs.HuespedDTO;
import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author mateo
 */
@Service
public class OcupacionDAOImpl implements OcupacionDAO{
    @Override
    public void crearOcupacion(List<HuespedDTO> listaHuespedes, Date f_inicio, Date f_fin, HabitacionDTO habitacion){
        
    }
}
