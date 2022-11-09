package com.sd2022.club.dao;

import com.sd2022.entities.models.Club;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface IClubRepository extends JpaRepository<Club, Integer> {
    Page<Club> findByDeleted(Pageable page, boolean deleted);
    Club save(Club club);

    @Query(value = "select c from Club c where c.id = :id AND c.deleted = false")
    Club findById(@Param("id") int id);
    @Query(value = "select c from Club c where c.cancha = :cancha AND c.deleted = false")
    Club findByCancha(@Param("cancha") String cancha);

}
