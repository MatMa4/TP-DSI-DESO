/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.mappers;

/**
 *
 * @author mateo
 */
import TP_Back.appSpringTP.DTOs.ocupacion.ConsumoDTO;
import TP_Back.appSpringTP.DTOs.ocupacion.OcupacionDTO;
import TP_Back.appSpringTP.modelo.ocupacion.Consumo;
import TP_Back.appSpringTP.modelo.ocupacion.Ocupacion;
import java.util.stream.Collectors;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class OcupacionMapper {

    @Autowired
    private final HuespedMapper huespedMapper;

    @Autowired
    private final HabitacionMapper habitacionMapper;

    // --- Consumo Mapper interno ---
    private ConsumoDTO toConsumoDTO(Consumo consumo) {
        if (consumo == null) return null;
        ConsumoDTO dto = new ConsumoDTO();
        dto.setIdConsumo(consumo.getConsumoId());
        dto.setTipoServicio(consumo.getTipoServicio());
        dto.setDetalle(consumo.getDetalle());
        dto.setMonto(consumo.getMonto());
        dto.setFacturado(consumo.isFacturado());
        return dto;
    }

    private Consumo toConsumoEntity(ConsumoDTO dto) {
        if (dto == null) return null;
        Consumo consumo = new Consumo();
        consumo.setTipoServicio(dto.getTipoServicio());
        consumo.setDetalle(dto.getDetalle());
        consumo.setMonto(dto.getMonto());
        return consumo;
    }

    // --- Ocupacion → OcupacionDTO ---
    public OcupacionDTO toDTO(Ocupacion ocupacion) {
        if (ocupacion == null) return null;

        OcupacionDTO dto = new OcupacionDTO();
        dto.setFechaInicio(ocupacion.getFechaInicio());
        dto.setFechaFin(ocupacion.getFechaFin());
        dto.setCheckIn(ocupacion.getCheckIn());
        dto.setCheckOut(ocupacion.getCheckOut());

        // Habitacion
        if (ocupacion.getHabitacion() != null) {
            dto.setHabitacion(habitacionMapper.toDTO(ocupacion.getHabitacion()));
        }
        dto.setFacturada(ocupacion.isFacturada());

        // Consumos
        if (ocupacion.getConsumos() != null) {
            dto.setConsumos(
                ocupacion.getConsumos().stream()
                    .map(this::toConsumoDTO)
                    .collect(Collectors.toList())
            );
        }

        // Huespedes
        if (ocupacion.getHuespedes() != null) {
            dto.setHuespedes(
                ocupacion.getHuespedes().stream()
                    .map(huespedMapper::toDTO)
                    .collect(Collectors.toList())
            );
        }

        return dto;
    }

    // --- OcupacionDTO → Ocupacion ---
    public Ocupacion toEntity(OcupacionDTO dto) {
        if (dto == null) return null;

        Ocupacion ocupacion = new Ocupacion();
        ocupacion.setFechaInicio(dto.getFechaInicio());
        ocupacion.setFechaFin(dto.getFechaFin());
        ocupacion.setCheckIn(dto.getCheckIn());
        ocupacion.setCheckOut(dto.getCheckOut());

        // Habitacion
        if (dto.getHabitacion() != null) {
            ocupacion.setHabitacion(habitacionMapper.toEntity(dto.getHabitacion()));
        }

        // Consumos
        if (dto.getConsumos() != null) {
            ocupacion.setConsumos(
                dto.getConsumos().stream()
                    .map(this::toConsumoEntity)
                    .collect(Collectors.toList())
            );
        }

        // Huespedes
        if (dto.getHuespedes() != null) {
            ocupacion.setHuespedes(
                dto.getHuespedes().stream()
                    .map(huespedMapper::toEntity)
                    .collect(Collectors.toList())
            );
        }

        return ocupacion;
    }
}