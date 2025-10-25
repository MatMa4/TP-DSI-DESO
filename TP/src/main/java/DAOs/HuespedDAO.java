/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOs;
import dominio.Direccion;
import dominio.Huesped;
import java.util.List;
import repositorio.HuespedDTO;

/**
 *
 * @author mateo
 */
public interface HuespedDAO {
    public void guardarHuesped(Huesped huesped);
    public HuespedDTO consultarDocumento(String tipoDocumento, String numeroDocumento);
    public void modificarHuesped(HuespedDTO huespedModificado, HuespedDTO huespedAntiguo);
    public void modificarHuesped(HuespedDTO huespedModificado, HuespedDTO huespedAntiguo, Direccion direccionNueva);
    public boolean equalsDTO(Huesped h, HuespedDTO huespedAntiguo);
    public void clonarDesdeDTO(HuespedDTO dto, Huesped h);
    public void eliminar(HuespedDTO huespedDTO);
    public List<HuespedDTO> buscarHuesped(String apellido, String nombre, String tipoDoc, String nroDoc);
}
