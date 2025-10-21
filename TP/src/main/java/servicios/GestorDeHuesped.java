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

    public static void modificarHuesped(HuespedDTO huespedModificado, HuespedDTO huespedAntiguo){
        //Se fija si las direcciones son iguales, si no lo son, le asigna la nueva direccion
        DireccionDAO direccionDAO = DireccionDAOImpl.getDireccionDAO();
        HuespedDAO huespedDAO= HuespedDAOImpl.getHuespedDAO();    
        if(!(equals(huespedAntiguo.getDireccionHuesped(), huespedModificado.getDireccionHuesped()))){
           huespedDAO.modificarHuesped(huespedModificado, huespedAntiguo, convertirADireccion(huespedModificado.getDireccionHuesped()));
           direccionDAO.agregarDireccion(convertirADireccion(huespedModificado.getDireccionHuesped()));
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
    
    public static void eliminarHuesped(HuespedDTO huespedDTO) throws HuespedNoEliminableException {
        
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
            throw new HuespedNoEncontradoException();
        }
        return encontrados;
    }
    
    public static boolean equals(DireccionDTO direc1, DireccionDTO direc2){
        return direc1.getCalle().equals(direc2.getCalle()) &&
               direc1.getNumero().equals(direc2.getNumero()) &&
               direc1.getDepartamento().equals(direc2.getDepartamento()) &&
               direc1.getPiso().equals(direc2.getPiso()) &&
               direc1.getCodigo().equals(direc2.getCodigo()) &&
               direc1.getLocalidad().equals(direc2.getLocalidad()) &&
               direc1.getProvincia().equals(direc2.getProvincia()) &&
               direc1.getPais().equals(direc2.getPais());
    }

    
}