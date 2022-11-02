package com.sd2022.club.service.traspasoDetalleService;

import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.traspasodetalle.TraspasoDetalleDTO;
import com.sd2022.club.dtos.traspasodetalle.TraspasoDetalleResultDTO;
import com.sd2022.club.service.baseService.BaseServiceImpl;
import com.sd2022.entities.models.TraspasoDetalle;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class TraspasoDetalleServiceImpl extends BaseServiceImpl<TraspasoDetalleDTO, TraspasoDetalle, BaseResultDTO<TraspasoDetalleDTO>> implements ITraspasoDetalleService {
    @Override
    public TraspasoDetalle toEntity(TraspasoDetalleDTO dto) {
        return null;
    }

    @Override
    public TraspasoDetalleDTO toDTO(TraspasoDetalle entity) {
        return null;
    }

    @Override
    public ResponseEntity<TraspasoDetalleDTO> findById(int id) {
        return null;
    }

    @Override
    public ResponseEntity<BaseResultDTO<TraspasoDetalleDTO>> getAll(Pageable page) {
        return null;
    }

    @Override
    public ResponseEntity remove(int id) {
        return null;
    }

    @Override
    public ResponseEntity<TraspasoDetalleDTO> edit(int id, TraspasoDetalleDTO dto) {
        return null;
    }

    @Override
    public ResponseEntity<TraspasoDetalleDTO> add(TraspasoDetalleDTO dto) {
        return null;
    }

    @Override
    public TraspasoDetalleResultDTO getByIdTraspaso(int idTraspaso, Pageable page) {
        return null;
    }
}
