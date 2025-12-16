/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.modelo.pago.PersonaFisica;
import TP_Back.appSpringTP.repositorios.RepositorioPersonaFisica;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author antigravity
 */
@Repository
public class PersonaFisicaDAOImpl implements PersonaFisicaDAO {
    
    @Autowired
    private RepositorioPersonaFisica repositorioPersonaFisica;

    @Override
    public PersonaFisica save(PersonaFisica personaFisica) {
        return repositorioPersonaFisica.save(personaFisica);
    }

    @Override
    public Optional<PersonaFisica> getByHuesped(String tipoDocumento, String numeroDocumento) {
        return repositorioPersonaFisica.findByHuespedDocumento(numeroDocumento, tipoDocumento);
    }
    
    @Override
    public Integer getIdResponsablePagoConHuesped(String tipoDocumento, String numeroDocumento) {
        Optional<PersonaFisica> persona = repositorioPersonaFisica.findByHuespedDocumento(numeroDocumento, tipoDocumento);
        return persona.get().getIdResponsable();
    }
}
