package TP_Back.appSpringTP.controladores;

import TP_Back.appSpringTP.DTOs.ReservaDTO;
import TP_Back.appSpringTP.gestores.GestorDeReservas;
import TP_Back.appSpringTP.modelo.reserva.Reserva;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas") 
public class ControladorReserva {

    @Autowired
    private GestorDeReservas gestorDeReservas;

    @PostMapping 
    public ResponseEntity<?> crearReserva(@RequestBody ReservaDTO reservaDTO) {
        try {
            Reserva nuevaReserva = gestorDeReservas.crearReserva(reservaDTO);
            return ResponseEntity.ok(nuevaReserva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}