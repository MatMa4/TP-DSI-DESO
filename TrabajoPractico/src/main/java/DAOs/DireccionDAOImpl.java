/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package DAOs;

import java.util.List;
import dominio.Direccion;


/**
 *
 * @author mateo
 */
public class DireccionDAOImpl implements DireccionDAO {
    private static List<Direccion> direcciones = new java.util.ArrayList<>();
    private static DireccionDAOImpl instancia;

    //Patrón Singleton
    private DireccionDAOImpl() {
    }

    public static DireccionDAOImpl getDireccionDAO() {
        if (instancia == null) {
            instancia = new DireccionDAOImpl();
            direcciones = new java.util.ArrayList<>();
        }
        return instancia;
    }

    @Override
    public boolean equals(Direccion direc1, Direccion direc2){
        return direc1.getCalle().equals(direc2.getCalle()) &&
               direc1.getNumero().equals(direc2.getNumero()) &&
               direc1.getDepartamento().equals(direc2.getDepartamento()) &&
               direc1.getPiso().equals(direc2.getPiso()) &&
               direc1.getCodigo().equals(direc2.getCodigo()) &&
               direc1.getLocalidad().equals(direc2.getLocalidad()) &&
               direc1.getProvincia().equals(direc2.getProvincia()) &&
               direc1.getPais().equals(direc2.getPais());
    }

    @Override
    public void agregarDireccion(Direccion direccion) {
        direcciones.add(direccion);
    }
    
}
