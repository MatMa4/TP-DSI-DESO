package TP_Back.appSpringTP.DTOs;

import java.util.Date;

public class FacturaDTO {
    private Integer numero;
    private Date fechaEmision;
    private String tipo;
    private float montoTotal;
    private float iva;
    private String estado;
    private Integer ocupacionId;
    private Integer pagoId;
    private Integer responsableId;

    public FacturaDTO() {
    }

    // Getters and Setters
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public Date getFechaEmision() { return fechaEmision; }
    public void setFechaEmision(Date fechaEmision) { this.fechaEmision = fechaEmision; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public float getMontoTotal() { return montoTotal; }
    public void setMontoTotal(float montoTotal) { this.montoTotal = montoTotal; }

    public float getIva() { return iva; }
    public void setIva(float iva) { this.iva = iva; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public Integer getOcupacionId() { return ocupacionId; }
    public void setOcupacionId(Integer ocupacionId) { this.ocupacionId = ocupacionId; }

    public Integer getPagoId() { return pagoId; }
    public void setPagoId(Integer pagoId) { this.pagoId = pagoId; }

    public Integer getResponsableId() { return responsableId; }
    public void setResponsableId(Integer responsableId) { this.responsableId = responsableId; }
}
