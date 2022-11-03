package com.sd2022.club.dao;

import com.sd2022.entities.models.TraspasoDetalle;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ITraspasoDetalleRepository  extends JpaRepository<TraspasoDetalle, Integer> {

    public TraspasoDetalle save(TraspasoDetalle detalle);
    public void deleteById(int id);
    public TraspasoDetalle findById(int id);

    @Query(value = "select td from TraspasoDetalle td where td.traspaso.id =  :idTraspaso ",
    countQuery = "select count(td) from TraspasoDetalle td where td.traspaso.id =  :idTraspaso ")
    public Page<TraspasoDetalle> findByIdTraspaso(@Param("idTraspaso") int idTraspaso, Pageable page);

    public Page<TraspasoDetalle> findAll(Pageable page);

    @Query(value = "select td from TraspasoDetalle td where td.traspaso.id =  :idTraspaso ")
    public List<TraspasoDetalle> findByIdTraspaso(@Param("idTraspaso") int idTraspaso);
}
