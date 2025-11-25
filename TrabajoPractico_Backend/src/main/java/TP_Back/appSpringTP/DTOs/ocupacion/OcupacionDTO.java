/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DTOs.ocupacion;

import TP_Back.appSpringTP.DTOs.HuespedDTO;
import java.sql.Time;
import java.util.Date;
import java.util.List;

/**
 *
 * @author mateo
 */
public class OcupacionDTO {
    private int idOcupacion;
    private Date fechaInicio;
    private Date fechaFin;
    private Time checkIn;
    private Time checkOut;
    private List<ConsumoDTO> consumos;
    private List<HuespedDTO> huespedes;
}
