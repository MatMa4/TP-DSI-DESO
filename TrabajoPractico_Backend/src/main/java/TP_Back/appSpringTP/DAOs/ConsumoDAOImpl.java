/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.modelo.ocupacion.Consumo;
import TP_Back.appSpringTP.repositorios.repositorioConsumo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ConsumoDAOImpl implements ConsumoDAO{
    
    @Autowired
    private repositorioConsumo repoConsumo;

    @Override
    public Consumo save(Consumo consumo) {
        return repoConsumo.save(consumo);
    }

    @Override
    public java.util.Optional<Consumo> findById(int id) {
        return repoConsumo.findById(id);
    }
}
