package TP_Back.appSpringTP.repositorios;

import TP_Back.appSpringTP.modelo.ocupacion.Consumo;
import org.springframework.data.jpa.repository.JpaRepository;

public interface repositorioConsumo extends JpaRepository<Consumo, Integer> {
}
