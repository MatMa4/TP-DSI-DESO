package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.FacturaDAO;
import TP_Back.appSpringTP.DAOs.NotaDeCreditoDAO;
import TP_Back.appSpringTP.DTOs.FacturaDTO;
import TP_Back.appSpringTP.DTOs.NotaDeCreditoDTO;
import TP_Back.appSpringTP.modelo.factura.Factura;
import TP_Back.appSpringTP.modelo.factura.NotaDeCredito;
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

    public Factura generarFactura(FacturaDTO facturaDTO) {
        Factura factura = new Factura();
        factura.setFechaEmision(facturaDTO.getFechaEmision());
        factura.setTipo(facturaDTO.getTipo());
        factura.setMontoTotal(facturaDTO.getMontoTotal());
        factura.setIva(facturaDTO.getIva());
        factura.setEstado(facturaDTO.getEstado());
        // Note: Ocupacion, Pago, and ResponsableDePago setting logic would go here,
        // fetching them from their respective DAOs using the IDs from DTO.
        
        return facturaDAO.save(factura);
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
