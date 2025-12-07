package TP_Back.appSpringTP.DTOs;

import TP_Back.appSpringTP.DTOs.ocupacion.OcupacionDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class HabitacionDetalleDTO {
    private HabitacionDTO habitacion;
    private List<OcupacionDTO> ocupaciones;
    private List<ReservaDTO> reservas;
}
