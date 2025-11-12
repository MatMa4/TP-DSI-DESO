/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.controladores;

import TP_Back.appSpringTP.DTOs.HuespedDTO;
import TP_Back.appSpringTP.excepciones.HuespedExistenteException;
import TP_Back.appSpringTP.gestores.GestorHuespedes;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/huespedes")
public class ControladorHuesped {

    private final GestorHuespedes gestorHuespedes;

    public ControladorHuesped(GestorHuespedes gestorHuespedes) {
        this.gestorHuespedes = gestorHuespedes;
    }
    
    @PostMapping
    public ResponseEntity<?> agregarHuesped(@RequestBody HuespedDTO huesped,
                                            @RequestParam(defaultValue = "false") Boolean forzar) {
        try {
            HuespedDTO guardado = gestorHuespedes.guardarHuesped(huesped, forzar);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);
        } catch (HuespedExistenteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "CONFLICTO", "mensaje", e.getMessage()));
        }
    }

    
    @GetMapping
    public List<HuespedDTO> obtenerTodos() {
        return gestorHuespedes.obtenerTodos();
    }
}
