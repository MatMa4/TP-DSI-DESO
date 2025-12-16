package TP_Back.appSpringTP.gestores.factories;

import TP_Back.appSpringTP.DAOs.PersonaFisicaDAO;
import TP_Back.appSpringTP.DTOs.HuespedDTO;
import TP_Back.appSpringTP.modelo.pago.PersonaFisica;
import TP_Back.appSpringTP.modelo.pago.ResponsableDePago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class FacturaFisicaFactory extends AbstractFacturaFactory {

    @Autowired
    private PersonaFisicaDAO personaFisicaDAO;

    @Override
    protected ResponsableDePago obtenerResponsable(Object identificador) {
        if (!(identificador instanceof HuespedDTO)) {
            throw new IllegalArgumentException("El identificador para Factura Física debe ser un HuespedDTO");
        }
        HuespedDTO huesped = (HuespedDTO) identificador;

        Optional<PersonaFisica> responsableOpt = personaFisicaDAO.getByHuesped(
                huesped.getTipoDocumento(),
                huesped.getNumeroDocumento());

        if (responsableOpt.isEmpty()) {
            throw new RuntimeException(
                    "No se encontró una Persona Física asociada al Huésped. Valide que el Huésped esté registrado.");
        }

        return responsableOpt.get();
    }
}
