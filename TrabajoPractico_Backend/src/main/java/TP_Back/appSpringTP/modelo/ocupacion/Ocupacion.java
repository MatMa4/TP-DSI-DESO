/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo.ocupacion;

import TP_Back.appSpringTP.modelo.habitacion.Habitacion;
import TP_Back.appSpringTP.modelo.huesped.Huesped;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.sql.Time;
import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mateo
 */
@Entity
@Table(name = "ocupacion")
@Getter
@Setter
public class Ocupacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idOcupacion;
    
    @ManyToOne
    @JoinColumn(name = "habitacion_numero")
    private Habitacion habitacion;

    private Date fechaInicio;
    private Date fechaFin;
    private Time checkIn;
    private Time checkOut;
    
    @OneToMany
    private List<Consumo> consumos;
    @ManyToMany
    private List<Huesped> huespedes;
    
    private boolean facturada;
}
