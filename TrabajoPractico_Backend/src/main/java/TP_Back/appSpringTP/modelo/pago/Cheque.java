/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo.pago;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import java.util.Date;

/**
 *
 * @author mateo
 */
@Entity
@DiscriminatorValue("Cheque")
public class Cheque extends MetodoDePago{
    private String numero; 
    private String banco;
    private String plaza;
    private Date fechaCobro;
}
