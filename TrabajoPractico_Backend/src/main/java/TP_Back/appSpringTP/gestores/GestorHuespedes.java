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
import TP_Back.appSpringTP.excepciones.HuespedNoEncontradoException;
import TP_Back.appSpringTP.modelo.direccion.Direccion;
import TP_Back.appSpringTP.modelo.huesped.Huesped;
import TP_Back.appSpringTP.DAOs.PersonaFisicaDAO;
import TP_Back.appSpringTP.DAOs.PersonaFisicaDAOImpl;
import TP_Back.appSpringTP.modelo.pago.PersonaFisica;

@Service
public class GestorHuespedes {
    @Autowired
    private final HuespedDAO huespedDAO;
    @Autowired
    private final DireccionDAO direccionDAO;
    @Autowired
    private final PersonaFisicaDAO personaFisicaDAO;

    public GestorHuespedes(HuespedDAOImpl huespedDAO, DireccionDAOImpl direccionDAO, PersonaFisicaDAOImpl personaFisicaDAO) {
        this.huespedDAO = huespedDAO;
        this.direccionDAO = direccionDAO;
        this.personaFisicaDAO = personaFisicaDAO;
    }

    public HuespedDTO registrarHuesped(HuespedDTO h) throws HuespedNoEncontradoException {
        DireccionDTO direccionDto=h.getDireccionHuesped();
        Direccion direccion = new Direccion();
        direccion.setDepartamento(direccionDto.getDepartamento());
        direccion.setCodigo(direccionDto.getCodigo());
        direccion.setPiso(direccionDto.getPiso());
        direccion.setId(direccionDto.getCalle(), direccionDto.getNumero(), direccionDto.getLocalidad(), direccionDto.getProvincia(), direccionDto.getPais());
        try{
            direccionDAO.save(direccion);
        }catch(Exception e){
            throw new RuntimeException("Error inesperado al registrar huésped", e);
        }
        
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
        try{
            huespedDAO.save(huesped);
        }catch(Exception e){
            throw new RuntimeException("Error inesperado al registrar huésped", e);
        }
        
        PersonaFisica personaFisica = new PersonaFisica();
        personaFisica.setHuesped(huesped);
        try{
            personaFisicaDAO.save(personaFisica);
        }catch(Exception e){
            throw new RuntimeException("Error inesperado al registrar el responsable de pago", e);
        }
        
        Optional <HuespedDTO> resultado = huespedDAO.consultarDocumento(huesped.getTipoDocumento(), huesped.getNumeroDocumento());   
        if(resultado.isPresent()){
            return resultado.get();
        }else{
            throw new HuespedNoEncontradoException("No se encontró huésped con documento " + huesped.getNumeroDocumento());
        }     
    }
    
    public HuespedDTO modificarHuesped(List<HuespedDTO> huespedes) throws HuespedNoEncontradoException{
        huespedDAO.modificarIDHuesped(huespedes.get(0), huespedes.get(1));
        try{
            huespedDAO.guardar(huespedes.get(0));
        }catch(Exception e){
            throw new RuntimeException("Error inesperado al guardar el huésped modificado", e);
        }
        Optional <HuespedDTO> resultado = huespedDAO.consultarDocumento(huespedes.get(0).getTipoDocumento(), huespedes.get(0).getNumeroDocumento());   
        if(resultado.isPresent()){
            return resultado.get();
        }else{
            throw new HuespedNoEncontradoException("No se encontró huésped con documento " + huespedes.get(0).getNumeroDocumento());
        } 
    }
    
    public List<HuespedDTO> buscarHuesped(String tipo, String numero, String nombre, String apellido){
        HuespedDTO huesped = new HuespedDTO();
        huesped.setTipoDocumento(tipo);
        huesped.setNumeroDocumento(numero);
        huesped.setApellido(apellido);
        huesped.setNombre(nombre);
        List<HuespedDTO> huespedes;
        try{
            huespedes = huespedDAO.buscarHuesped(huesped);
        }catch(HuespedNoEncontradoException e){
            throw e;
        }
        return huespedes;
    }
    public void consultarDocumento(String tipoDocumento, String numeroDocumento){
        Optional<HuespedDTO> huesped = huespedDAO.consultarDocumento(tipoDocumento, numeroDocumento);
        if(huesped.isPresent()){
            throw new HuespedExistenteException("Huesped existente");
        }
    }
    public void huespedExistente(String tipoModificado, String numeroModificado, String tipoOriginal, String numeroOriginal){
        if(!tipoModificado.equals(tipoOriginal) || !numeroModificado.equals(numeroOriginal)){
            Optional<HuespedDTO> huesped = huespedDAO.consultarDocumento(tipoModificado, numeroModificado);
            if(huesped.isPresent()){
                throw new HuespedExistenteException("Huesped existente");
            }
        }
    }

    public List<HuespedDTO> listarTodosHuespedes() {
        return huespedDAO.findAll();
    }
}

