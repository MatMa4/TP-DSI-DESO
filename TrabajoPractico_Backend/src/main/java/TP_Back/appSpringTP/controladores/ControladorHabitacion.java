package TP_Back.appSpringTP.controladores;

import TP_Back.appSpringTP.DAOs.HabitacionDAO;
import TP_Back.appSpringTP.DTOs.HabitacionDetalleDTO;
import TP_Back.appSpringTP.gestores.GestorDeHabitaciones;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/habitaciones")
public class ControladorHabitacion {

    @Autowired
    private GestorDeHabitaciones gestorHabitaciones;

    @GetMapping
    public ResponseEntity<List<HabitacionDetalleDTO>> getHabitacionesDisponibles(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaInicio,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") Date fechaFin) {
        
        List<HabitacionDetalleDTO> resultado = gestorHabitaciones.getHabitacionesConDetalle(fechaInicio, fechaFin);
        return ResponseEntity.ok(resultado);
    }
}
