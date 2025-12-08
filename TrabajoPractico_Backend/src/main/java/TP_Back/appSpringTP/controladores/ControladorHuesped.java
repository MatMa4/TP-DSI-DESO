/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.controladores;

import TP_Back.appSpringTP.DTOs.HuespedDTO;
import TP_Back.appSpringTP.excepciones.HuespedExistenteException;
import TP_Back.appSpringTP.gestores.GestorHuespedes;
import TP_Back.appSpringTP.modelo.direccion.Direccion;
import TP_Back.appSpringTP.modelo.huesped.Huesped;
import TP_Back.appSpringTP.repositorios.repositorioDireccion;
import java.util.List;
import java.util.Map;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/huespedes")
public class ControladorHuesped {

    private final GestorHuespedes gestorHuespedes;
    
    private final repositorioDireccion repoDir;

    public ControladorHuesped(GestorHuespedes gestorHuespedes, repositorioDireccion repoDir) {
        this.gestorHuespedes = gestorHuespedes;
        this.repoDir = repoDir;
    }
    
    @PutMapping
    public ResponseEntity<?> registrarHuesped(@RequestBody HuespedDTO huesped) {
        return ResponseEntity.ok(gestorHuespedes.registrarHuesped(huesped));
    }

    @GetMapping
    public List<HuespedDTO> obtenerTodos() {
        return gestorHuespedes.obtenerTodos();
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<HuespedDTO>> buscarHuespedes(@RequestParam(defaultValue = "") String tipo, @RequestParam(defaultValue = "") String numero, @RequestParam(defaultValue = "") String nombre, @RequestParam(defaultValue = "") String apellido) {

        return ResponseEntity.ok(gestorHuespedes.buscarHuesped(tipo, numero, nombre, apellido));
    }
    @GetMapping("/obtener")
    public ResponseEntity<Huesped> obtenerHuespedes(@RequestParam String tipo, @RequestParam String numero) {
        return ResponseEntity.ok(gestorHuespedes.obtenerHuesped(tipo, numero));
    }
    @GetMapping("/consultarDocumento")
    public ResponseEntity<?> consultarDocumento(@RequestParam String tipo, @RequestParam String numero) {
        try {
            gestorHuespedes.consultarDocumento(tipo, numero);
            return ResponseEntity.status(HttpStatus.OK).body("Huesped no existe");
        } catch (HuespedExistenteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "CONFLICTO", "mensaje", e.getMessage()));
        }
    }
    @GetMapping("/direcciones")
    public ResponseEntity<List<Direccion>> obtenerDirecciones() {

        return ResponseEntity.ok(repoDir.findAll());
    }

}
