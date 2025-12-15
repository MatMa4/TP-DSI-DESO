/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.modelo.ocupacion.Ocupacion;
import org.springframework.stereotype.Service;

/**
 *
 * @author mateo
 */
@Service
public interface OcupacionDAO {
        public void crearOcupacion(Ocupacion ocupacion);
        public Ocupacion getOcupacionPorHabitacionYFecha(int numero, java.util.Date fecha);
        public Ocupacion getOcupacionById(int id);
}
