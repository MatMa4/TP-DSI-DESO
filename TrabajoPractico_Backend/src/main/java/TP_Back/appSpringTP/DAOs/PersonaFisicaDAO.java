/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.modelo.pago.PersonaFisica;
import java.util.Optional;

/**
 *
 * @author mateo
 */
public interface PersonaFisicaDAO {
    PersonaFisica save(PersonaFisica personaFisica);

    Optional<PersonaFisica> getByHuesped(String tipoDocumento, String numeroDocumento);

    Integer getIdResponsablePagoConHuesped(String tipoDocumento, String numeroDocumento);
}
