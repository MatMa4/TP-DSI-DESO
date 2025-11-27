package TP_Back.appSpringTP.modelo.factura;

import jakarta.persistence.*;
import java.util.Date;

@Entity
@Table(name = "notas_de_credito")
public class NotaDeCredito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer numero;

    @Temporal(TemporalType.DATE)
    private Date fecha;

    private float importe;

    @ManyToOne
    @JoinColumn(name = "factura_id")
    private Factura factura;

    public NotaDeCredito() {
    }

    // Getters and Setters
    public Integer getNumero() { return numero; }
    public void setNumero(Integer numero) { this.numero = numero; }

    public Date getFecha() { return fecha; }
    public void setFecha(Date fecha) { this.fecha = fecha; }

    public float getImporte() { return importe; }
    public void setImporte(float importe) { this.importe = importe; }

    public Factura getFactura() { return factura; }
    public void setFactura(Factura factura) { this.factura = factura; }
}
