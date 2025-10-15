/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;
import Excepcion.HuespedEliminadoCorrectamenteException;
import repositorio.HuespedDTO;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.io.File;
import java.io.IOException;


import java.util.ArrayList;
import java.util.List;
import dominio.Huesped;
import dominio.Direccion;
import Excepcion.HuespedNoEncontradoException;
import java.util.Iterator;
import java.util.Scanner;

/**
 *
 * @author mateo
 */
public class HuespedDAOImpl implements HuespedDAO {
    private List<Huesped> huespedes; // o la fuente de datos que uses
    private static HuespedDAOImpl instancia;

    //Patrón Singleton
    public HuespedDAOImpl() {
        huespedes = cargarListaDesdeJSON();
    }

    public static HuespedDAOImpl getHuespedDAO() {
        if (instancia == null) {
            instancia = new HuespedDAOImpl();
        }
        return instancia;
    }

    private List<Huesped> cargarListaDesdeJSON() {
        ObjectMapper mapper = new ObjectMapper();
        File archivo = new File("listaHuespedes.json");

        if (!archivo.exists()) return new ArrayList<>();

        try {
            return List.of(mapper.readValue(archivo, Huesped[].class));
        } catch (IOException e) {
            throw new RuntimeException("Error al cargar la lista de huéspedes desde JSON", e);
        }
    }

    @Override
    public void guardarHuesped(Huesped huesped) {
        if (huesped == null) {
            throw new IllegalArgumentException("El huésped no puede ser null");
        }

        huespedes.add(huesped);
        guardarListaEnJSON(); // persistencia automática
    }

    private void guardarListaEnJSON() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT); // para que quede legible

        try {
            mapper.writeValue(new File("listaHuespedes.json"), huespedes);
        } catch (IOException e) {
            throw new RuntimeException("Error al guardar la lista de huéspedes en JSON", e);
        }
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
        throw new HuespedNoEncontradoException(tipoDocumento, numeroDocumento);
    }

    @Override
    public void modificarHuesped(HuespedDTO huespedModificado, Huesped huespedAntiguo){
        //huespedAntiguo siempre debería estar, por lo que no hay excepción
         huespedes.stream()
        .filter(h -> equals(h, huespedAntiguo))
        .findFirst()
        .ifPresent(h -> clonarDesdeDTO(huespedModificado, h));
    }

    @Override
    public void modificarHuesped(HuespedDTO huespedModificado, Huesped huespedAntiguo, Direccion direccionNueva){
        //huespedAntiguo siempre debería estar, por lo que no hay excepción 
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
    
    @Override
    public void eliminar(HuespedDTO huespedDTO) {
        Iterator<Huesped> it = huespedes.iterator();
        boolean eliminado = false;

        while (it.hasNext()) {
            Huesped h = it.next();
            if (h.getTipoDocumento().equals(huespedDTO.getTipoDocumento()) &&
                h.getNumeroDocumento().equals(huespedDTO.getNumeroDocumento())) {

                Scanner in = new Scanner(System.in);
                System.out.print("Los datos del huésped "+ huespedDTO.getNombre()+ ", " +huespedDTO.getApellido()+" cuyo tipo de documento es " + huespedDTO.getTipoDocumento() + " numero "+ huespedDTO.getNumeroDocumento()+ "serán eliminados del sistema");
                String respuesta = in.nextLine();
                if("ELIMINAR".equals(respuesta)){
                 it.remove(); 
                eliminado = true;
                throw new HuespedEliminadoCorrectamenteException(huespedDTO.getNombre(),
                        huespedDTO.getApellido(),huespedDTO.getTipoDocumento(),huespedDTO.getNumeroDocumento());
                }
            }
        }
    }

}