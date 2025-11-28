/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo.pago;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

/**
 *
 * @author mateo
 */
@Entity
@DiscriminatorValue("TarjetaDebito")
public class TarjetaDebito extends MetodoDePago{
    private String tipo;
    private String banco;
}
