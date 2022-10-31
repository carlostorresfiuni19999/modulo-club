package com.sd2022.club.dao;

import com.sd2022.club.dtos.persona.PersonaDTO;
import com.sd2022.entities.models.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IPersonaRepository extends JpaRepository<Persona, Integer> {
    public Persona save(PersonaDTO dto);
    public Page<Persona> findByDeleted(boolean deleted, Pageable page);
    public Page<Persona> findByNombreAndDeleted(String nombre, boolean deleted, Pageable page);
    public Page<Persona> findByApellidoAndDeleted(String apellido, boolean deleted, Pageable page);
    public Page<Persona> findByCatAndDeleted(int cat, boolean deleted, Pageable page);
    public Persona findById(int id);
}
