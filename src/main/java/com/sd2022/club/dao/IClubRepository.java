package com.sd2022.club.dao;

import com.sd2022.entities.models.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClubRepository extends JpaRepository<Club, Integer> {
    public Page<Club> findByDeleted(Pageable page, boolean deleted);
    public Club save(Club club);
    public Club findById(int id);
    public Club findByCancha(String cancha);
    public Page<Club> findBySedeAndDeleted(String sede, boolean deleted, Pageable page);

}
