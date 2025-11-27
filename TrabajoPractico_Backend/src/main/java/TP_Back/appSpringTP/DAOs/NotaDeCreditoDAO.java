package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.modelo.factura.NotaDeCredito;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotaDeCreditoDAO extends JpaRepository<NotaDeCredito, Integer> {
}
