package com.sd2022.club.service.clubService;

import com.sd2022.club.dao.IClubRepository;
import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.club.ClubDTO;
import com.sd2022.club.dtos.club.ClubResultDTO;
import com.sd2022.club.service.base.BaseServiceImpl;
import com.sd2022.entities.models.Club;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClubServiceImpl extends BaseServiceImpl<ClubDTO, Club, BaseResultDTO<ClubDTO>> implements IClubService{

    @Autowired
    private IClubRepository clubRepo;

    @Override
    public ClubDTO findById(int id) {
        Club club = clubRepo.findById(id);
        if(club.isDeleted()) return null;
        return toDTO(club);
    }

    @Override
    public BaseResultDTO<ClubDTO> getAll(Pageable page){


        List<ClubDTO> dtos = clubRepo.findByDeleted(page,false)
                        .map(this::toDTO)
                                .getContent();
        BaseResultDTO<ClubDTO> result = new ClubResultDTO();
        result.setDtos(dtos);
        return result;
    }

    @Override
    public ClubDTO add(ClubDTO club){
        Club ent = toEntity(club);
        clubRepo.save(ent);

        ClubDTO result = toDTO(clubRepo.findById(ent.getId()));

        return result;
    }


    @Override
    public boolean remove(int id){
       try{
           Club club = clubRepo.findById(id);

           if(club.isDeleted()) return false;
           club.setDeleted(true);
           clubRepo.save(club);
           return true;
       }catch (Exception e) {
           return false;
       }

    }




    @Override
    public ClubDTO findByCancha(String cancha){
        Club club = clubRepo.findByCancha(cancha);
        if(club.isDeleted()) return null;

        return toDTO(club);
    }

    @Override
    public BaseResultDTO<ClubDTO> findBySede(String sede, Pageable page){
        List<ClubDTO> dtos =  clubRepo.findBySedeAndDeleted(sede, false, page)
                                .stream()
                                .filter(c -> !c.isDeleted())
                                .map(this::toDTO)
                                .collect(Collectors.toList());

        BaseResultDTO<ClubDTO> result = new ClubResultDTO();
        result.setDtos(dtos);
        return result;
    }

    @Override
    public ClubDTO edit(int id, ClubDTO club){
        if(club.getId() == id){

            Club entity = clubRepo.findById(id);
            if(!entity.isDeleted()){
                entity.setNombreClub(club.getNombre());
                entity.setSede(club.getSede());
                entity.setCancha(club.getCancha());
                clubRepo.save(entity);

                return toDTO(clubRepo.findById(id));
            } else{
                return null;
            }

        } else{
            return null;
        }


    }

    @Override
    public ClubDTO toDTO(Club entity){
        ClubDTO dto = new ClubDTO();
        dto.setCancha(entity.getCancha());
        dto.setSede(entity.getSede());
        dto.setNombre(entity.getNombreClub());
        dto.setId(entity.getId());

        return dto;
    }



    @Override
    public Club toEntity(ClubDTO dto){
        Club entity = new Club();
        entity.setCancha(dto.getCancha());
        entity.setSede(dto.getSede());
        entity.setNombreClub(dto.getNombre());
        return entity;
    }
}
