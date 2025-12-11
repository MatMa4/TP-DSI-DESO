/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo.pago;

import TP_Back.appSpringTP.modelo.direccion.Direccion;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/**
 *
 * @author mateo
 */
@Entity
@DiscriminatorValue("PersonaJuridica")
@Getter
@Setter
public class PersonaJuridica extends ResponsableDePago{
    private String razonSocial;
    private String cuit;
        @OneToOne
    @JoinColumns({
        @JoinColumn(name = "calle", referencedColumnName = "calle"),
        @JoinColumn(name = "numero", referencedColumnName = "numero"),
        @JoinColumn(name = "localidad", referencedColumnName = "localidad"),
        @JoinColumn(name = "provincia", referencedColumnName = "provincia"),
        @JoinColumn(name = "pais", referencedColumnName = "pais")
    })
    private Direccion direccionJurídica;
    private String telefono;
}
