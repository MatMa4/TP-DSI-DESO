package TP_Back.appSpringTP.modelo.factura;

import TP_Back.appSpringTP.modelo.ocupacion.Ocupacion;
import TP_Back.appSpringTP.modelo.pago.Pago;
import TP_Back.appSpringTP.modelo.pago.ResponsableDePago;
import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "facturas")
public class Factura {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numero;

    @Temporal(TemporalType.DATE)
    private Date fechaEmision;

    private String tipo;
    private float montoTotal;
    private float iva;
    private String estado;

    @ManyToOne
    @JoinColumn(name = "ocupacion_id")
    private Ocupacion ocupacion;

    @OneToOne
    @JoinColumn(name = "pago_id")
    private Pago pago;

    @ManyToOne
    @JoinColumn(name = "responsable_id")
    private ResponsableDePago responsableDePago;

    public Factura() {
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

    public Ocupacion getOcupacion() { return ocupacion; }
    public void setOcupacion(Ocupacion ocupacion) { this.ocupacion = ocupacion; }

    public Pago getPago() { return pago; }
    public void setPago(Pago pago) { this.pago = pago; }

    public ResponsableDePago getResponsableDePago() { return responsableDePago; }
    public void setResponsableDePago(ResponsableDePago responsableDePago) { this.responsableDePago = responsableDePago; }
}
