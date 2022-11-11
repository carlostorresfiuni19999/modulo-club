package com.sd2022.club.dao;

import com.sd2022.club.dtos.persona.PersonaDTO;
import com.sd2022.entities.models.Persona;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface IPersonaRepository extends JpaRepository<Persona, Integer> {
    Persona save(PersonaDTO dto);
    Page<Persona> findByDeleted(boolean deleted, Pageable page);
    Page<Persona> findByCatAndDeleted(int cat, boolean deleted, Pageable page);

    @Query(value = "select p from Persona p where p.id= :id" +
            " and p.deleted = false ")
    Persona findById(@Param("id") int id);

    @Query(value = "Select p from Persona p where " +
            " p.rol.id = :rol and p.deleted = false")
    List<Persona> findByRol(@Param("rol") int rol);

    @Query(value = "select (count(p) > 0) from Persona p " +
            " where p.deleted = false AND p.username = :username")
    boolean existsByUsername(@Param("username") String username);

    @Query(value = "select (count(p) > 0) from Persona p " +
            " where p.deleted = false AND p.username = :email")
    boolean existsByEmail(@Param("email") String email);



}
