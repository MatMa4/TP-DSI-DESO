package servicios;


import dominio.Direccion;
import dominio.Huesped;
import repositorio.HuespedDTO;
import repositorio.DireccionDTO;
import DAOs.HuespedDAO;
import DAOs.HuespedDAOImpl;
import DAOs.DireccionDAO;
import DAOs.DireccionDAOImpl;
import Excepcion.HuespedNoEliminableException;
import Excepcion.HuespedNoEncontradoException;
import java.util.List;


/**
 *
 * @author mateo
 */
public class GestorDeHuesped {
    private HuespedDAO huespedDAO;
    
    public GestorDeHuesped() {
        this.huespedDAO = new HuespedDAOImpl(); // inicialización directa
    }

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
    public void eliminarHuesped(HuespedDTO huespedDTO) throws HuespedNoEliminableException {
        
        if (!huespedDTO.estaAlojado()) {
            huespedDAO.eliminar(huespedDTO);
            
        }else{
            throw new HuespedNoEliminableException();
        }
    }
     public List<Huesped> buscarHuesped(String apellido, String nombre, String tipoDoc, String nroDoc)
            throws HuespedNoEncontradoException {
        List<Huesped> encontrados = huespedDAO.buscarHuesped(apellido, nombre, tipoDoc, nroDoc);
        if (encontrados.isEmpty()) {
            throw new HuespedNoEncontradoException("No se encontraron huéspedes con esos criterios.");
        }
        return encontrados;
    }
    

    
}