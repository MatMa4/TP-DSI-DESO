/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.DTOs.HuespedDTO;
import TP_Back.appSpringTP.mappers.HuespedMapper;
import TP_Back.appSpringTP.modelo.huesped.Huesped;
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
    public Huesped save(Huesped huesp){
        return repoHuesped.save(huesp);
    }
    @Override
    public Optional<HuespedDTO> consultarDocumento(String tipoDocumento, String numeroDocumento){
        return repoHuesped.findByIdTipoDocumentoAndIdNumeroDocumento(tipoDocumento, numeroDocumento).map(huespedMapper::toDTO);
    }
    @Override
    public List<HuespedDTO> findAll(){
        return huespedMapper.toDTOList(repoHuesped.findAll());
    }
    @Override
    public List<HuespedDTO> buscarHuesped(HuespedDTO h){
        return huespedMapper.toDTOList(repoHuesped.buscarHuespedes(h.getNombre(), h.getApellido(), h.getTipoDocumento(), h.getNumeroDocumento()));
    }
    @Override
    public Huesped obtenerHuesped(HuespedDTO huesped){
        return repoHuesped.findByIdTipoDocumentoAndIdNumeroDocumento(huesped.getTipoDocumento(), huesped.getNumeroDocumento()).get();
    }


}
