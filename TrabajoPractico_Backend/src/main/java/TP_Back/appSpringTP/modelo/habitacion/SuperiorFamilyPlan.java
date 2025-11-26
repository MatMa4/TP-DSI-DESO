/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo.habitacion;
import TP_Back.appSpringTP.modelo.habitacion.Habitacion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 *
 * @author mateo
 */
@Entity
@DiscriminatorValue("SuperiorFamilyPlan")
public class SuperiorFamilyPlan extends Habitacion {
    private float costoPorNoche;
    private Integer capacidad;
    private String estado;
    private String descripcion;
    private Integer camaDobles;
    private Integer camasIndividuales;
}

