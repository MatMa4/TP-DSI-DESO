/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.DTOs.DireccionDTO;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author mateo
 */
@Service
public interface DireccionDAO {
    public Optional<DireccionDTO> findByCalleAndNumero(String calle, String numero);
    public DireccionDTO save(DireccionDTO dir);
}
