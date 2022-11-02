package com.sd2022.club.dao;

import com.sd2022.club.dtos.persona.PersonaDTO;
import com.sd2022.entities.models.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IPersonaRepository extends JpaRepository<Persona, Integer> {
    public Persona save(PersonaDTO dto);
    public Page<Persona> findByDeleted(boolean deleted, Pageable page);
    public Page<Persona> findByNombreAndDeleted(String nombre, boolean deleted, Pageable page);
    public Page<Persona> findByApellidoAndDeleted(String apellido, boolean deleted, Pageable page);
    public Page<Persona> findByCatAndDeleted(int cat, boolean deleted, Pageable page);
    public Persona findById(int id);
    public List<Persona> findAll();

    public Persona findByEmail(String email);

    public Persona findByUsername(String username);

    @Query(value = "Select p from Persona p where " +
            " p.rol.id = :rol ")
    public Persona findByRol(@Param("rol") int rol);



}
