package TP_Back.appSpringTP.DTOs;

import TP_Back.appSpringTP.DTOs.ocupacion.ConsumoDTO;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SolicitudFacturacionJuridicaDTO {
    private Long idOcupacion;
    private List<ConsumoDTO> listaConsumos;
    private String cuitResponsable;
}
