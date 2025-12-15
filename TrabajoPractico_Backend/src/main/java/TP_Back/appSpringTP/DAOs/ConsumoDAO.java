/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.modelo.ocupacion.Consumo;
import org.springframework.stereotype.Service;

@Service
public interface ConsumoDAO {
   public Consumo save(Consumo consumo);
   public java.util.Optional<Consumo> findById(int id);
}
