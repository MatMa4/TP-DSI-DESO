/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.controladores;

import TP_Back.appSpringTP.DTOs.pago.PersonaJuridicaDTO;
import TP_Back.appSpringTP.gestores.GestorResponsableDePago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.web.bind.annotation.RequestParam;

/**
 *
 * @author mateo
 */
@RestController
@RequestMapping("/responsable-pago")
public class ControladorResponsableDePago {

    @Autowired
    private GestorResponsableDePago gestorResponsableDePago;

    @PostMapping("/juridica")
    public ResponseEntity<?> registrarPersonaJuridica(@RequestBody PersonaJuridicaDTO personaJuridicaDTO) {
        try {
            gestorResponsableDePago.registrarPersonaJuridica(personaJuridicaDTO);
            return ResponseEntity.ok("Persona jurídica registrada exitosamente");
        } catch (Exception e) {
            e.printStackTrace(); // Log stack trace
            String causeMessage = e.getCause() != null ? e.getCause().getMessage() : e.getMessage();
            return ResponseEntity.internalServerError().body("Error al registrar persona jurídica: " + causeMessage);
        }
    }

    @GetMapping("/juridica")
    public ResponseEntity<?> getPersonaJuridica(@RequestParam String cuit) {
        try {
            PersonaJuridicaDTO dto = gestorResponsableDePago.buscarPersonaJuridica(cuit);
            return ResponseEntity.ok(dto);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(404).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Error al buscar persona jurídica: " + e.getMessage());
        }
    }
}
