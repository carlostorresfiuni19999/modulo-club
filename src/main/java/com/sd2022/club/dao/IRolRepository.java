package com.sd2022.club.dao;

import com.sd2022.entities.models.Rol;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;


public interface IRolRepository extends JpaRepository<Rol, Integer> {
    public Rol findById(int id);
    public Rol findByRol(String rol);
    public Rol save(Rol rol);
    public Page<Rol> findByDeleted(boolean deleted, Pageable page);

    public void deleteById(int id);


}
