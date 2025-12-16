package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DTOs.ocupacion.ConsumoDTO;
import TP_Back.appSpringTP.DTOs.HuespedDTO;
import TP_Back.appSpringTP.DTOs.SolicitudFacturacionDTO;
import TP_Back.appSpringTP.DTOs.SolicitudFacturacionJuridicaDTO;
import TP_Back.appSpringTP.gestores.factories.FacturaFisicaFactory;
import TP_Back.appSpringTP.gestores.factories.FacturaJuridicaFactory;
import TP_Back.appSpringTP.modelo.factura.Factura;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class GestorDeFacturasTest {

    @Mock
    private FacturaFisicaFactory facturaFisicaFactory;

    @Mock
    private FacturaJuridicaFactory facturaJuridicaFactory;

    @InjectMocks
    private GestorDeFacturas gestorDeFacturas;

    // --- Tests para generarFacturaFisica ---

    @Test
    public void testGenerarFacturaFisicaExito() {
        // Prepare data
        HuespedDTO huesped = new HuespedDTO();
        List<ConsumoDTO> consumos = new ArrayList<>();
        SolicitudFacturacionDTO solicitud = new SolicitudFacturacionDTO(1L, consumos, huesped);

        Factura facturaEsperada = new Factura();

        // Mock behavior
        when(facturaFisicaFactory.crearFactura(anyLong(), anyList(), any(HuespedDTO.class)))
                .thenReturn(facturaEsperada);

        // Execute
        Factura resultado = gestorDeFacturas.generarFacturaFisica(solicitud);

        // Verify
        assertNotNull(resultado);
        assertEquals(facturaEsperada, resultado);
        verify(facturaFisicaFactory).crearFactura(solicitud.getIdOcupacion(), solicitud.getListaConsumos(),
                solicitud.getHuesped());
    }

    @Test
    public void testGenerarFacturaFisicaSolicitudNula() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gestorDeFacturas.generarFacturaFisica(null);
        });
        assertEquals("La solicitud no puede ser nula.", exception.getMessage());
    }

    @Test
    public void testGenerarFacturaFisicaIdOcupacionNulo() {
        SolicitudFacturacionDTO solicitud = new SolicitudFacturacionDTO(null, new ArrayList<>(), new HuespedDTO());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gestorDeFacturas.generarFacturaFisica(solicitud);
        });
        assertEquals("El ID de ocupación es obligatorio.", exception.getMessage());
    }

    @Test
    public void testGenerarFacturaFisicaListaConsumosNula() {
        SolicitudFacturacionDTO solicitud = new SolicitudFacturacionDTO(1L, null, new HuespedDTO());
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gestorDeFacturas.generarFacturaFisica(solicitud);
        });
        assertEquals("La lista de consumos es obligatoria.", exception.getMessage());
    }

    @Test
    public void testGenerarFacturaFisicaSinHuesped() {
        // Prepare data with null Huesped
        SolicitudFacturacionDTO solicitud = new SolicitudFacturacionDTO(1L, new ArrayList<>(), null);

        // Execute & Verify
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gestorDeFacturas.generarFacturaFisica(solicitud);
        });

        assertEquals("El huesped responsable de pago es obligatorio para Factura Física.", exception.getMessage());
    }

    // --- Tests para generarFacturaJuridica ---

    @Test
    public void testGenerarFacturaJuridicaExito() {
        // Prepare data
        String cuit = "20123456789";
        List<ConsumoDTO> consumos = new ArrayList<>();
        SolicitudFacturacionJuridicaDTO solicitud = new SolicitudFacturacionJuridicaDTO(1L, consumos, cuit);

        Factura facturaEsperada = new Factura();

        // Mock behavior
        when(facturaJuridicaFactory.crearFactura(anyLong(), anyList(), anyString()))
                .thenReturn(facturaEsperada);

        // Execute
        Factura resultado = gestorDeFacturas.generarFacturaJuridica(solicitud);

        // Verify
        assertNotNull(resultado);
        assertEquals(facturaEsperada, resultado);
        verify(facturaJuridicaFactory).crearFactura(solicitud.getIdOcupacion(), solicitud.getListaConsumos(),
                solicitud.getCuitResponsable());
    }

    @Test
    public void testGenerarFacturaJuridicaSinCuit() {
        // Prepare data with null CUIT
        SolicitudFacturacionJuridicaDTO solicitud = new SolicitudFacturacionJuridicaDTO(1L, new ArrayList<>(), null);

        // Execute & Verify
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gestorDeFacturas.generarFacturaJuridica(solicitud);
        });

        assertEquals("El CUIT del responsable de pago es obligatorio para Factura Jurídica.", exception.getMessage());
    }

    @Test
    public void testGenerarFacturaJuridicaCuitVacio() {
        // Prepare data with empty CUIT
        SolicitudFacturacionJuridicaDTO solicitud = new SolicitudFacturacionJuridicaDTO(1L, new ArrayList<>(), "");

        // Execute & Verify
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gestorDeFacturas.generarFacturaJuridica(solicitud);
        });

        assertEquals("El CUIT del responsable de pago es obligatorio para Factura Jurídica.", exception.getMessage());
    }

    @Test
    public void testGenerarFacturaJuridicaSolicitudNula() {
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gestorDeFacturas.generarFacturaJuridica(null);
        });
        assertEquals("La solicitud no puede ser nula.", exception.getMessage());
    }

    @Test
    public void testGenerarFacturaJuridicaIdOcupacionNulo() {
        SolicitudFacturacionJuridicaDTO solicitud = new SolicitudFacturacionJuridicaDTO(null, new ArrayList<>(),
                "20123456789");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gestorDeFacturas.generarFacturaJuridica(solicitud);
        });
        assertEquals("El ID de ocupación es obligatorio.", exception.getMessage());
    }

    @Test
    public void testGenerarFacturaJuridicaListaConsumosNula() {
        SolicitudFacturacionJuridicaDTO solicitud = new SolicitudFacturacionJuridicaDTO(1L, null, "20123456789");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gestorDeFacturas.generarFacturaJuridica(solicitud);
        });
        assertEquals("La lista de consumos es obligatoria.", exception.getMessage());
    }

    @Test
    public void testGenerarFacturaJuridicaCuitEspacios() {
        SolicitudFacturacionJuridicaDTO solicitud = new SolicitudFacturacionJuridicaDTO(1L, new ArrayList<>(), "   ");
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            gestorDeFacturas.generarFacturaJuridica(solicitud);
        });
        assertEquals("El CUIT del responsable de pago es obligatorio para Factura Jurídica.", exception.getMessage());
    }
}
