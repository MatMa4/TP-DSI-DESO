/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOs;

import dominio.Direccion;

/**
 *
 * @author mateo
 */
public interface DireccionDAO {
    public boolean equals(Direccion direc1, Direccion direc2);
    public void agregarDireccion(Direccion direccion);
}
