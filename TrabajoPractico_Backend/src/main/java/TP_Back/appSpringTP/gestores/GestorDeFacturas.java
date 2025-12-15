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
    private TP_Back.appSpringTP.DAOs.PersonaJuridicaDAO personaJuridicaDAO;
    @Autowired
    private TP_Back.appSpringTP.DAOs.ConsumoDAO consumoDAO;

    public Factura generarFacturaFisica(TP_Back.appSpringTP.DTOs.SolicitudFacturacionDTO solicitud) {
         // Validar inputs comunes
         validarSolicitud(solicitud.getIdOcupacion(), solicitud.getListaConsumos());

         if (solicitud.getHuesped() == null) {
              throw new RuntimeException("El huesped responsable de pago es obligatorio para Factura Física.");
         }

         // Buscar Responsable de Pago (PersonaFisica)
         java.util.Optional<TP_Back.appSpringTP.modelo.pago.PersonaFisica> responsableOpt = personaFisicaDAO.getByHuesped(
                 solicitud.getHuesped().getTipoDocumento(), 
                 solicitud.getHuesped().getNumeroDocumento());
         
         if (responsableOpt.isEmpty()) {
             throw new RuntimeException("No se encontró una Persona Física asociada al Huésped. Valide que el Huésped esté registrado.");
         }
         
         return procesarFactura(solicitud.getIdOcupacion(), solicitud.getListaConsumos(), responsableOpt.get());
    }

    public Factura generarFacturaJuridica(TP_Back.appSpringTP.DTOs.SolicitudFacturacionJuridicaDTO solicitud) {
         // Validar inputs comunes
         validarSolicitud(solicitud.getIdOcupacion(), solicitud.getListaConsumos());

         if (solicitud.getCuitResponsable() == null || solicitud.getCuitResponsable().isEmpty()) {
              throw new RuntimeException("El CUIT del responsable de pago es obligatorio para Factura Jurídica.");
         }

         // Buscar Responsable de Pago (PersonaJuridica)
         java.util.Optional<TP_Back.appSpringTP.modelo.pago.PersonaJuridica> responsableOpt = personaJuridicaDAO.buscarPorCuit(solicitud.getCuitResponsable());
         
         if (responsableOpt.isEmpty()) {
             throw new RuntimeException("No se encontró una Persona Jurídica con el CUIT proporcionado: " + solicitud.getCuitResponsable());
         }
         
         return procesarFactura(solicitud.getIdOcupacion(), solicitud.getListaConsumos(), responsableOpt.get());
    }

    private void validarSolicitud(Long idOcupacion, java.util.List<TP_Back.appSpringTP.DTOs.ocupacion.ConsumoDTO> listaConsumos) {
        if ((idOcupacion == null || idOcupacion == 0) && 
            (listaConsumos == null || listaConsumos.isEmpty())) {
            throw new RuntimeException("Debe existir al menos un item para facturar (Ocupación o Consumos).");
        }
    }

    private Factura procesarFactura(Long idOcupacion, java.util.List<TP_Back.appSpringTP.DTOs.ocupacion.ConsumoDTO> listaConsumos, TP_Back.appSpringTP.modelo.pago.ResponsableDePago responsable) {
        // Buscar Ocupacion
        TP_Back.appSpringTP.modelo.ocupacion.Ocupacion ocupacion = null;
        if (idOcupacion != null && idOcupacion != 0) {
            ocupacion = ocupacionDAO.getOcupacionById(idOcupacion.intValue());
            if (ocupacion == null) {
                 throw new RuntimeException("Ocupacion no encontrada con ID: " + idOcupacion);
            }
        }

        // Crear Factura
        Factura factura = new Factura();
        factura.setFechaEmision(new java.util.Date());
        factura.setEstado("PENDIENTE_PAGO"); 
        factura.setResponsableDePago(responsable);
        
        if (ocupacion != null) {
            factura.setOcupacion(ocupacion);
        }
        
        // Calculo de monto total
        float total = 0;
        
        if (ocupacion != null) {
            factura.setOcupacion(ocupacion);
        }
        
        // Sumar ocupacion si NO ha sido facturada
        if (ocupacion != null) {
             if (!ocupacion.isFacturada()) {
                 long diffInMillies = Math.abs(ocupacion.getFechaFin().getTime() - ocupacion.getFechaInicio().getTime());
                 long diff = java.util.concurrent.TimeUnit.DAYS.convert(diffInMillies, java.util.concurrent.TimeUnit.MILLISECONDS);
                 total += diff * ocupacion.getHabitacion().getCostoPorNoche();
                 
                 ocupacion.setFacturada(true);
                 ocupacionDAO.crearOcupacion(ocupacion); // Guardar cambio de estado
             }
             // Si ya fue facturada, no sumamos nada (el requerimiento es "solo facture si no fue facturada").
        }
        
        // Sumar consumos extra y marcarlos como facturados
        if (listaConsumos != null) {
            for (TP_Back.appSpringTP.DTOs.ocupacion.ConsumoDTO cDTO : listaConsumos) {
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
                    total += cDTO.getMonto();
                }
            }
        }
        
        if (total > 0) {
            factura.setMontoTotal(total);
            factura.setIva(total * 0.30f);
            factura.setEstado("PENDIENTE_PAGO"); // Set state here as it's a valid factura
        } else {
             throw new IllegalArgumentException("El monto total de la factura debe ser mayor a 0. Revise si la ocupación o los consumos ya fueron facturados previamente.");
        }

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
