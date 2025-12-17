/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.ConsumoDAO;
import TP_Back.appSpringTP.DAOs.HuespedDAO;
import TP_Back.appSpringTP.DAOs.OcupacionDAO;
import TP_Back.appSpringTP.DTOs.HuespedDTO;
import TP_Back.appSpringTP.DTOs.ocupacion.OcupacionDTO;
import TP_Back.appSpringTP.mappers.HabitacionMapper;
import TP_Back.appSpringTP.mappers.HuespedMapper;
import TP_Back.appSpringTP.modelo.ocupacion.Ocupacion;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import TP_Back.appSpringTP.mappers.OcupacionMapper;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;
import TP_Back.appSpringTP.DTOs.ocupacion.SolicitudConsumoDTO;
import TP_Back.appSpringTP.modelo.ocupacion.Consumo;

/**
 *
 * @author mateo
 */
@Service
@AllArgsConstructor
public class GestorDeOcupaciones {
    @Autowired
    private final OcupacionDAO ocupacionDAO;
    @Autowired
    private final ConsumoDAO consumoDAO;
    @Autowired
    private final HuespedDAO huespedDAO;
    @Autowired
    private final HabitacionMapper habitacionMapper;
    @Autowired
    private final HuespedMapper huespedMapper;

    @Autowired
    private final OcupacionMapper ocupacionMapper;

    public void crearOcupacion(OcupacionDTO ocupacion) {
        Ocupacion ocupacionNueva = new Ocupacion();
        ocupacionNueva.setHabitacion(habitacionMapper.toEntity(ocupacion.getHabitacion()));
        ocupacionNueva.setFechaInicio(ocupacion.getFechaInicio());
        ocupacionNueva.setFechaFin(ocupacion.getFechaFin());
        ocupacionNueva.setCheckIn(ocupacion.getCheckIn());
        ocupacionNueva.setCheckOut(ocupacion.getCheckOut());
        ocupacionNueva.setHuespedes(huespedMapper.toEntityList(ocupacion.getHuespedes()));
        ocupacionDAO.crearOcupacion(ocupacionNueva);

        for (HuespedDTO h : ocupacion.getHuespedes()) {
            HuespedDTO huesped = huespedDAO.consultarDocumento(h.getTipoDocumento(), h.getNumeroDocumento()).get();
            huesped.setAlojado(true);
            huespedDAO.guardar(huesped);
        }
    }

    public OcupacionDTO obtenerOcupacionActual(int numeroHabitacion, LocalTime hora) {
        // Assume 'today' is the current date
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 0);
        Date fechaActual = calendar.getTime();

        Ocupacion ocupacion = ocupacionDAO.getOcupacionPorHabitacionYFecha(numeroHabitacion, fechaActual);

        if (ocupacion != null) {
            OcupacionDTO dto = ocupacionMapper.toDTO(ocupacion);

            // Calculate total price
            long diffInMillies = Math.abs(ocupacion.getFechaFin().getTime() - ocupacion.getFechaInicio().getTime());
            long diff = TimeUnit.DAYS.convert(diffInMillies,
                    TimeUnit.MILLISECONDS);

            double costoEstadia = diff * dto.getHabitacion().getCostoPorNoche();

            dto.setPrecioTotal(costoEstadia);
            dto.setId(ocupacion.getIdOcupacion());

            return dto;
        }
        return null;
    }

    public void agregarConsumo(SolicitudConsumoDTO solicitud) {
        Ocupacion ocupacion = ocupacionDAO.getOcupacionById(solicitud.getIdOcupacion());
        if (ocupacion != null) {
            Consumo consumo = new Consumo();
            consumo.setTipoServicio(solicitud.getConsumo().getTipoServicio());
            consumo.setDetalle(solicitud.getConsumo().getDetalle());
            consumo.setMonto(solicitud.getConsumo().getMonto());

            consumo = consumoDAO.save(consumo);

            if (ocupacion.getConsumos() == null) {
                ocupacion.setConsumos(new java.util.ArrayList<>());
            }
            ocupacion.getConsumos().add(consumo);
            ocupacionDAO.crearOcupacion(ocupacion); // Updating
        } else {
            throw new RuntimeException("Ocupacion no encontrada con ID: " + solicitud.getIdOcupacion());
        }
    }
}
