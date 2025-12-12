/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.DTOs.HuespedDTO;
import TP_Back.appSpringTP.modelo.huesped.Huesped;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author mateo
 */
@Service
public interface HuespedDAO {
    public Huesped save(Huesped huesp);
    public Optional<HuespedDTO> consultarDocumento(String tipoDocumento, String numeroDocumento);
    public List<HuespedDTO> findAll();
    public List<HuespedDTO> buscarHuesped(HuespedDTO huesped);
    public HuespedDTO guardar(HuespedDTO huesp);
    public void modificarIDHuesped(HuespedDTO huespedModificado, HuespedDTO huespedNuevo);
    public void eliminar(HuespedDTO huesp);
}
