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
import TP_Back.appSpringTP.DAOs.HuespedDAO;
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
import TP_Back.appSpringTP.DAOs.ResponsablePagoDAO;
import TP_Back.appSpringTP.excepciones.HuespedNoEliminableException;
import TP_Back.appSpringTP.modelo.pago.PersonaFisica;
import lombok.AllArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;

@Service
@AllArgsConstructor
public class GestorHuespedes {
    @Autowired
    private final HuespedDAO huespedDAO;
    @Autowired
    private final DireccionDAO direccionDAO;
    @Autowired
    private final PersonaFisicaDAO personaFisicaDAO;
    @Autowired
    private final ResponsablePagoDAO responsablePagoDAO;

    public HuespedDTO registrarHuesped(HuespedDTO h) throws HuespedNoEncontradoException {
        DireccionDTO direccionDto = h.getDireccionHuesped();
        Direccion direccion = new Direccion();
        direccion.setDepartamento(direccionDto.getDepartamento());
        direccion.setCodigo(direccionDto.getCodigo());
        direccion.setPiso(direccionDto.getPiso());
        direccion.setId(direccionDto.getCalle(), direccionDto.getNumero(), direccionDto.getLocalidad(),
                direccionDto.getProvincia(), direccionDto.getPais());
        try {
            direccionDAO.save(direccion);
        } catch (Exception e) {
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
        try {
            huespedDAO.save(huesped);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al registrar huésped", e);
        }

        PersonaFisica personaFisica = new PersonaFisica();
        personaFisica.setHuesped(huesped);
        try {
            personaFisicaDAO.save(personaFisica);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al registrar el responsable de pago", e);
        }
        Optional<PersonaFisica> buscarPersonaFisica = personaFisicaDAO.getByHuesped(huesped.getTipoDocumento(),
                huesped.getNumeroDocumento());
        if (buscarPersonaFisica.isEmpty()) {
            throw new RuntimeException("Responsable de pago no guardado");
        }
        Optional<HuespedDTO> resultado = huespedDAO.consultarDocumento(huesped.getTipoDocumento(),
                huesped.getNumeroDocumento());
        if (resultado.isPresent()) {
            return resultado.get();
        } else {
            throw new HuespedNoEncontradoException(
                    "No se encontró huésped con documento " + huesped.getNumeroDocumento());
        }
    }

    public HuespedDTO modificarHuesped(List<HuespedDTO> huespedes) throws HuespedNoEncontradoException {
        huespedDAO.modificarIDHuesped(huespedes.get(0), huespedes.get(1));

        DireccionDTO direccionDto = huespedes.get(0).getDireccionHuesped();
        Direccion direccion = new Direccion();
        direccion.setDepartamento(direccionDto.getDepartamento());
        direccion.setCodigo(direccionDto.getCodigo());
        direccion.setPiso(direccionDto.getPiso());
        direccion.setId(direccionDto.getCalle(), direccionDto.getNumero(), direccionDto.getLocalidad(),
                direccionDto.getProvincia(), direccionDto.getPais());
        try {
            direccionDAO.save(direccion);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al modificar huésped", e);
        }

        Huesped huesped = new Huesped();
        huesped.setNombre(huespedes.get(0).getNombre());
        huesped.setApellido(huespedes.get(0).getApellido());
        huesped.setTipoDocumento(huespedes.get(0).getTipoDocumento());
        huesped.setNumeroDocumento(huespedes.get(0).getNumeroDocumento());
        huesped.setFechaNacimiento(huespedes.get(0).getFechaNacimiento());
        huesped.setTelefono(huespedes.get(0).getTelefono());
        huesped.setEmail(huespedes.get(0).getEmail());
        huesped.setOcupacion(huespedes.get(0).getOcupacion());
        huesped.setNacionalidad(huespedes.get(0).getNacionalidad());
        huesped.setCuit(huespedes.get(0).getCuit());
        huesped.setPosicionIVA(huespedes.get(0).getPosicionIVA());
        huesped.setAlojado(huespedes.get(0).getAlojado());
        huesped.setDireccionHuesped(direccion);

        try {
            huespedDAO.save(huesped);
        } catch (Exception e) {
            throw new RuntimeException("Error inesperado al guardar el huésped modificado", e);
        }
        Optional<HuespedDTO> resultado = huespedDAO.consultarDocumento(huespedes.get(0).getTipoDocumento(),
                huespedes.get(0).getNumeroDocumento());

        if (resultado.isPresent()) {
            return resultado.get();
        } else {
            throw new HuespedNoEncontradoException(
                    "No se encontró huésped con documento " + huespedes.get(0).getNumeroDocumento());
        }
    }

    public Boolean eliminarHuesped(HuespedDTO huesped) {
        Optional<HuespedDTO> huespedAEliminar = huespedDAO.consultarDocumento(huesped.getTipoDocumento(),
                huesped.getNumeroDocumento());
        if (huespedAEliminar.isEmpty()) {
            throw new HuespedNoEncontradoException();
        }
        if (!huespedAEliminar.get().getAlojado()) {
            Integer idResponsable = personaFisicaDAO.getIdResponsablePagoConHuesped(huesped.getTipoDocumento(),
                    huesped.getNumeroDocumento());
            try {
                responsablePagoDAO.eliminar(idResponsable);
            } catch (DataIntegrityViolationException e) {
                throw e;
            }
            huespedDAO.eliminar(huesped);

            Optional<HuespedDTO> huespedEncontrado = huespedDAO.consultarDocumento(huesped.getTipoDocumento(),
                    huesped.getNumeroDocumento());
            return huespedEncontrado.isEmpty();
        } else {
            throw new HuespedNoEliminableException();
        }
    }

    public List<HuespedDTO> buscarHuesped(String tipo, String numero, String nombre, String apellido) {
        HuespedDTO huesped = new HuespedDTO();
        huesped.setTipoDocumento(tipo);
        huesped.setNumeroDocumento(numero);
        huesped.setApellido(apellido);
        huesped.setNombre(nombre);
        List<HuespedDTO> huespedes;
        try {
            huespedes = huespedDAO.buscarHuesped(huesped);
        } catch (HuespedNoEncontradoException e) {
            throw e;
        }
        return huespedes;
    }

    public void consultarDocumento(String tipoDocumento, String numeroDocumento) {
        Optional<HuespedDTO> huesped = huespedDAO.consultarDocumento(tipoDocumento, numeroDocumento);
        if (huesped.isPresent()) {
            throw new HuespedExistenteException("Huesped existente");
        }
    }

    public void huespedExistente(String tipoModificado, String numeroModificado, String tipoOriginal,
            String numeroOriginal) {
        if (!tipoModificado.equals(tipoOriginal) || !numeroModificado.equals(numeroOriginal)) {
            Optional<HuespedDTO> huesped = huespedDAO.consultarDocumento(tipoModificado, numeroModificado);
            if (huesped.isPresent()) {
                throw new HuespedExistenteException("Huesped existente");
            }
        }
    }

    public List<HuespedDTO> listarTodosHuespedes() {
        return huespedDAO.findAll();
    }
}
