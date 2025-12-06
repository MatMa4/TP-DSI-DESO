/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.DTOs.ocupacion.OcupacionDTO;
import TP_Back.appSpringTP.mappers.HabitacionMapper;
import TP_Back.appSpringTP.mappers.OcupacionMapper;
import TP_Back.appSpringTP.modelo.ocupacion.Ocupacion;
import TP_Back.appSpringTP.repositorios.repositorioOcupacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mateo
 */
@Service
public class OcupacionDAOImpl implements OcupacionDAO{
    @Autowired
    private OcupacionMapper ocupacionMapper;
    @Autowired
    private repositorioOcupacion repoOcupacion;
    @Autowired
    private HabitacionMapper habitacionMapper;
    @Override
    public void crearOcupacion(OcupacionDTO ocupDTO){
        Ocupacion ocupacion = ocupacionMapper.toEntity(ocupDTO);
        ocupacion.setHabitacion(habitacionMapper.toEntity(ocupDTO.getHabitacion()));
        repoOcupacion.save(ocupacion);
    }
}
