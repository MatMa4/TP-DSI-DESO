/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Interface.java to edit this template
 */
package DAOs;

import dominio.Habitacion;

/**
 *
 * @author mateo
 */
public interface HabitacionDAO {
    public Habitacion crearHabitacion();
    public void actualizarEstadoHabitacion();
    public Habitacion obtenerHabitacion();
}
