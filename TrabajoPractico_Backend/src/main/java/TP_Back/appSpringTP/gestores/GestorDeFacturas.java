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

    @Autowired
    private TP_Back.appSpringTP.DAOs.OcupacionDAO ocupacionDAO;
    @Autowired
    private TP_Back.appSpringTP.DAOs.PersonaFisicaDAO personaFisicaDAO;
    @Autowired
    private TP_Back.appSpringTP.DAOs.ConsumoDAO consumoDAO;

    public Factura generarFactura(TP_Back.appSpringTP.DTOs.SolicitudFacturacionDTO solicitud) {
        // Validaciones
        if ((solicitud.getIdOcupacion() == null || solicitud.getIdOcupacion() == 0) && 
            (solicitud.getListaConsumos() == null || solicitud.getListaConsumos().isEmpty())) {
            throw new RuntimeException("Debe existir al menos un item para facturar (Ocupación o Consumos).");
        }

        if (solicitud.getHuesped() == null) {
             throw new RuntimeException("El huesped responsable de pago es obligatorio.");
        }

        // Buscar Ocupacion
        TP_Back.appSpringTP.modelo.ocupacion.Ocupacion ocupacion = null;
        if (solicitud.getIdOcupacion() != null && solicitud.getIdOcupacion() != 0) {
            ocupacion = ocupacionDAO.getOcupacionById(solicitud.getIdOcupacion().intValue());
            if (ocupacion == null) {
                 // Si se paso ID pero no existe, puede ser error. El requerimiento dice "Si el id es vacio... no se genera con ocupacion".
                 // Pero si viene ID y no existe, mejor fallar.
                 throw new RuntimeException("Ocupacion no encontrada con ID: " + solicitud.getIdOcupacion());
            }
        }

        // Buscar Responsable de Pago (PersonaFisica) asociado al Huesped
        java.util.Optional<TP_Back.appSpringTP.modelo.pago.PersonaFisica> responsableOpt = personaFisicaDAO.getByHuesped(
                solicitud.getHuesped().getTipoDocumento(), 
                solicitud.getHuesped().getNumeroDocumento());
        
        if (responsableOpt.isEmpty()) {
            throw new RuntimeException("No se encontró una Persona Física asociada al Huésped (Responsable de Pago). Valide que el Huésped esté registrado correctamente como cliente.");
        }
        
        TP_Back.appSpringTP.modelo.pago.PersonaFisica responsable = responsableOpt.get();

        // Crear Factura
        Factura factura = new Factura();
        factura.setFechaEmision(new java.util.Date());
        factura.setEstado("PENDIENTE_PAGO"); // Estado inicial
        factura.setResponsableDePago(responsable);
        
        if (ocupacion != null) {
            factura.setOcupacion(ocupacion);
        }
        
        // Calculo de monto total
        float total = 0;
        
        // Sumar ocupacion
        if (ocupacion != null) {
             long diffInMillies = Math.abs(ocupacion.getFechaFin().getTime() - ocupacion.getFechaInicio().getTime());
             long diff = java.util.concurrent.TimeUnit.DAYS.convert(diffInMillies, java.util.concurrent.TimeUnit.MILLISECONDS);
             total += diff * ocupacion.getHabitacion().getCostoPorNoche();
        }
        
        // Sumar consumos extra pasados en la lista y marcarlos como facturados
        if (solicitud.getListaConsumos() != null) {
            for (TP_Back.appSpringTP.DTOs.ocupacion.ConsumoDTO cDTO : solicitud.getListaConsumos()) {
                // Si el DTO tiene ID, buscamos el consumo existente para marcarlo
                if (cDTO.getIdConsumo() != null && cDTO.getIdConsumo() != 0) {
                     java.util.Optional<TP_Back.appSpringTP.modelo.ocupacion.Consumo> consumoOpt = consumoDAO.findById(cDTO.getIdConsumo());
                     if (consumoOpt.isPresent()) {
                         TP_Back.appSpringTP.modelo.ocupacion.Consumo consumo = consumoOpt.get();
                         if (!consumo.isFacturado()) {
                             total += consumo.getMonto();
                             consumo.setFacturado(true);
                             consumoDAO.save(consumo);
                         } 
                     }
                } else {
                    // Si no tiene ID, asumimos que es un consumo "volatil" que solo se cobra pero no se persiste como item individual o se debería crear?
                    // Asumiré que se cobra el monto.
                    total += cDTO.getMonto();
                }
            }
        }
        
        factura.setMontoTotal(total);
        factura.setIva(total * 0.30f); // 30% IVA
        
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
