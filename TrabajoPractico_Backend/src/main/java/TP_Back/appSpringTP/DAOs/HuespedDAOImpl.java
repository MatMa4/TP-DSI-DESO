/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

/**
 *
 * @author JS
 */

import TP_Back.appSpringTP.modelo.Huesped;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HuespedDAOImpl extends JpaRepository<Huesped, Long> {
    Optional<Huesped> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);
}

