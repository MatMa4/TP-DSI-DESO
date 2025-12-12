/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.modelo.pago;

import TP_Back.appSpringTP.modelo.huesped.Huesped;
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
@DiscriminatorValue("PersonaFisica")
@Getter
@Setter
public class PersonaFisica extends ResponsableDePago{
    @OneToOne
    @JoinColumns({
        @JoinColumn(name = "huesped_numero_documento", referencedColumnName = "numeroDocumento"),
        @JoinColumn(name = "huesped_tipo_documento", referencedColumnName = "tipoDocumento")
    })
    private Huesped huesped;
}
