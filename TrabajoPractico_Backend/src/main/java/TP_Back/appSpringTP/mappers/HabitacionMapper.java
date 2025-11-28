/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.mappers;
import TP_Back.appSpringTP.DTOs.HabitacionDTO;
import TP_Back.appSpringTP.modelo.habitacion.DobleEstandar;
import TP_Back.appSpringTP.modelo.habitacion.DobleSuperior;
import TP_Back.appSpringTP.modelo.habitacion.Habitacion;
import TP_Back.appSpringTP.modelo.habitacion.IndividualEstandar;
import TP_Back.appSpringTP.modelo.habitacion.Suite;
import TP_Back.appSpringTP.modelo.habitacion.SuperiorFamilyPlan;
import org.springframework.stereotype.Component;
/**
 *
 * @author mateo
 */


@Component
public class HabitacionMapper {

    // --- Habitacion → HabitacionDTO ---
    public HabitacionDTO toDTO(Habitacion habitacion) {
        if (habitacion == null) return null;

        HabitacionDTO dto = new HabitacionDTO();
        dto.setNumero(habitacion.getNumero());

        if (habitacion instanceof Suite h) {
            dto.setTipoHabitacion("Suite");
            dto.setCostoPorNoche(h.getCostoPorNoche());
            dto.setCapacidad(h.getCapacidad());
            dto.setEstado(h.getEstado());
            dto.setDescripcion(h.getDescripcion());
            dto.setCamaKingsize(h.getCamaKingsize());
        } else if (habitacion instanceof DobleEstandar h) {
            dto.setTipoHabitacion("DobleEstándar");
            dto.setCostoPorNoche(h.getCostoPorNoche());
            dto.setCapacidad(h.getCapacidad());
            dto.setEstado(h.getEstado());
            dto.setDescripcion(h.getDescripcion());
            dto.setCamaDoble(h.getCamaDobles());
            dto.setCamasIndividuales(h.getCamasIndividuales());
        } else if (habitacion instanceof DobleSuperior h) {
            dto.setTipoHabitacion("DobleSuperior");
            dto.setCostoPorNoche(h.getCostoPorNoche());
            dto.setCapacidad(h.getCapacidad());
            dto.setEstado(h.getEstado());
            dto.setDescripcion(h.getDescripcion());
            dto.setCamaDoble(h.getCamaDobles());
            dto.setCamasIndividuales(h.getCamasIndividuales());
        } else if (habitacion instanceof IndividualEstandar h) {
            dto.setTipoHabitacion("IndividualEstándar");
            dto.setCostoPorNoche(h.getCostoPorNoche());
            dto.setCapacidad(h.getCapacidad());
            dto.setEstado(h.getEstado());
            dto.setDescripcion(h.getDescripcion());
            dto.setCamasIndividuales(h.getCamasIndividuales());
        } else if (habitacion instanceof SuperiorFamilyPlan h) {
            dto.setTipoHabitacion("SuperiorFamilyPlan");
            dto.setCostoPorNoche(h.getCostoPorNoche());
            dto.setCapacidad(h.getCapacidad());
            dto.setEstado(h.getEstado());
            dto.setDescripcion(h.getDescripcion());
            dto.setCamaDoble(h.getCamaDobles());
            dto.setCamasIndividuales(h.getCamasIndividuales());
        }

        return dto;
    }

    // --- HabitacionDTO → Habitacion ---
    public Habitacion toEntity(HabitacionDTO dto) {
        if (dto == null) return null;

        String tipo = dto.getTipoHabitacion();
        if (tipo == null) {
            throw new IllegalArgumentException("El tipo de habitación no puede ser nulo");
        }

        switch (tipo) {
            case "Suite" -> {
                Suite h = new Suite();
                h.setNumero(dto.getNumero());
                h.setCostoPorNoche(dto.getCostoPorNoche());
                h.setCapacidad(dto.getCapacidad());
                h.setEstado(dto.getEstado());
                h.setDescripcion(dto.getDescripcion());
                h.setCamaKingsize(dto.getCamaKingsize());
                return h;
            }
            case "DobleEstándar" -> {
                DobleEstandar h = new DobleEstandar();
                h.setNumero(dto.getNumero());
                h.setCostoPorNoche(dto.getCostoPorNoche());
                h.setCapacidad(dto.getCapacidad());
                h.setEstado(dto.getEstado());
                h.setDescripcion(dto.getDescripcion());
                h.setCamaDobles(dto.getCamaDoble());
                h.setCamasIndividuales(dto.getCamasIndividuales());
                return h;
            }
            case "DobleSuperior" -> {
                DobleSuperior h = new DobleSuperior();
                h.setNumero(dto.getNumero());
                h.setCostoPorNoche(dto.getCostoPorNoche());
                h.setCapacidad(dto.getCapacidad());
                h.setEstado(dto.getEstado());
                h.setDescripcion(dto.getDescripcion());
                h.setCamaDobles(dto.getCamaDoble());
                h.setCamasIndividuales(dto.getCamasIndividuales());
                return h;
            }
            case "IndividualEstándar" -> {
                IndividualEstandar h = new IndividualEstandar();
                h.setNumero(dto.getNumero());
                h.setCostoPorNoche(dto.getCostoPorNoche());
                h.setCapacidad(dto.getCapacidad());
                h.setEstado(dto.getEstado());
                h.setDescripcion(dto.getDescripcion());
                h.setCamasIndividuales(dto.getCamasIndividuales());
                return h;
            }
            case "SuperiorFamilyPlan" -> {
                SuperiorFamilyPlan h = new SuperiorFamilyPlan();
                h.setNumero(dto.getNumero());
                h.setCostoPorNoche(dto.getCostoPorNoche());
                h.setCapacidad(dto.getCapacidad());
                h.setEstado(dto.getEstado());
                h.setDescripcion(dto.getDescripcion());
                h.setCamaDobles(dto.getCamaDoble());
                h.setCamasIndividuales(dto.getCamasIndividuales());
                return h;
            }
            default -> throw new IllegalArgumentException("Tipo de habitación no reconocido: " + tipo);
        }
    }
}
