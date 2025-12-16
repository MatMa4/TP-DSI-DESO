package TP_Back.appSpringTP.gestores.factories;

import TP_Back.appSpringTP.DAOs.PersonaJuridicaDAO;
import TP_Back.appSpringTP.modelo.pago.PersonaJuridica;
import TP_Back.appSpringTP.modelo.pago.ResponsableDePago;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.Optional;

@Component
public class FacturaJuridicaFactory extends AbstractFacturaFactory {

    @Autowired
    private PersonaJuridicaDAO personaJuridicaDAO;

    @Override
    protected ResponsableDePago obtenerResponsable(Object identificador) {
        if (!(identificador instanceof String)) {
            throw new IllegalArgumentException("El identificador para Factura Jurídica debe ser un CUIT (String)");
        }
        String cuit = (String) identificador;

        Optional<PersonaJuridica> responsableOpt = personaJuridicaDAO.buscarPorCuit(cuit);

        if (responsableOpt.isEmpty()) {
            throw new RuntimeException("No se encontró una Persona Jurídica con el CUIT proporcionado: " + cuit);
        }

        return responsableOpt.get();
    }
}
