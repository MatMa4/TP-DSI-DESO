package TP_Back.appSpringTP.DTOs;

import TP_Back.appSpringTP.DTOs.ocupacion.ConsumoDTO;
import java.util.List;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SolicitudFacturacionDTO {
    private Long idOcupacion;
    private List<ConsumoDTO> listaConsumos;
    private HuespedDTO huesped;
}
