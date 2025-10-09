/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;
import repositorio.HuespedDTO;

import java.util.ArrayList;
import java.util.List;
import dominio.Huesped;
import dominio.Direccion;

/**
 *
 * @author mateo
 */
public class HuespedDAOImpl implements HuespedDAO {
    private List<Huesped> huespedes; // o la fuente de datos que uses
    private static HuespedDAOImpl instancia;

    //Patrón Singleton
    private HuespedDAOImpl() {
        huespedes = new ArrayList<>();
    }

    public static HuespedDAOImpl getHuespedDAO() {
        if (instancia == null) {
            instancia = new HuespedDAOImpl();
        }
        return instancia;
    }

    @Override
    public HuespedDTO consultarDocumento(String tipoDocumento, String numeroDocumento) {
        for (Huesped huesped : huespedes) {
            if (huesped.getTipoDocumento().equals(tipoDocumento) && huesped.getNumeroDocumento().equals(numeroDocumento)) {
                HuespedDTO.Builder builder = new HuespedDTO.Builder()
                    .apellido(huesped.getApellido())
                    .nombre(huesped.getNombre())
                    .tipoDocumento(huesped.getTipoDocumento())
                    .numeroDocumento(huesped.getNumeroDocumento())
                    .fechaNacimiento(huesped.getFechaNacimiento())
                    .direccionHuesped(huesped.getDireccionHuesped())
                    .telefono(huesped.getTelefono())
                    .ocupacion(huesped.getOcupacion())
                    .nacionalidad(huesped.getNacionalidad());

                if (huesped.getEmail() != null) {
                    builder.email(huesped.getEmail());
                }
                if (huesped.getCuit() != null) {
                    builder.cuit(huesped.getCuit());
                }
                if (huesped.getPosicionIVA() != null) {
                    builder.posicionIVA(huesped.getPosicionIVA());
                }

                return builder.build();
            }
        }
        return null;
    }

    @Override
    public void modificarHuesped(HuespedDTO huespedModificado, Huesped huespedAntiguo){
         huespedes.stream()
        .filter(h -> equals(h, huespedAntiguo))
        .findFirst()
        .ifPresent(h -> clonarDesdeDTO(huespedModificado, h));
    }

    @Override
    public void modificarHuesped(HuespedDTO huespedModificado, Huesped huespedAntiguo, Direccion direccionNueva){
         huespedes.stream()
        .filter(h -> equals(h, huespedAntiguo))
        .findFirst()
        .ifPresent(h -> {clonarDesdeDTO(huespedModificado, h);
                         h.setDireccionHuesped(direccionNueva);});
    }

    @Override
    public boolean equals(Huesped h, Huesped huespedAntiguo) {
        return h.getApellido().equals(huespedAntiguo.getApellido()) && 
                h.getNombre().equals(huespedAntiguo.getNombre()) &&
                h.getTipoDocumento().equals(huespedAntiguo.getTipoDocumento()) &&
                h.getNumeroDocumento().equals(huespedAntiguo.getNumeroDocumento()) &&
                h.getFechaNacimiento().equals(huespedAntiguo.getFechaNacimiento()) &&
                h.getDireccionHuesped().equals(huespedAntiguo.getDireccionHuesped()) &&
                h.getTelefono().equals(huespedAntiguo.getTelefono()) &&
                ((h.getEmail() == null && huespedAntiguo.getEmail() == null) || 
                 (h.getEmail() != null && h.getEmail().equals(huespedAntiguo.getEmail()))) &&
                h.getOcupacion().equals(huespedAntiguo.getOcupacion()) &&
                h.getNacionalidad().equals(huespedAntiguo.getNacionalidad()) &&
                ((h.getCuit() == null && huespedAntiguo.getCuit() == null) || 
                 (h.getCuit() != null && h.getCuit().equals(huespedAntiguo.getCuit()))) &&
                ((h.getPosicionIVA() == null && huespedAntiguo.getPosicionIVA() == null) || 
                 (h.getPosicionIVA() != null && h.getPosicionIVA().equals(huespedAntiguo.getPosicionIVA())));
    }

    @Override
    public void clonarDesdeDTO(HuespedDTO dto, Huesped h) { //cambia todo menos la direccion
        h.setApellido(dto.getApellido());
        h.setNombre(dto.getNombre());
        h.setTipoDocumento(dto.getTipoDocumento());
        h.setNumeroDocumento(dto.getNumeroDocumento());
        h.setFechaNacimiento(dto.getFechaNacimiento());
        h.setTelefono(dto.getTelefono());
        h.setEmail(dto.getEmail()); //Ver si es null
        h.setOcupacion(dto.getOcupacion()); 
        h.setNacionalidad(dto.getNacionalidad());
        h.setCuit(dto.getCuit()); //Ver si es null
        h.setPosicionIVA(dto.getPosicionIVA()); //Ver si es null
    }
}