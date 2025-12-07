/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo.habitacion;

import TP_Back.appSpringTP.modelo.habitacion.Habitacion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author mateo
 */
@Entity
@DiscriminatorValue("IndividualEstándar")@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class IndividualEstandar extends Habitacion {

    private float costoPorNoche;
    private Integer capacidad;

    private String descripcion;
    private Integer camasIndividuales;
}
