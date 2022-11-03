package com.sd2022.club.service.clubService;

import com.sd2022.club.dao.IClubRepository;
import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.club.ClubDTO;
import com.sd2022.club.dtos.club.ClubResultDTO;
import com.sd2022.club.service.baseService.BaseServiceImpl;
import com.sd2022.entities.models.Club;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClubServiceImpl extends BaseServiceImpl<ClubDTO, Club, BaseResultDTO<ClubDTO>> implements IClubService{

    @Autowired
    private Environment env;
    @Autowired
    private IClubRepository clubRepo;

    private Logger log = Logger.getLogger(ClubServiceImpl.class);

    @Override
    public ResponseEntity<ClubDTO> findById(int id) {
        try{
            Club club = clubRepo.findById(id);
            if(club.isDeleted()) return new ResponseEntity<>( HttpStatus.NOT_FOUND);;
            return new ResponseEntity<>(toDTO(club), HttpStatus.OK);
        } catch (Exception e){
            log.error(e);
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<BaseResultDTO<ClubDTO>> getAll(Pageable page){

        try{
            List<ClubDTO> dtos = clubRepo.findByDeleted(page,false)
                    .map(this::toDTO)
                    .getContent();
            BaseResultDTO<ClubDTO> result = new ClubResultDTO();
            result.setDtos(dtos);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (Exception e){
            log.error(e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<ClubDTO> add(ClubDTO club){
        try {
            Club exist = clubRepo.findByCancha(club.getCancha().trim().toUpperCase());
            if (exist == null || exist.isDeleted()){
                Club ent = toEntity(club);
                clubRepo.save(ent);

                ClubDTO result = toDTO(clubRepo.findById(ent.getId()));

                return new ResponseEntity<>(result, HttpStatus.OK);
            }

        } catch (Exception e){
            log.error(e);
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }




        return new ResponseEntity(env.getProperty("canchaerror"), HttpStatus.BAD_REQUEST);
    }


    @Override
    public ResponseEntity remove(int id){
       try{
           Club club = clubRepo.findById(id);

           if(club == null || club.isDeleted()) {
               return new ResponseEntity<String>(env.getProperty("notfound"), HttpStatus.NOT_FOUND);
           }
           club.setDeleted(true);
           clubRepo.save(club);
           return new ResponseEntity(HttpStatus.OK);

       }catch (Exception e) {
           log.error(e);
           return new ResponseEntity<String>( HttpStatus.INTERNAL_SERVER_ERROR);
       }

    }




    @Override
    public ResponseEntity<ClubDTO> findByCancha(String cancha){
        try {
            Club club = clubRepo.findByCancha(cancha);
            if(club.isDeleted()) {
                log.error(env.getProperty("notfound"));
                return new ResponseEntity(HttpStatus.NOT_FOUND);
            }

            return new ResponseEntity<ClubDTO>(toDTO(club), HttpStatus.OK);

        } catch (Exception e){
            log.error(e);
            return new ResponseEntity<>( HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity<BaseResultDTO<ClubDTO>> findBySede(String sede, Pageable page){
        try{
            List<ClubDTO> dtos =  clubRepo.findBySedeAndDeleted(sede, false, page)
                    .stream()
                    .filter(c -> !c.isDeleted())
                    .map(this::toDTO)
                    .collect(Collectors.toList());

            BaseResultDTO<ClubDTO> result = new ClubResultDTO();
            result.setDtos(dtos);
            return new ResponseEntity<BaseResultDTO<ClubDTO>>(result, HttpStatus.OK);

        } catch (Exception e){
            log.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @Override
    public ResponseEntity edit(int id, ClubDTO club){
        try{
            if(club.getId() == id){



                Club entity = clubRepo.findById(id);

                if(club.getCancha().trim().toUpperCase().equals(entity.getCancha())){
                    if(!entity.isDeleted()){
                        entity.setNombreClub(club.getNombre());
                        entity.setSede(club.getSede());
                        entity.setCancha(club.getCancha());
                        clubRepo.save(entity);

                        return new ResponseEntity<ClubDTO>(toDTO(clubRepo.findById(id)), HttpStatus.OK);
                    } else{
                        log.error(env.getProperty("notfound"));
                        return new ResponseEntity( HttpStatus.NOT_FOUND);
                    }

                } else {
                    Club exist = clubRepo.findByCancha(club.getCancha().trim().toUpperCase());

                    if(exist == null || exist.isDeleted()){
                        entity.setNombreClub(club.getNombre());
                        entity.setSede(club.getSede());
                        entity.setCancha(club.getCancha());
                        clubRepo.save(entity);
                        return new ResponseEntity<ClubDTO>(toDTO(clubRepo.findById(id)), HttpStatus.OK);
                    } else{
                        log.error(env.getProperty("canchaerror"));
                        return new ResponseEntity(env.getProperty("canchaerror"), HttpStatus.BAD_REQUEST);
                    }
                }

            } else{
                log.error(env.getProperty("primarykeyerror"));
                return new ResponseEntity<String>(env.getProperty("primarykeyerror"), HttpStatus.BAD_REQUEST);
            }


        } catch (Exception e){
            log.error(e);
            return  new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
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
        entity.setCancha(dto.getCancha().trim().toUpperCase());
        entity.setSede(dto.getSede().trim().toUpperCase());
        entity.setNombreClub(dto.getNombre().trim().toUpperCase());
        return entity;
    }
}
