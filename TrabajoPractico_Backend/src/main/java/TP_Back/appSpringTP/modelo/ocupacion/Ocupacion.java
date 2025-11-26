/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo.ocupacion;

import TP_Back.appSpringTP.modelo.huesped.Huesped;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mateo
 */
@Entity
@Table(name = "ocupacion")
public class Ocupacion {
    @Id
    private int idOcupacion;
    private Date fechaInicio;
    private Date fechaFin;
    private Time checkIn;
    private Time checkOut;
    @OneToMany
    private List<Consumo> consumos;
    @ManyToMany
    private List<Huesped> huespedes;
}
