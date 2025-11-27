package TP_Back.appSpringTP.DTOs;

import java.util.Date;

public class NotaDeCreditoDTO {
    private Integer numero;
    private Date fecha;
    private float importe;
    private Integer facturaNumero;

    public NotaDeCreditoDTO() {
    }

    // Getters and Setters
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public float getImporte() { return importe; }
    public void setImporte(float importe) { this.importe = importe; }

    public Integer getFacturaNumero() { return facturaNumero; }
    public void setFacturaNumero(Integer facturaNumero) { this.facturaNumero = facturaNumero; }
}
