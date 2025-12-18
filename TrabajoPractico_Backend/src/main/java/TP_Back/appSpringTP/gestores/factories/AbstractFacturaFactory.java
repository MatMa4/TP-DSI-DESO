package TP_Back.appSpringTP.gestores.factories;

import TP_Back.appSpringTP.DAOs.ConsumoDAO;
import TP_Back.appSpringTP.DAOs.FacturaDAO;
import TP_Back.appSpringTP.DAOs.OcupacionDAO;
import TP_Back.appSpringTP.DTOs.ocupacion.ConsumoDTO;
import TP_Back.appSpringTP.modelo.factura.Factura;
import TP_Back.appSpringTP.modelo.ocupacion.Consumo;
import TP_Back.appSpringTP.modelo.ocupacion.Ocupacion;
import TP_Back.appSpringTP.modelo.pago.ResponsableDePago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Component
public abstract class AbstractFacturaFactory {

    @Autowired
    protected FacturaDAO facturaDAO;
    @Autowired
    protected OcupacionDAO ocupacionDAO;
    @Autowired
    protected ConsumoDAO consumoDAO;

    protected abstract ResponsableDePago obtenerResponsable(Object identificador);

    public Factura crearFactura(Long idOcupacion, List<ConsumoDTO> listaConsumos, Object identificadorResponsable) {

        validarSolicitud(idOcupacion, listaConsumos);

        // 1. Obtener Responsable
        ResponsableDePago responsable = obtenerResponsable(identificadorResponsable);
        if (responsable == null) {
            throw new RuntimeException("No se pudo identificar al Responsable de Pago.");
        }

        // 2. Buscar Ocupacion
        Ocupacion ocupacion = null;
        if (idOcupacion != null && idOcupacion != 0) {
            ocupacion = ocupacionDAO.getOcupacionById(idOcupacion.intValue());
            if (ocupacion == null) {
                throw new RuntimeException("Ocupacion no encontrada con ID: " + idOcupacion);
            }
        }

        // 3. Crear Estructura Base Factura
        Factura factura = new Factura();
        factura.setFechaEmision(new Date());
        factura.setEstado("PENDIENTE_PAGO");
        factura.setResponsableDePago(responsable);

        if (ocupacion != null) {
            factura.setOcupacion(ocupacion);
        }

        // 4. Calcular Montos y Actualizar Estados
        float total = calcularTotalYActualizarEstados(ocupacion, listaConsumos);
        float iva = total * 0.30f;

        if (total > 0) {
            factura.setMontoTotal(total + iva);
            factura.setIva(iva);
        } else {
            throw new IllegalArgumentException("El monto total de la factura debe ser mayor a 0.");
        }

        return facturaDAO.save(factura);
    }

    private void validarSolicitud(Long idOcupacion, List<ConsumoDTO> listaConsumos) {
        if ((idOcupacion == null || idOcupacion == 0) &&
                (listaConsumos == null || listaConsumos.isEmpty())) {
            throw new RuntimeException("Debe existir al menos un item para facturar (Ocupación o Consumos).");
        }
    }

    private float calcularTotalYActualizarEstados(Ocupacion ocupacion, List<ConsumoDTO> listaConsumos) {
        float total = 0;

        // Ocupacion
        if (ocupacion != null) {
            if (!ocupacion.isFacturada()) {
                long diffInMillies = Math.abs(ocupacion.getFechaFin().getTime() - ocupacion.getFechaInicio().getTime());
                long diff = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                total += diff * ocupacion.getHabitacion().getCostoPorNoche();

                ocupacion.setFacturada(true);
                ocupacionDAO.crearOcupacion(ocupacion);
            }
        }

        // Consumos
        if (listaConsumos != null) {
            for (ConsumoDTO cDTO : listaConsumos) {
                if (cDTO.getIdConsumo() != null && cDTO.getIdConsumo() != 0) {
                    Optional<Consumo> consumoOpt = consumoDAO.findById(cDTO.getIdConsumo());
                    if (consumoOpt.isPresent()) {
                        Consumo consumo = consumoOpt.get();
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
        return total;
    }
}
