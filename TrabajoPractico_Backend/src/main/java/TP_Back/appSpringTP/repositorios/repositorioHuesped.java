/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.repositorios;

/**
 *
 * @author JS
 */

import TP_Back.appSpringTP.modelo.huesped.Huesped;
import TP_Back.appSpringTP.modelo.huesped.HuespedId;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface repositorioHuesped extends JpaRepository<Huesped, HuespedId> {
    Optional<Huesped> findByIdTipoDocumentoAndIdNumeroDocumento(String tipoDocumento, String numeroDocumento);
    @Query("SELECT h FROM Huesped h " +
           "WHERE (:nombre IS NULL OR :nombre='' OR h.nombre LIKE CONCAT(:nombre, '%')) " +
           "AND (:apellido IS NULL OR :apellido='' OR h.apellido LIKE CONCAT(:apellido, '%')) " +
           "AND (:tipoDocumento IS NULL OR :tipoDocumento='' OR h.id.tipoDocumento LIKE CONCAT(:tipoDocumento, '%')) " +
           "AND (:numeroDocumento IS NULL OR :numeroDocumento='' OR h.id.numeroDocumento LIKE CONCAT(:numeroDocumento, '%'))")
    List<Huesped> buscarHuespedes(@Param("nombre") String nombre,
                                  @Param("apellido") String apellido,
                                  @Param("tipoDocumento") String tipoDocumento,
                                  @Param("numeroDocumento") String numeroDocumento);
    @Modifying
    @Transactional
    @Query(value = "UPDATE HUESPED " +
                   "SET numero_documento = :nuevoNumero, tipo_documento = :nuevoTipo " +
                   "WHERE numero_documento = :viejoNumero AND tipo_documento = :viejoTipo",
           nativeQuery = true)
    int modificarHuespedId(@Param("viejoNumero") String viejoNumero,
                        @Param("viejoTipo") String viejoTipo,
                        @Param("nuevoNumero") String nuevoNumero,
                        @Param("nuevoTipo") String nuevoTipo);
}

