/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.controladores;

import TP_Back.appSpringTP.DTOs.ocupacion.OcupacionDTO;
import TP_Back.appSpringTP.gestores.GestorDeOcupaciones;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestParam;
import TP_Back.appSpringTP.DTOs.ocupacion.SolicitudConsumoDTO;
import org.springframework.http.ResponseEntity;

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

    @GetMapping
    public ResponseEntity<OcupacionDTO> buscarOcupacion(@RequestParam int numero,
            @RequestParam java.time.LocalTime hora) {
        OcupacionDTO occupied = gestorOcupaciones.obtenerOcupacionActual(numero, hora);
        if (occupied != null) {
            return ResponseEntity.ok(occupied);
        }
        return ResponseEntity.notFound().build();
    }

    @PostMapping("/agregar_consumo")
    public ResponseEntity<?> agregarConsumo(@RequestBody SolicitudConsumoDTO solicitud) {
        try {
            gestorOcupaciones.agregarConsumo(solicitud);
            return ResponseEntity.ok("Consumo agregado correctamente");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
