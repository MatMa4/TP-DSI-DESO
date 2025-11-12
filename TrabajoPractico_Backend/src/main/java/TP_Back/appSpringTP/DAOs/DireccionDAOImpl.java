/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.DTOs.DireccionDTO;
import TP_Back.appSpringTP.mappers.HuespedMapper;
import TP_Back.appSpringTP.modelo.Direccion;
import TP_Back.appSpringTP.repositorios.repositorioDireccion;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mateo
 */
@Service
public class DireccionDAOImpl implements DireccionDAO {
    @Autowired
    private final repositorioDireccion repoDireccion;
    private final HuespedMapper huespedMapper;


    public DireccionDAOImpl(repositorioDireccion direccionDAO, HuespedMapper huespedMapper) {
        this.repoDireccion = direccionDAO;
        this.huespedMapper = huespedMapper;
    }
    @Override
    public Optional<DireccionDTO> findByCalleAndNumero(String calle, String numero){
        return repoDireccion.findByIdCalleAndIdNumero(calle, numero).map(huespedMapper::toDTO);
    }
    @Override
    public DireccionDTO save(DireccionDTO dir){
        Direccion d = huespedMapper.toEntity(dir);
        return huespedMapper.toDTO(repoDireccion.save(d));
    }
}
