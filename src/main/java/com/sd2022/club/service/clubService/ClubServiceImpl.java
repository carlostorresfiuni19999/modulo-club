package com.sd2022.club.service.clubService;

import com.sd2022.club.dao.IClubRepository;
import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.club.ClubDTO;
import com.sd2022.club.dtos.club.ClubResultDTO;
import com.sd2022.club.service.base.BaseServiceImpl;
import com.sd2022.entities.models.Club;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClubServiceImpl extends BaseServiceImpl<ClubDTO, Club, BaseResultDTO<ClubDTO>> implements IClubService{

    @Autowired
    private IClubRepository clubRepo;

    @Override
    public ResponseEntity<ClubDTO> findById(int id) {
        Club club = clubRepo.findById(id);
        if(club.isDeleted()) return null;
        return new ResponseEntity<>(toDTO(club), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResultDTO<ClubDTO>> getAll(Pageable page){


        List<ClubDTO> dtos = clubRepo.findByDeleted(page,false)
                        .map(this::toDTO)
                                .getContent();
        BaseResultDTO<ClubDTO> result = new ClubResultDTO();
        result.setDtos(dtos);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<ClubDTO> add(ClubDTO club){
        Club ent = toEntity(club);
        clubRepo.save(ent);

        ClubDTO result = toDTO(clubRepo.findById(ent.getId()));

        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @Override
    public ResponseEntity remove(int id){
       try{
           Club club = clubRepo.findById(id);

           if(club == null || club.isDeleted()) {
               return new ResponseEntity<String>("El elemento no existe", HttpStatus.BAD_REQUEST);
           }
           club.setDeleted(true);
           clubRepo.save(club);
           return new ResponseEntity<String>("Borrado con exito", HttpStatus.OK);

       }catch (Exception e) {
           return new ResponseEntity<String>("Ocurrio un error inesperado", HttpStatus.BAD_REQUEST);
       }

    }




    @Override
    public ResponseEntity<ClubDTO> findByCancha(String cancha){
        Club club = clubRepo.findByCancha(cancha);
        if(club.isDeleted()) return null;

        return new ResponseEntity<ClubDTO>(toDTO(club), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResultDTO<ClubDTO>> findBySede(String sede, Pageable page){
        List<ClubDTO> dtos =  clubRepo.findBySedeAndDeleted(sede, false, page)
                                .stream()
                                .filter(c -> !c.isDeleted())
                                .map(this::toDTO)
                                .collect(Collectors.toList());

        BaseResultDTO<ClubDTO> result = new ClubResultDTO();
        result.setDtos(dtos);
        return new ResponseEntity<BaseResultDTO<ClubDTO>>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity edit(int id, ClubDTO club){
        if(club.getId() == id){

            Club entity = clubRepo.findById(id);
            if(!entity.isDeleted()){
                entity.setNombreClub(club.getNombre());
                entity.setSede(club.getSede());
                entity.setCancha(club.getCancha());
                clubRepo.save(entity);

                return new ResponseEntity<ClubDTO>(toDTO(clubRepo.findById(id)), HttpStatus.OK);
            } else{
                return new ResponseEntity<String>("No existe el recurso que intentas editar", HttpStatus.BAD_REQUEST);
            }

        } else{
            return new ResponseEntity<String>("No se pueden actualizar las claves primarias", HttpStatus.BAD_REQUEST);
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
