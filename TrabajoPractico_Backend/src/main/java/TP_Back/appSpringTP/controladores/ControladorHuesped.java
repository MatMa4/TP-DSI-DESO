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
    public ResponseEntity<?> agregarHuesped(@RequestBody HuespedDTO huesped,
                                            @RequestParam(defaultValue = "false") Boolean forzar) {
        try {
            gestorHuespedes.registrarHuesped(huesped, forzar);
            return ResponseEntity.status(HttpStatus.CREATED).body(huesped.getNumeroDocumento());
        } catch (HuespedExistenteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "CONFLICTO", "mensaje", e.getMessage()));
        }
    }

    @GetMapping
    public List<HuespedDTO> obtenerTodos() {
        return gestorHuespedes.obtenerTodos();
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<HuespedDTO>> buscarHuespedes(@RequestBody HuespedDTO huesped) {

        return ResponseEntity.ok(gestorHuespedes.buscarHuesped(huesped));
    }
    @GetMapping("/obtener")
    public ResponseEntity<Huesped> obtenerHuespedes(@RequestBody HuespedDTO huesped) {
        System.out.println(huesped.getTipoDocumento());
        System.out.println(huesped.getNumeroDocumento());
        return ResponseEntity.ok(gestorHuespedes.obtenerHuesped(huesped));
    }
    @GetMapping("/consultarDocumento")
    public ResponseEntity<HuespedDTO> consultarDocumento(@RequestParam String tipo, @RequestParam String numero) {

        return ResponseEntity.ok(gestorHuespedes.consultarDocumento(tipo, numero));
    }
    @GetMapping("/direcciones")
    public ResponseEntity<List<Direccion>> obtenerDirecciones() {

        return ResponseEntity.ok(repoDir.findAll());
    }

}
