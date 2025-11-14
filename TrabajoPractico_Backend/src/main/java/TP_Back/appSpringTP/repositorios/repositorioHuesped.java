/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.repositorios;

/**
 *
 * @author JS
 */

import TP_Back.appSpringTP.modelo.Huesped;
import TP_Back.appSpringTP.modelo.HuespedId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface repositorioHuesped extends JpaRepository<Huesped, HuespedId> {
    Optional<Huesped> findByIdTipoDocumentoAndIdNumeroDocumento(String tipoDocumento, String numeroDocumento);
}

