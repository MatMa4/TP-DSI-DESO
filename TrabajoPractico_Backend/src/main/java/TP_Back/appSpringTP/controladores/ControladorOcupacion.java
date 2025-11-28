/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.controladores;

import TP_Back.appSpringTP.DTOs.HuespedDTO;
import TP_Back.appSpringTP.DTOs.ocupacion.OcupacionDTO;
import TP_Back.appSpringTP.excepciones.HuespedExistenteException;
import TP_Back.appSpringTP.gestores.GestorDeOcupaciones;
import TP_Back.appSpringTP.gestores.GestorHuespedes;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author mateo
 */
@RestController
@RequestMapping("/ocupacion")
public class ControladorOcupacion {
        private final GestorDeOcupaciones gestorOcupaciones;

    public ControladorOcupacion(GestorDeOcupaciones gestorOcupaciones) {
        this.gestorOcupaciones = gestorOcupaciones;
    }
    
    @PutMapping
    public boolean crearOcupacion(@RequestBody OcupacionDTO ocupacion,
                                            @RequestParam(defaultValue = "false") Boolean forzar) {
        gestorOcupaciones.crearOcupacion(ocupacion);
        return true;
    }
    
}
