/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

/**
 *
 * @author JS
 */
import TP_Back.appSpringTP.modelo.Direccion;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DireccionDAOImpl extends JpaRepository<Direccion, Long> {
}

