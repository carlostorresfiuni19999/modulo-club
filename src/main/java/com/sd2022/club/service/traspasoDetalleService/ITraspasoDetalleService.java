package com.sd2022.club.service.traspasoDetalleService;

import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.traspasodetalle.TraspasoDetalleDTO;
import com.sd2022.club.dtos.traspasodetalle.TraspasoDetalleResultDTO;
import com.sd2022.club.service.baseService.IBaseService;
import org.springframework.data.domain.Pageable;

public interface ITraspasoDetalleService extends IBaseService<TraspasoDetalleDTO, BaseResultDTO<TraspasoDetalleDTO>> {
    public TraspasoDetalleResultDTO getByIdTraspaso(int idTraspaso, Pageable page);
}
