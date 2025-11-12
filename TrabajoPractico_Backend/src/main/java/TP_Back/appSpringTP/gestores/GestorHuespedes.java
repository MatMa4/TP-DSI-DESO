/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.gestores;

/**
 *
 * @author JS
 */

import TP_Back.appSpringTP.DAOs.DireccionDAO;
import TP_Back.appSpringTP.DAOs.DireccionDAOImpl;
import TP_Back.appSpringTP.DAOs.HuespedDAO;
import TP_Back.appSpringTP.DAOs.HuespedDAOImpl;
import TP_Back.appSpringTP.excepciones.HuespedExistenteException;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import TP_Back.appSpringTP.DTOs.DireccionDTO;
import TP_Back.appSpringTP.DTOs.HuespedDTO;

@Service
public class GestorHuespedes {
    @Autowired
    private final HuespedDAO huespedDAO;
    @Autowired
    private final DireccionDAOImpl direccionDAO;


    public GestorHuespedes(HuespedDAOImpl huespedDAO, DireccionDAOImpl direccionDAO) {
        this.huespedDAO = huespedDAO;
        this.direccionDAO = direccionDAO;
    }

    public HuespedDTO guardarHuesped(HuespedDTO h, Boolean forzar) {
        if (forzar){
            DireccionDTO dir = h.getDireccionHuesped();
            // Buscar si ya existe una dirección igual
            Optional<DireccionDTO> existente = direccionDAO.findByCalleAndNumero(dir.getCalle(), dir.getNumero());

            if (existente.isPresent()) {
                // Reutilizar la dirección existente
                h.setDireccion(existente.get());
            } else {
                // Guardar la nueva dirección primero
                direccionDAO.save(dir);
            }
            return huespedDAO.save(h);
            
        }else {
            Optional <HuespedDTO> optHuesp = huespedDAO.findByTipoDocumentoAndNumeroDocumento(h.getTipoDocumento(), h.getNumeroDocumento());
            if (optHuesp.isEmpty()){ 
                return huespedDAO.save(h);
            } else {
                throw new HuespedExistenteException("Huesped existente");
            }
        }        
    }
    public List<HuespedDTO> obtenerTodos() {
        return huespedDAO.findAll();
    }
}

