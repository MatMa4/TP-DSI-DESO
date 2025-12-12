/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.modelo.pago.PersonaJuridica;
import TP_Back.appSpringTP.repositorios.RepositorioPersonaJuridica;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mateo
 */
@Repository
public class PersonaJuridicaDAOImpl implements PersonaJuridicaDAO {
    
    @Autowired
    private RepositorioPersonaJuridica repositorioPersonaJuridica;

    @Override
    public PersonaJuridica save(PersonaJuridica personaJuridica) {
        return repositorioPersonaJuridica.save(personaJuridica);
    }
}
