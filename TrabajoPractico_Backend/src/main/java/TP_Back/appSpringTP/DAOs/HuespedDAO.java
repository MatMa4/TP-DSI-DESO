/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.DTOs.HuespedDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author mateo
 */
@Service
public interface HuespedDAO {
    public HuespedDTO save(HuespedDTO huesp);
    public Optional<HuespedDTO> findByTipoDocumentoAndNumeroDocumento(String tipoDocumento, String numeroDocumento);
    public List<HuespedDTO> findAll();
}
