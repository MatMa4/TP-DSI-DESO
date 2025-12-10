/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.controladores;

import TP_Back.appSpringTP.DTOs.HabitacionDTO;
import TP_Back.appSpringTP.DTOs.ocupacion.OcupacionDTO;
import TP_Back.appSpringTP.gestores.GestorDeOcupaciones;
import TP_Back.appSpringTP.mappers.HabitacionMapper;
import TP_Back.appSpringTP.modelo.ocupacion.Ocupacion;
import TP_Back.appSpringTP.repositorios.repositorioHabitacion;
import TP_Back.appSpringTP.repositorios.repositorioOcupacion;
import java.util.List;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mateo
 */
@RestController
@RequestMapping("/ocupacion")
@AllArgsConstructor
public class ControladorOcupacion {
        private final GestorDeOcupaciones gestorOcupaciones;
    
    @PostMapping
    public boolean crearOcupacion(@RequestBody OcupacionDTO ocupacion) {
        gestorOcupaciones.crearOcupacion(ocupacion);
        return true;
    }
}
