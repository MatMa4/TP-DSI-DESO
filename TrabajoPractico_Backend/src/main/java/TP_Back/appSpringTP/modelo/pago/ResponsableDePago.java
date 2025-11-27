/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo.pago;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

/**
 *
 * @author mateo
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "responsable_de_pago")
public abstract class ResponsableDePago {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer idResponsable;
}
