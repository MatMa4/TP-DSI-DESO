/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.repositorios;

import TP_Back.appSpringTP.modelo.pago.PersonaFisica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 *
 * @author antigravity
 */
@Repository
public interface RepositorioPersonaFisica extends JpaRepository<PersonaFisica, Integer> {
    
    @org.springframework.data.jpa.repository.Query("SELECT p FROM PersonaFisica p WHERE p.huesped.id.numeroDocumento = :numeroDocumento AND p.huesped.id.tipoDocumento = :tipoDocumento")
    java.util.Optional<PersonaFisica> findByHuespedDocumento(@org.springframework.data.repository.query.Param("numeroDocumento") String numeroDocumento, @org.springframework.data.repository.query.Param("tipoDocumento") String tipoDocumento);
}
