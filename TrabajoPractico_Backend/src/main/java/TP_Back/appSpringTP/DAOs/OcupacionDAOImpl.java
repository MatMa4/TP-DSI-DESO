/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.modelo.ocupacion.Ocupacion;
import TP_Back.appSpringTP.repositorios.repositorioOcupacion;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mateo
 */
@Service
public class OcupacionDAOImpl implements OcupacionDAO {
    @Autowired
    private repositorioOcupacion repoOcupacion;

    @Override
    public void crearOcupacion(Ocupacion ocupacion) {
        repoOcupacion.save(ocupacion);
    }

    @Override
    public Ocupacion getOcupacionPorHabitacionYFecha(int numero, Date fecha) {
        List<Ocupacion> ocupaciones = repoOcupacion.findOcupacionPorHabitacionYFecha(numero, fecha);
        if (ocupaciones != null && !ocupaciones.isEmpty()) {
            return ocupaciones.get(0);
        }
        return null;
    }

    @Override
    public Ocupacion getOcupacionById(int id) {
        return repoOcupacion.findById(id).orElse(null);
    }
}
