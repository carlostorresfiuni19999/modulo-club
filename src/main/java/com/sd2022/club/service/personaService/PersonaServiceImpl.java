package com.sd2022.club.service.personaService;

import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.persona.PersonaDTO;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;

public class PersonaServiceImpl implements IPersonaService{
    @Override
    public ResponseEntity<PersonaDTO> findById(int id) {
        return null;
    }

    @Override
    public ResponseEntity<BaseResultDTO<PersonaDTO>> getAll(Pageable page) {
        return null;
    }

    @Override
    public ResponseEntity remove(int id) {
        return null;
    }

    @Override
    public ResponseEntity edit(int id, PersonaDTO dto) {
        return null;
    }

    @Override
    public ResponseEntity<PersonaDTO> add(PersonaDTO dto) {
        return null;
    }

    @Override
    public ResponseEntity<BaseResultDTO<PersonaDTO>> findByNombre(String nombre, Pageable page) {
        return null;
    }

    @Override
    public ResponseEntity<BaseResultDTO<PersonaDTO>> findByApellido(String apellido, Pageable page) {
        return null;
    }

    @Override
    public ResponseEntity<BaseResultDTO<PersonaDTO>> findByCat(int cat, Pageable page) {
        return null;
    }

    @Override
    public ResponseEntity<PersonaDTO> findByUsername(String username) {
        return null;
    }

    @Override
    public ResponseEntity<PersonaDTO> findByEmail(String email) {
        return null;
    }
}
