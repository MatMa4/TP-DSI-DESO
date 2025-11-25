package TP_Back.appSpringTP.modelo.habitacion;

import TP_Back.appSpringTP.modelo.habitacion.Habitacion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author mateo
 */
@Entity
@DiscriminatorValue("Suite")
public class Suite extends Habitacion {
    private float costoPorNoche;
    private Integer capacidad;
    private String estado;
    private String descripcion;
    private Integer camaKingsize;
}
