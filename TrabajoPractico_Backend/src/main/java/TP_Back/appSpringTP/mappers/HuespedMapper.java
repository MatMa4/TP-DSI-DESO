/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package TP_Back.appSpringTP.mappers;

import TP_Back.appSpringTP.DTOs.DireccionDTO;
import TP_Back.appSpringTP.DTOs.HuespedDTO;
import TP_Back.appSpringTP.modelo.direccion.Direccion;
import TP_Back.appSpringTP.modelo.direccion.DireccionId;
import TP_Back.appSpringTP.modelo.huesped.Huesped;
import TP_Back.appSpringTP.modelo.huesped.HuespedId;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
@Component
public class HuespedMapper {

    public Huesped toEntity(HuespedDTO dto) {
        if (dto == null) return null;

        return Huesped.builder()
                .id(HuespedId.builder()
                    .numeroDocumento(dto.getNumeroDocumento())
                    .tipoDocumento(dto.getTipoDocumento())
                    .build()
                )
                .apellido(dto.getApellido())
                .nombre(dto.getNombre())
                .fechaNacimiento(dto.getFechaNacimiento())
                .telefono(dto.getTelefono())
                .email(dto.getEmail())
                .ocupacion(dto.getOcupacion())
                .nacionalidad(dto.getNacionalidad())
                .cuit(dto.getCuit())
                .posicionIVA(dto.getPosicionIVA())
                .alojado(dto.getAlojado())
                .direccion(toEntity(dto.getDireccionHuesped()))
                .build();
    }

    public HuespedDTO toDTO(Huesped entity) {
        if (entity == null) return null;

        return HuespedDTO.builder()
                .numeroDocumento(entity.getNumeroDocumento())
                .tipoDocumento(entity.getTipoDocumento())
                .apellido(entity.getApellido())
                .nombre(entity.getNombre())
                .fechaNacimiento(entity.getFechaNacimiento())
                .telefono(entity.getTelefono())
                .email(entity.getEmail())
                .ocupacion(entity.getOcupacion())
                .nacionalidad(entity.getNacionalidad())
                .cuit(entity.getCuit())
                .posicionIVA(entity.getPosicionIVA())
                .alojado(entity.getAlojado())
                .direccion(toDTO(entity.getDireccionHuesped()))
                .build();
    }

    public Direccion toEntity(DireccionDTO dto) {
        if (dto == null) return null;

        return Direccion.builder()
                .id(DireccionId.builder()
                        .calle(dto.getCalle())
                        .numero(dto.getNumero())
                        .localidad(dto.getLocalidad())
                        .provincia(dto.getProvincia())
                        .pais(dto.getPais())
                        .build())
                .departamento(dto.getDepartamento())
                .piso(dto.getPiso())
                .codigo(dto.getCodigo())
                .build();
    }

    public DireccionDTO toDTO(Direccion entity) {
        if (entity == null) return null;

        return DireccionDTO.builder()
                .calle(entity.getCalle())
                .numero(entity.getNumero())
                .departamento(entity.getDepartamento())
                .piso(entity.getPiso())
                .codigo(entity.getCodigo())
                .localidad(entity.getLocalidad())
                .provincia(entity.getProvincia())
                .pais(entity.getPais())
                .build();
    }

    public List<Huesped> toEntityList(List<HuespedDTO> dtos) {
        return dtos == null ? null : dtos.stream().map(this::toEntity).collect(Collectors.toList());
    }

    public List<HuespedDTO> toDTOList(List<Huesped> entities) {
        return entities == null ? null : entities.stream().map(this::toDTO).collect(Collectors.toList());
    }
}

