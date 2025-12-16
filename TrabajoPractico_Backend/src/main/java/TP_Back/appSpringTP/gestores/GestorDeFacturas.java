package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.FacturaDAO;
import TP_Back.appSpringTP.DAOs.NotaDeCreditoDAO;
import TP_Back.appSpringTP.DTOs.NotaDeCreditoDTO;
import TP_Back.appSpringTP.modelo.factura.Factura;
import TP_Back.appSpringTP.modelo.factura.NotaDeCredito;
import TP_Back.appSpringTP.gestores.factories.FacturaFisicaFactory;
import TP_Back.appSpringTP.gestores.factories.FacturaJuridicaFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GestorDeFacturas {

    @Autowired
    private FacturaDAO facturaDAO;

    @Autowired
    private NotaDeCreditoDAO notaDeCreditoDAO;

    @Autowired
    private FacturaFisicaFactory facturaFisicaFactory;

    @Autowired
    private FacturaJuridicaFactory facturaJuridicaFactory;

    public Factura generarFacturaFisica(TP_Back.appSpringTP.DTOs.SolicitudFacturacionDTO solicitud) {
        if (solicitud.getHuesped() == null) {
            throw new RuntimeException("El huesped responsable de pago es obligatorio para Factura Física.");
        }

        return facturaFisicaFactory.crearFactura(
                solicitud.getIdOcupacion(),
                solicitud.getListaConsumos(),
                solicitud.getHuesped());
    }

    public Factura generarFacturaJuridica(TP_Back.appSpringTP.DTOs.SolicitudFacturacionJuridicaDTO solicitud) {
        if (solicitud.getCuitResponsable() == null || solicitud.getCuitResponsable().isEmpty()) {
            throw new RuntimeException("El CUIT del responsable de pago es obligatorio para Factura Jurídica.");
        }

        return facturaJuridicaFactory.crearFactura(
                solicitud.getIdOcupacion(),
                solicitud.getListaConsumos(),
                solicitud.getCuitResponsable());
    }

    public NotaDeCredito ingresarNotaDeCredito(NotaDeCreditoDTO notaDTO) {
        NotaDeCredito nota = new NotaDeCredito();
        nota.setFecha(notaDTO.getFecha());
        nota.setImporte(notaDTO.getImporte());

        if (notaDTO.getFacturaNumero() != null) {
            Optional<Factura> factura = facturaDAO.findById(notaDTO.getFacturaNumero());
            factura.ifPresent(nota::setFactura);
        }

        return notaDeCreditoDAO.save(nota);
    }

    public List<Factura> listarFacturas() {
        return facturaDAO.findAll();
    }
}
