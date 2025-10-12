/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;


import dominio.Direccion;
import dominio.Huesped;
import repositorio.HuespedDTO;
import repositorio.DireccionDTO;
import DAOs.HuespedDAO;
import DAOs.HuespedDAOImpl;
import DAOs.DireccionDAO;
import DAOs.DireccionDAOImpl;


/**
 *
 * @author mateo
 */
public class GestorDeHuesped {

    public static HuespedDTO consultarDocumento(String tipoDocumento, String numeroDocumento){
        HuespedDAO huespedDAO= HuespedDAOImpl.getHuespedDAO();
        return huespedDAO.consultarDocumento(tipoDocumento, numeroDocumento);
    }

    public static void modificarHuesped(HuespedDTO huespedModificado, Huesped huespedAntiguo){
        //Se fija si las direcciones son iguales, si no lo son, le asigna la nueva direccion
        DireccionDAO direccionDAO = DireccionDAOImpl.getDireccionDAO();
        HuespedDAO huespedDAO= HuespedDAOImpl.getHuespedDAO();
        Direccion direccionNueva = convertirADireccion(huespedModificado.getDireccionHuesped());    
        if(!(direccionDAO.equals(huespedAntiguo.getDireccionHuesped(), direccionNueva))){
           huespedDAO.modificarHuesped(huespedModificado, huespedAntiguo, direccionNueva);
           direccionDAO.agregarDireccion(direccionNueva);
        }else{
            huespedDAO.modificarHuesped(huespedModificado, huespedAntiguo);
        } 
    }

    private static Direccion convertirADireccion(DireccionDTO dirDTO){
        Direccion direccion = new Direccion(dirDTO.getCalle(), dirDTO.getNumero(), dirDTO.getDepartamento(),
                                            dirDTO.getPiso(), dirDTO.getCodigo(), dirDTO.getLocalidad(),
                                            dirDTO.getProvincia(), dirDTO.getPais());
        return direccion;
    }

    public static void registrarHuesped(HuespedDTO huespedDTO){
        HuespedDAO huespedDAO= HuespedDAOImpl.getHuespedDAO();
        DireccionDAO direccionDAO = DireccionDAOImpl.getDireccionDAO();
        Direccion direccionNueva = convertirADireccion(huespedDTO.getDireccionHuesped()); 
        Huesped.Builder builder = new Huesped.Builder()
                    .apellido(huespedDTO.getApellido())
                    .nombre(huespedDTO.getNombre())
                    .tipoDocumento(huespedDTO.getTipoDocumento())
                    .numeroDocumento(huespedDTO.getNumeroDocumento())
                    .fechaNacimiento(huespedDTO.getFechaNacimiento())
                    .direccionHuesped(direccionNueva)
                    .telefono(huespedDTO.getTelefono())
                    .ocupacion(huespedDTO.getOcupacion())
                    .nacionalidad(huespedDTO.getNacionalidad());

                if (huespedDTO.getEmail() != null) {
                    builder.email(huespedDTO.getEmail());
                }
                if (huespedDTO.getCuit() != null) {
                    builder.cuit(huespedDTO.getCuit());
                }
                if (huespedDTO.getPosicionIVA() != null) {
                    builder.posicionIVA(huespedDTO.getPosicionIVA());
                }

        Huesped huespedNuevo = builder.build();

        direccionDAO.agregarDireccion(direccionNueva);
        huespedDAO.guardarHuesped(huespedNuevo);
    }

    
}

