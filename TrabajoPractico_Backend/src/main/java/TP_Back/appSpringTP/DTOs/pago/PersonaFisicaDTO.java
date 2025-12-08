package TP_Back.appSpringTP.DTOs.pago;

import TP_Back.appSpringTP.DTOs.HuespedDTO;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonaFisicaDTO extends ResponsableDePagoDTO {
    private Integer id_per_fisica;
    private HuespedDTO huesped;
}
