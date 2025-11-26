/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.repositorios;

import TP_Back.appSpringTP.modelo.ocupacion.Ocupacion;
import org.springframework.data.jpa.repository.JpaRepository;
/**
 *
 * @author mateo
 */
public interface repositorioOcupacion extends JpaRepository<Ocupacion, Integer> {
   
}
