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
import TP_Back.appSpringTP.modelo.direccion.Direccion;
import TP_Back.appSpringTP.modelo.huesped.Huesped;

@Service
public class GestorHuespedes {
    @Autowired
    private final HuespedDAO huespedDAO;
    @Autowired
    private final DireccionDAO direccionDAO;


    public GestorHuespedes(HuespedDAOImpl huespedDAO, DireccionDAOImpl direccionDAO) {
        this.huespedDAO = huespedDAO;
        this.direccionDAO = direccionDAO;
    }

    public Boolean registrarHuesped(HuespedDTO h) {
        DireccionDTO direccionDto=h.getDireccionHuesped();
        Direccion direccion = new Direccion();
        direccion.setDepartamento(direccionDto.getDepartamento());
        direccion.setCodigo(direccionDto.getCodigo());
        direccion.setPiso(direccionDto.getPiso());
        direccion.setId(direccionDto.getCalle(), direccionDto.getNumero(), direccionDto.getLocalidad(), direccionDto.getProvincia(), direccionDto.getPais());
        direccionDAO.save(direccion);
        Huesped huesped = new Huesped();
        huesped.setNombre(h.getNombre());
        huesped.setApellido(h.getApellido());
        huesped.setTipoDocumento(h.getTipoDocumento());
        huesped.setNumeroDocumento(h.getNumeroDocumento());
        huesped.setFechaNacimiento(h.getFechaNacimiento());
        huesped.setTelefono(h.getTelefono());
        huesped.setEmail(h.getEmail());
        huesped.setOcupacion(h.getOcupacion());
        huesped.setNacionalidad(h.getNacionalidad());
        huesped.setCuit(h.getCuit());
        huesped.setPosicionIVA(h.getPosicionIVA());
        huesped.setAlojado(h.getAlojado());
        huesped.setDireccionHuesped(direccion);
        huespedDAO.save(huesped);
        return true;     
    }
    public List<HuespedDTO> obtenerTodos() {
        return huespedDAO.findAll();
    }
    
    public List<HuespedDTO> buscarHuesped(String tipo, String numero, String nombre, String apellido){
        HuespedDTO huesped = new HuespedDTO();
        huesped.setTipoDocumento(tipo);
        huesped.setNumeroDocumento(numero);
        huesped.setApellido(apellido);
        huesped.setNombre(nombre);
        return huespedDAO.buscarHuesped(huesped);
    }
    public Huesped obtenerHuesped(String tipo, String numero){
        HuespedDTO huesped = new HuespedDTO();
        huesped.setTipoDocumento(tipo);
        huesped.setNumeroDocumento(numero);
        return huespedDAO.obtenerHuesped(huesped);
    }
    public boolean consultarDocumento(String tipoDocumento, String numeroDocumento){
        Optional<HuespedDTO> huesped = huespedDAO.consultarDocumento(tipoDocumento, numeroDocumento);
        if(huesped.isEmpty()){
            return true;
        }else{
            throw new HuespedExistenteException("Huesped existente");
        }
        
    }
}

