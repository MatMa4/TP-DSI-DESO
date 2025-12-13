/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

/**
 *
 * @author mateo
 */
import TP_Back.appSpringTP.modelo.pago.PersonaJuridica;

public interface PersonaJuridicaDAO {
    PersonaJuridica save(PersonaJuridica personaJuridica);
    java.util.Optional<PersonaJuridica> buscarPorCuit(String cuit);
}
