package com.sd2022.club.service.traspasoService;

import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.traspaso.TraspasoCreateDTO;
import com.sd2022.club.dtos.traspaso.TraspasoDTO;
import com.sd2022.club.errors.BadRequestException;
import com.sd2022.club.service.baseService.IBaseService;
import org.springframework.data.domain.Pageable;

public interface ITraspasoService extends IBaseService<TraspasoDTO, BaseResultDTO<TraspasoDTO>> {
   BaseResultDTO<TraspasoDTO> filtrarEntreFechas(String inicio, String fin, Pageable page) throws BadRequestException;

   TraspasoDTO save(TraspasoCreateDTO traspaso) throws Exception;

}
