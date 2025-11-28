/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DTOs;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 *
 * @author mateo
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class HabitacionDTO {
    private int numero;
    private float costoPorNoche;
    private int capacidad;
    private String estado;
    private String descripcion;
    private int camasIndividuales;
    private int camaDoble;
    private int camaKingsize;
    private String tipoHabitacion;
}
