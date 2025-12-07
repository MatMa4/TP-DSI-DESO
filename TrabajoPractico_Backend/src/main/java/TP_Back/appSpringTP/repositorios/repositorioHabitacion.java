/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.repositorios;

import TP_Back.appSpringTP.modelo.habitacion.Habitacion;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 *
 * @author mateo
 */
public interface repositorioHabitacion extends JpaRepository<Habitacion, Integer>{
    List<Habitacion> findByEstado(String estado);
}
