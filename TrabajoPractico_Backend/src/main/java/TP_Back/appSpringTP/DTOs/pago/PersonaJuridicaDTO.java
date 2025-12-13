/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DTOs.pago;

import TP_Back.appSpringTP.DTOs.DireccionDTO;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mateo
 */
@Getter
@Setter
public class PersonaJuridicaDTO {
    private String razonSocial;
    private String cuit;
    private String telefono;
    private DireccionDTO direccion;
    
}
