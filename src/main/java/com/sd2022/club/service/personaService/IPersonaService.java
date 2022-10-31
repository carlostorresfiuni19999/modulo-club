package com.sd2022.club.service.personaService;

import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.persona.PersonaDTO;
import com.sd2022.club.service.base.IBaseService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public interface IPersonaService extends IBaseService<PersonaDTO, BaseResultDTO<PersonaDTO>> {
    public ResponseEntity <BaseResultDTO<PersonaDTO>>findByNombre(String nombre, Pageable page);
    public ResponseEntity <BaseResultDTO<PersonaDTO>> findByApellido(String apellido, Pageable page);
    public ResponseEntity <BaseResultDTO<PersonaDTO>> findByCat(int cat, Pageable page);
    public ResponseEntity <PersonaDTO> findByUsername(String username);
    public ResponseEntity <PersonaDTO> findByEmail(String email);

}
