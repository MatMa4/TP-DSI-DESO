/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.DAOs;

import TP_Back.appSpringTP.DTOs.HuespedDTO;
import TP_Back.appSpringTP.excepciones.HuespedNoEncontradoException;
import TP_Back.appSpringTP.mappers.HuespedMapper;
import TP_Back.appSpringTP.modelo.huesped.Huesped;
import TP_Back.appSpringTP.repositorios.repositorioHuesped;
import java.util.List;
import java.util.Optional;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

/**
 *
 * @author mateo
 */
@Service
@AllArgsConstructor
public class HuespedDAOImpl implements HuespedDAO {
    @Autowired
    private final repositorioHuesped repoHuesped;
    @Autowired
    private final HuespedMapper huespedMapper;
    @Autowired
    private final JdbcTemplate jdbcTemplate;

    @Override
    public Huesped save(Huesped huesp){
        return repoHuesped.save(huesp);
    }
    @Override
    public Optional<HuespedDTO> consultarDocumento(String tipoDocumento, String numeroDocumento){
        return repoHuesped.findByIdTipoDocumentoAndIdNumeroDocumento(tipoDocumento, numeroDocumento).map(huespedMapper::toDTO);
    }
    @Override
    public List<HuespedDTO> findAll(){
        return huespedMapper.toDTOList(repoHuesped.findAll());
    }
    @Override
    public List<HuespedDTO> buscarHuesped(HuespedDTO h){
        List<Huesped> huespedes;
        huespedes = repoHuesped.buscarHuespedes(h.getNombre(), h.getApellido(), h.getTipoDocumento(), h.getNumeroDocumento());
        if(huespedes.isEmpty()){
            throw new HuespedNoEncontradoException("No hay ningún guesped que cumpla con los requisitos de búsqueda: " + h.getNombre() + " " + h.getApellido() + " " + h.getTipoDocumento() + " " + h.getNumeroDocumento());
        }
        return huespedMapper.toDTOList(huespedes);
    }
    @Override
    public HuespedDTO guardar(HuespedDTO huesp){
        return huespedMapper.toDTO(repoHuesped.save(huespedMapper.toEntity(huesp)));
    }
    @Override
    public void modificarIDHuesped(HuespedDTO huespedModificado, HuespedDTO huespedOriginal){
        String sql = "UPDATE HUESPED " +
                     "SET NUMERO_DOCUMENTO = ?, TIPO_DOCUMENTO = ? " +
                     "WHERE NUMERO_DOCUMENTO = ? AND TIPO_DOCUMENTO = ?";
        int filasAfectadas = jdbcTemplate.update(sql, huespedModificado.getNumeroDocumento(), huespedModificado.getTipoDocumento(), huespedOriginal.getNumeroDocumento(), huespedOriginal.getTipoDocumento());
        if (filasAfectadas == 0) {
            throw new HuespedNoEncontradoException("No se encontró huésped con documento "
                    + huespedOriginal.getTipoDocumento() + " " + huespedOriginal.getNumeroDocumento());
        }

    }

    @Override
    public void eliminar(HuespedDTO huesp){
        repoHuesped.delete(huespedMapper.toEntity(huesp));
    }
}
