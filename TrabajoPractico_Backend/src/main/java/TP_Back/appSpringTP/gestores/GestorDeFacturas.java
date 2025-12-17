package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DTOs.SolicitudFacturacionJuridicaDTO;
import TP_Back.appSpringTP.modelo.factura.Factura;
import TP_Back.appSpringTP.gestores.factories.FacturaFisicaFactory;
import TP_Back.appSpringTP.gestores.factories.FacturaJuridicaFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import TP_Back.appSpringTP.DTOs.SolicitudFacturacionDTO;

@Service
public class GestorDeFacturas {

    @Autowired
    private FacturaFisicaFactory facturaFisicaFactory;

    @Autowired
    private FacturaJuridicaFactory facturaJuridicaFactory;

    public Factura generarFacturaFisica(SolicitudFacturacionDTO solicitud) {
        if (solicitud == null) {
            throw new RuntimeException("La solicitud no puede ser nula.");
        }
        if (solicitud.getIdOcupacion() == null) {
            throw new RuntimeException("El ID de ocupación es obligatorio.");
        }
        if (solicitud.getListaConsumos() == null) {
            throw new RuntimeException("La lista de consumos es obligatoria.");
        }
        if (solicitud.getHuesped() == null) {
            throw new RuntimeException("El huesped responsable de pago es obligatorio para Factura Física.");
        }

        return facturaFisicaFactory.crearFactura(
                solicitud.getIdOcupacion(),
                solicitud.getListaConsumos(),
                solicitud.getHuesped());
    }

    public Factura generarFacturaJuridica(SolicitudFacturacionJuridicaDTO solicitud) {
        if (solicitud == null) {
            throw new RuntimeException("La solicitud no puede ser nula.");
        }
        if (solicitud.getIdOcupacion() == null) {
            throw new RuntimeException("El ID de ocupación es obligatorio.");
        }
        if (solicitud.getListaConsumos() == null) {
            throw new RuntimeException("La lista de consumos es obligatoria.");
        }
        if (solicitud.getCuitResponsable() == null || solicitud.getCuitResponsable().isBlank()) {
            throw new RuntimeException("El CUIT del responsable de pago es obligatorio para Factura Jurídica.");
        }

        return facturaJuridicaFactory.crearFactura(
                solicitud.getIdOcupacion(),
                solicitud.getListaConsumos(),
                solicitud.getCuitResponsable());
    }
}
