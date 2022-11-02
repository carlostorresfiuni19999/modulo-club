package com.sd2022.club.service.traspasoService;

import com.sd2022.club.dao.ITraspasoRepository;
import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.traspaso.TraspasoDTO;
import com.sd2022.club.dtos.traspaso.TraspasoDTOResult;
import com.sd2022.club.dtos.traspaso.TraspasoFullCreateDTO;
import com.sd2022.club.service.baseService.BaseServiceImpl;
import com.sd2022.entities.models.Traspaso;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;


@Service
public class TraspasoServiceImpl extends BaseServiceImpl<TraspasoDTO, Traspaso, BaseResultDTO<TraspasoDTO>> implements ITraspasoService{

    @Autowired
    private ITraspasoRepository traspasoRepo;
    @Override
    public Traspaso toEntity(TraspasoDTO dto) {
        return null;
    }

    @Override
    public TraspasoDTO toDTO(Traspaso entity) {
        TraspasoDTO dto = new TraspasoDTO();
        dto.setCostoTotal(entity.getCostoTotal());
        dto.setId(entity.getId());
        dto.setFechaTraspaso(entity.getFechaTraspaso());
        return dto;
    }

    @Override
    public ResponseEntity<TraspasoDTO> findById(int id) {
        Traspaso traspaso = traspasoRepo.findById(id);

        if(traspaso == null) {
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<TraspasoDTO>(toDTO(traspaso), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResultDTO<TraspasoDTO>> getAll(Pageable page) {
        List<TraspasoDTO> dtos = traspasoRepo.findAll(page)
                .map(this::toDTO)
                .getContent();

        BaseResultDTO<TraspasoDTO> result = new TraspasoDTOResult();
        result.setDtos(dtos);
        return new ResponseEntity<BaseResultDTO<TraspasoDTO>>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity remove(int id) {
        return null;
    }

    @Override
    public ResponseEntity<TraspasoDTO> edit(int id, TraspasoDTO dto) {
        return null;
    }

    @Override
    public ResponseEntity<TraspasoDTO> add(TraspasoDTO dto) {
        return null;
    }

    @Override
    public ResponseEntity filtrarEntreFechas(Date inicio, Date fin, Pageable page) {
        List<TraspasoDTO> dtos = traspasoRepo.filtrarEntreFechas(inicio, fin, page)
                .map(this::toDTO)
                .getContent();

        BaseResultDTO<TraspasoDTO> result = new TraspasoDTOResult();
        result.setDtos(dtos);
        return new ResponseEntity<BaseResultDTO<TraspasoDTO>>(result, HttpStatus.OK);
    }

    @Override
    public Traspaso toEntity(TraspasoFullCreateDTO traspaso) {
        return null;
    }
}
