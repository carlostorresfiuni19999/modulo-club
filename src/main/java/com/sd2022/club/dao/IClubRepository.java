package com.sd2022.club.dao;

import com.sd2022.entities.models.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IClubRepository extends JpaRepository<Club, Integer> {
    public Page<Club> findAll(Pageable page);

    public Club save(Club club);

    public void delete(int id);
    public Club findById(int id);
}
