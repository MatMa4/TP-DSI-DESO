/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.gestores;

import TP_Back.appSpringTP.DAOs.DireccionDAO;
import TP_Back.appSpringTP.DAOs.PersonaJuridicaDAO;
import TP_Back.appSpringTP.DTOs.DireccionDTO;
import TP_Back.appSpringTP.DTOs.pago.PersonaJuridicaDTO;
import TP_Back.appSpringTP.modelo.direccion.Direccion;
import TP_Back.appSpringTP.repositorios.repositorioDireccion;
import TP_Back.appSpringTP.modelo.direccion.DireccionId;
import TP_Back.appSpringTP.modelo.pago.PersonaJuridica;
import jakarta.persistence.EntityNotFoundException;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GestorResponsableDePago {

    @Autowired
    private PersonaJuridicaDAO personaJuridicaDAO;

    @Autowired
    private DireccionDAO direccionDAO;

    @Autowired
    private repositorioDireccion repoDireccion;

    public void registrarPersonaJuridica(PersonaJuridicaDTO dto) {
        // 1. Guardar o Recuperar Direccion
        DireccionDTO dirDto = dto.getDireccion();
        Direccion direccion = new Direccion();
        direccion.setDepartamento(dirDto.getDepartamento());
        direccion.setCodigo(dirDto.getCodigo());
        direccion.setPiso(dirDto.getPiso());

        // Asignar ID
        direccion.setId(dirDto.getCalle(), dirDto.getNumero(), dirDto.getLocalidad(), dirDto.getProvincia(),
                dirDto.getPais());

        // Crear ID objeto para busqueda
        DireccionId id = new DireccionId(dirDto.getCalle(), dirDto.getNumero());
        // Completar el ID con los otros campos para la busqueda completa (aunque el
        // constructor de DireccionId solo toma calle y numero?? Revisemos DireccionId)
        // DireccionId.java tiene calle, numero, localidad, provincia, pais. El
        // constructor de 2 args solo setea calle y numero.
        // Pero el equals usa todos. Necesitamos setear todos.
        id.setLocalidad(dirDto.getLocalidad());
        id.setProvincia(dirDto.getProvincia());
        id.setPais(dirDto.getPais());

        Optional<Direccion> existingDir = repoDireccion.findById(id);

        if (existingDir.isPresent()) {
            direccion = existingDir.get();
        } else {
            try {
                direccion = direccionDAO.save(direccion);
            } catch (Exception e) {
                throw new RuntimeException("Error al guardar la dirección de la persona jurídica", e);
            }
        }

        // 2. Guardar Persona Juridica
        PersonaJuridica pj = new PersonaJuridica();
        pj.setRazonSocial(dto.getRazonSocial());
        pj.setCuit(dto.getCuit());
        pj.setTelefono(dto.getTelefono());
        pj.setDireccion(direccion); // Asignar la direccion (existente o nueva)

        try {
            personaJuridicaDAO.save(pj);
        } catch (Exception e) {
            throw new RuntimeException("Error al guardar la persona jurídica", e);
        }
    }

    public PersonaJuridicaDTO buscarPersonaJuridica(String cuit) {
        Optional<PersonaJuridica> pjOpt = personaJuridicaDAO.buscarPorCuit(cuit);

        if (pjOpt.isEmpty()) {
            throw new EntityNotFoundException("No se encontró una persona jurídica con CUIT: " + cuit);
        }

        PersonaJuridica pj = pjOpt.get();
        PersonaJuridicaDTO dto = new PersonaJuridicaDTO();
        dto.setRazonSocial(pj.getRazonSocial());
        dto.setCuit(pj.getCuit());
        dto.setTelefono(pj.getTelefono());

        if (pj.getDireccion() != null) {
            Direccion dir = pj.getDireccion();
            DireccionDTO dirDto = new DireccionDTO();
            dirDto.setCalle(dir.getCalle());
            dirDto.setNumero(dir.getNumero());
            dirDto.setLocalidad(dir.getLocalidad());
            dirDto.setProvincia(dir.getProvincia());
            dirDto.setPais(dir.getPais());
            dirDto.setDepartamento(dir.getDepartamento());
            dirDto.setPiso(dir.getPiso());
            dirDto.setCodigo(dir.getCodigo());
            dto.setDireccion(dirDto);
        }

        return dto;
    }
}
