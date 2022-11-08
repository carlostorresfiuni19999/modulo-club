package com.sd2022.club.service.personaService;

import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.persona.PersonaDTO;
import com.sd2022.club.service.baseService.IBaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IPersonaService extends IBaseService<PersonaDTO, BaseResultDTO<PersonaDTO>> {

    public BaseResultDTO<PersonaDTO> findByCat(int cat, Pageable page);

}
