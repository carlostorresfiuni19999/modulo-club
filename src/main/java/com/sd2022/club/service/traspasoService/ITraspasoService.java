package com.sd2022.club.service.traspasoService;

import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.traspaso.TraspasoDTO;
import com.sd2022.club.service.baseService.IBaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

import java.util.Date;

public interface ITraspasoService extends IBaseService<TraspasoDTO, BaseResultDTO<TraspasoDTO>> {
    public ResponseEntity filtrarEntreFechas(Date inicio, Date fin, Pageable page);


}
