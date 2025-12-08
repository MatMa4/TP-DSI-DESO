/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.repositorios;

/**
 *
 * @author JS
 */
import TP_Back.appSpringTP.modelo.direccion.Direccion;
import TP_Back.appSpringTP.modelo.direccion.DireccionId;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface repositorioDireccion extends JpaRepository<Direccion, DireccionId> {
    Optional<Direccion> findByIdCalleAndIdNumero(String calle, Integer numero);
}

