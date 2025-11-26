/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo.ocupacion;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

/**
 *
 * @author mateo
 */
@Entity
@Table(name = "consumo")
public class Consumo {
    @Id
    private int consumoId;
    private String tipoServicio;
    private String detalle;
    private float monto;
}
