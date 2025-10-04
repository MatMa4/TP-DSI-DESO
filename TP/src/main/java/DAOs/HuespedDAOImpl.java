/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;
import repositorio.HuespedDTO;
import java.util.List;
import dominio.Huesped;

/**
 *
 * @author mateo
 */
public class HuespedDAOImpl implements HuespedDAO {
    private List<Huesped> huespedes; // o la fuente de datos que uses

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

    }
}