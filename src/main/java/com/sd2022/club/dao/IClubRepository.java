package com.sd2022.club.dao;

import com.sd2022.entities.models.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface IClubRepository extends JpaRepository<Club, Integer> {
    Page<Club> findByDeleted(Pageable page, boolean deleted);
    Club save(Club club);
    Club findById(int id);
    Club findByCancha(String cancha);
    Page<Club> findBySedeAndDeleted(String sede, boolean deleted, Pageable page);


}
