/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.ConsumoDAO;
import TP_Back.appSpringTP.DAOs.ConsumoDAOImpl;
import TP_Back.appSpringTP.DAOs.DireccionDAOImpl;
import TP_Back.appSpringTP.DAOs.HuespedDAO;
import TP_Back.appSpringTP.DAOs.HuespedDAOImpl;
import TP_Back.appSpringTP.DAOs.OcupacionDAO;
import TP_Back.appSpringTP.DAOs.OcupacionDAOImpl;
import TP_Back.appSpringTP.DTOs.HabitacionDTO;
import TP_Back.appSpringTP.DTOs.HuespedDTO;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mateo
 */
@Service
public class GestorDeOcupaciones {
    @Autowired
    private final OcupacionDAO ocupacionDAO;
    @Autowired
    private final ConsumoDAO consumoDAO;
    
    public GestorDeOcupaciones(OcupacionDAOImpl ocupacionDAO, ConsumoDAOImpl consumoDAO) {
        this.ocupacionDAO = ocupacionDAO;
        this.consumoDAO = consumoDAO;
    }
    
    public void crearOcupacion(List<HuespedDTO> listaHuespedes, Date f_inicio, Date f_fin, HabitacionDTO habitacion){
        ocupacionDAO.crearOcupacion(listaHuespedes, f_inicio, f_fin, habitacion);
    }
}
