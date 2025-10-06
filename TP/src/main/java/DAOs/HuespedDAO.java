/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOs;
import dominio.Huesped;
import repositorio.HuespedDTO;

/**
 *
 * @author mateo
 */
public interface HuespedDAO {
    public HuespedDTO consultarDocumento(String tipoDocumento, String numeroDocumento);
    public void modificarHuesped(HuespedDTO huespedModificado, Huesped huespedAntiguo);
    public boolean equals(Huesped h, Huesped huespedAntiguo);
    public void clonarDesdeDTO(HuespedDTO dto, Huesped h);

}
