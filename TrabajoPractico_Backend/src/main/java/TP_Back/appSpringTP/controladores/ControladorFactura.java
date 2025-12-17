package TP_Back.appSpringTP.controladores;

import TP_Back.appSpringTP.DTOs.SolicitudFacturacionDTO;
import TP_Back.appSpringTP.gestores.GestorDeFacturas;
import TP_Back.appSpringTP.modelo.factura.Factura;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import TP_Back.appSpringTP.DTOs.SolicitudFacturacionJuridicaDTO;

@RestController
@RequestMapping("/facturas")
public class ControladorFactura {

    @Autowired
    private GestorDeFacturas gestorDeFacturas;

    @PostMapping("/generar/fisica")
    public ResponseEntity<?> generarFacturaFisica(@RequestBody SolicitudFacturacionDTO solicitud) {
        try {
            Factura factura = gestorDeFacturas.generarFacturaFisica(solicitud);
            return ResponseEntity.ok(factura.getNumero());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @PostMapping("/generar/juridica")
    public ResponseEntity<?> generarFacturaJuridica(@RequestBody SolicitudFacturacionJuridicaDTO solicitud) {
        try {
            Factura factura = gestorDeFacturas.generarFacturaJuridica(solicitud);
            return ResponseEntity.ok(factura.getNumero());
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}
