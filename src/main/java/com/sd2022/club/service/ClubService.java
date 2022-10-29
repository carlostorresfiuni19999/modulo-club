package com.sd2022.club.service;

import com.sd2022.club.dao.IClubRepository;
import com.sd2022.club.models.dtos.ClubDTO;
import com.sd2022.entities.models.Club;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ClubService {

    @Autowired
    private IClubRepository clubRepo;

    public Page<ClubDTO> getAll(Pageable page){
        Page<ClubDTO> dtos = clubRepo.findAll(page).map(this::toDTO);

        return dtos;
    }

    public ClubDTO add(ClubDTO club){
        Club ent = toEntity(club);
        clubRepo.save(ent);

        ClubDTO result = toDTO(clubRepo.findById(ent.getId()));

        return result;
    }

    public boolean remove(int id){
       try{
           clubRepo.delete(id);
           return true;
       }catch (Exception e){
           return false;
       }



    }

    public ClubDTO edit(int id, ClubDTO club){
        if(club.getId() == id){
            Club entity = clubRepo.findById(id);
            entity.setNombreClub(club.getNombre());
            entity.setSede(club.getSede());
            entity.setCancha(club.getCancha());
            clubRepo.save(entity);

            return toDTO(clubRepo.findById(id));
        } else{
            return null;
        }


    }


    private ClubDTO toDTO(Club entity){
        ClubDTO dto = new ClubDTO();
        dto.setCancha(entity.getCancha());
        dto.setSede(entity.getSede());
        dto.setNombre(entity.getNombreClub());
        dto.setId(entity.getId());

        return dto;
    }




    private Club toEntity(ClubDTO dto){
        Club entity = new Club();
        entity.setCancha(dto.getCancha());
        entity.setSede(dto.getSede());
        entity.setNombreClub(dto.getNombre());
        return entity;
    }
}
