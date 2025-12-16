package TP_Back.appSpringTP.controladores;

import TP_Back.appSpringTP.DTOs.ReservaDTO;
import TP_Back.appSpringTP.gestores.GestorDeReservas;
import TP_Back.appSpringTP.modelo.reserva.Reserva;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservas")
public class ControladorReserva {

    @Autowired
    private GestorDeReservas gestorDeReservas;

    @PostMapping
    public ResponseEntity<?> crearReserva(@RequestBody List<ReservaDTO> reservaDTO) {
        try {
            List<ReservaDTO> nuevaReserva = gestorDeReservas.crearReserva(reservaDTO);
            return ResponseEntity.ok(nuevaReserva);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PutMapping("/cancelar")
    public ResponseEntity<?> cancelarReserva(@RequestBody List<ReservaDTO>reservas) {
        try {
            gestorDeReservas.cancelarReservas(reservas);
            return ResponseEntity.ok("Reservas canceladas exitosamente.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al cancelar las reservas: " + e.getMessage());
        }
    }

    @GetMapping("/buscar")
    public ResponseEntity<?> buscarReservas(
            @RequestParam(required = false) String nombre,
            @RequestParam String apellido) {
        try {
            List<ReservaDTO> reservas = gestorDeReservas.buscarReservas(nombre.toUpperCase(), apellido.toUpperCase());
            return ResponseEntity.ok(reservas);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al buscar reservas: " + e.getMessage());
        }
    }
}