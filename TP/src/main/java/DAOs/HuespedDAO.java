/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOs;
import dominio.Direccion;
import dominio.Huesped;
import repositorio.HuespedDTO;

/**
 *
 * @author mateo
 */
public interface HuespedDAO {
    public void guardarHuesped(Huesped huesped);
    public HuespedDTO consultarDocumento(String tipoDocumento, String numeroDocumento);
    public void modificarHuesped(HuespedDTO huespedModificado, Huesped huespedAntiguo);
    public void modificarHuesped(HuespedDTO huespedModificado, Huesped huespedAntiguo, Direccion direccionNueva);
    public boolean equals(Huesped h, Huesped huespedAntiguo);
    public void clonarDesdeDTO(HuespedDTO dto, Huesped h);
}
