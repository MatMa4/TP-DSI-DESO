/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.DTOs.DireccionDTO;
import TP_Back.appSpringTP.modelo.direccion.Direccion;
import TP_Back.appSpringTP.modelo.direccion.DireccionId;
import java.util.Optional;
import org.springframework.stereotype.Service;

/**
 *
 * @author mateo
 */
@Service
public interface DireccionDAO {
    public Optional<DireccionDTO> findById(DireccionId id);
    public Direccion save(Direccion dir);
}
