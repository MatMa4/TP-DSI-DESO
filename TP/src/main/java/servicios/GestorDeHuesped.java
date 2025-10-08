/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package servicios;

import DAOs.HuespedDAOImpl;
import dominio.Huesped;
import repositorio.HuespedDTO;
import DAOs.HuespedDAO;

/**
 *
 * @author mateo
 */
public class GestorDeHuesped {

    public HuespedDTO consultarDocumento(String tipoDocumento, String numeroDocumento){
        HuespedDAO huespedDAO= HuespedDAOImpl.getHuespedDAO();
        return huespedDAO.consultarDocumento(tipoDocumento, numeroDocumento);
    }

    public void modificarHuesped(HuespedDTO huespedModificado, Huesped huespedAntiguo){

    }
    
}

