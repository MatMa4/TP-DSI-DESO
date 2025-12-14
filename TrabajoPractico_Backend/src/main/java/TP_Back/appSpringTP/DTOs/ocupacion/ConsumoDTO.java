/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DTOs.ocupacion;

import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mateo
 */
@Getter
@Setter
public class ConsumoDTO {
    private Integer idConsumo;
    private String tipoServicio;
    private String detalle;
    private float monto;
    private boolean facturado;
}
