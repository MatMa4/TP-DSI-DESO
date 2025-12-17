package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.modelo.factura.Factura;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FacturaDAO extends JpaRepository<Factura, Integer> {
}
