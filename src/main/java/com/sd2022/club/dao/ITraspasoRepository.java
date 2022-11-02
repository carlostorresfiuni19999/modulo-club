package com.sd2022.club.dao;

import com.sd2022.entities.models.Traspaso;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Date;

public interface ITraspasoRepository extends JpaRepository<Traspaso, Integer> {
    public Traspaso save(Traspaso traspaso);
    public Traspaso findById(int id);
    public Page<Traspaso> findAll(Pageable page);

    @Query(value = "select t from Traspaso t where t.deleted = false AND t.fechaTraspaso " +
            " BETWEEN :inicio AND :fin ")
    public Page<Traspaso> filtrarEntreFechas(@Param("inicio") Date fechaInicio, @Param("fin") Date fechaFin, Pageable page);


}
