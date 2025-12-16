package TP_Back.appSpringTP.DTOs;

import java.util.Date;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReservaDTO {
    private Integer idReserva;
    private Date fechaInicio;
    private Date fechaFin;
    private String estado;
    private String nombre;
    private String apellido;
    private String telefono;
    private Integer habitacionNumero;
    private String tipoHabitacion;

    public ReservaDTO() {
    }

}
