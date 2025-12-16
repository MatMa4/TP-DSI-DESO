/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.repositorios.RepositorioResponsablePago;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mateo
 */
@Service
@AllArgsConstructor
public class ResponsablePagoDAOImpl implements ResponsablePagoDAO{
    @Autowired
    private final RepositorioResponsablePago repoResponsable;
    @Override
    public void eliminar(Integer id){
        repoResponsable.deleteById(id);
    }
    
}
