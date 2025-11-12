/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.gestores;

/**
 *
 * @author JS
 */

import TP_Back.appSpringTP.excepciones.HuespedExistenteException;
import TP_Back.appSpringTP.modelo.Huesped;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import TP_Back.appSpringTP.DAOs.HuespedDAOImpl;

@Service
public class GestorHuespedes {

    private final HuespedDAOImpl huespedRepository;

    public GestorHuespedes(HuespedDAOImpl huespedRepository) {
        this.huespedRepository = huespedRepository;
    }

    public Huesped guardarHuesped(Huesped h, Boolean forzar) {
        if (forzar){
            return huespedRepository.save(h);
            
        }else {
            Optional <Huesped> optHuesp = huespedRepository.findByTipoDocumentoAndNumeroDocumento(h.getTipoDocumento(), h.getNumeroDocumento());
            if (optHuesp.isEmpty()){                
                return huespedRepository.save(h);
            } else {
                throw new HuespedExistenteException("Huesped existente");
            }
        }        
    }
    public List<Huesped> obtenerTodos() {
        return huespedRepository.findAll();
    }
}

