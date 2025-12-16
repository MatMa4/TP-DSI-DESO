/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.controladores;
import TP_Back.appSpringTP.DTOs.HuespedDTO;
import TP_Back.appSpringTP.excepciones.HuespedExistenteException;
import TP_Back.appSpringTP.excepciones.HuespedNoEliminableException;
import TP_Back.appSpringTP.excepciones.HuespedNoEncontradoException;
import TP_Back.appSpringTP.gestores.GestorHuespedes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/huespedes")
@AllArgsConstructor
public class ControladorHuesped {

    private final GestorHuespedes gestorHuespedes;
    
    @PutMapping
    public ResponseEntity<?> registrarHuesped(@RequestBody HuespedDTO huesped) {
        return ResponseEntity.ok(gestorHuespedes.registrarHuesped(huesped));
    }
    
    @PostMapping
    public ResponseEntity<?> modificarHuesped(@RequestBody List<HuespedDTO> huespedes) {
        return ResponseEntity.ok(gestorHuespedes.modificarHuesped(huespedes));
    }
    
    @DeleteMapping
    public ResponseEntity<?> eliminarHuesped(@RequestBody HuespedDTO huesped){
        try{
            gestorHuespedes.eliminarHuesped(huesped);
        } catch (DataIntegrityViolationException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                                .body("No se puede eliminar el huésped porque existen facturas asociadas.");
       } catch (HuespedNoEncontradoException e) {
           return ResponseEntity.status(HttpStatus.NOT_FOUND)
                                .body("Huésped no encontrado.");
       } catch (HuespedNoEliminableException e) {
           return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                                .body("El huésped está alojado actualmente y no puede eliminarse.");
       }

        return ResponseEntity.ok(true);
    }
    
    @GetMapping("/buscar")
    public ResponseEntity<List<HuespedDTO>> buscarHuespedes(
            @RequestParam(required = false) String nombre,
            @RequestParam(required = false) String apellido,
            @RequestParam(required = false) String tipo,
            @RequestParam(required = false) String numero) {
        
        try{
            return ResponseEntity.ok(gestorHuespedes.buscarHuesped(tipo, numero, nombre, apellido));
        }catch(HuespedNoEncontradoException e){
            return ResponseEntity.ok(new ArrayList<>());
        }

        
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
    @GetMapping("/huespedExistente")
    public ResponseEntity<?> huespedExistente(@RequestParam String tipoModificado, @RequestParam String numeroModificado, @RequestParam String tipoOriginal, @RequestParam String numeroOriginal) {
        try {
            gestorHuespedes.huespedExistente(tipoModificado, numeroModificado, tipoOriginal, numeroOriginal);
            return ResponseEntity.status(HttpStatus.OK).body("Huesped no existe");
        } catch (HuespedExistenteException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(Map.of("error", "CONFLICTO", "mensaje", e.getMessage()));
        }
    }

    @GetMapping("/listartodos")
    public ResponseEntity<List<HuespedDTO>> listarTodos() {
        return ResponseEntity.ok(gestorHuespedes.listarTodosHuespedes());
    }
}
