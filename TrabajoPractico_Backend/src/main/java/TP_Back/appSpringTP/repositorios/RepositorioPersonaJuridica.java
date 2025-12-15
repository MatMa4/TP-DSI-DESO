/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.repositorios;

import TP_Back.appSpringTP.modelo.pago.PersonaJuridica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author mateo
 */
@Repository
public interface RepositorioPersonaJuridica extends JpaRepository<PersonaJuridica, Integer> {
    java.util.Optional<PersonaJuridica> findByCuit(String cuit);
}
