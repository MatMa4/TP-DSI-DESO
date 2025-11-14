/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.DTOs.HuespedDTO;
import TP_Back.appSpringTP.mappers.HuespedMapper;
import TP_Back.appSpringTP.modelo.Huesped;
import TP_Back.appSpringTP.repositorios.repositorioHuesped;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author mateo
 */
@Service
public class HuespedDAOImpl implements HuespedDAO {
    @Autowired
    private final repositorioHuesped repoHuesped;
    private final HuespedMapper huespedMapper;


    public HuespedDAOImpl(repositorioHuesped huespedDAO, HuespedMapper huespedMapper) {
        this.repoHuesped = huespedDAO;
        this.huespedMapper = huespedMapper;
    }
    @Override
    public HuespedDTO save(HuespedDTO huesp){
        Huesped h = huespedMapper.toEntity(huesp);
        return huespedMapper.toDTO(repoHuesped.save(h));
    }
    @Override
    public Optional<HuespedDTO> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento){
        return repoHuesped.findByIdTipoDocumentoAndIdNumeroDocumento(tipoDocumento, numeroDocumento).map(huespedMapper::toDTO);
    }
    @Override
    public List<HuespedDTO> findAll(){
        return huespedMapper.toDTOList(repoHuesped.findAll());
    }
}
