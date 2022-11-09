package com.sd2022.club.dao;

import com.sd2022.entities.models.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface IRolRepository extends JpaRepository<Rol, Integer> {
    Rol findById(int id);
    Rol findByRol(String rol);
    Rol save(Rol rol);
    Page<Rol> findByDeleted(boolean deleted, Pageable page);

    void deleteById(int id);

    @Query("select count(rol) > 0 from Rol rol where rol.deleted = false AND rol.rol = :rol")
    boolean existsByRol(@Param("rol") String rol);



}
