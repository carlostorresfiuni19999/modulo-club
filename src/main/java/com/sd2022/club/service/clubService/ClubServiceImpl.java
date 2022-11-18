package com.sd2022.club.service.clubService;

import com.sd2022.club.dao.IClubRepository;
import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.club.ClubDTO;
import com.sd2022.club.dtos.club.ClubResultDTO;
import com.sd2022.club.errors.BadRequestException;
import com.sd2022.club.errors.NotFoundException;
import com.sd2022.club.service.baseService.BaseServiceImpl;
import com.sd2022.club.utils.Settings;
import com.sd2022.entities.models.Club;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CachePut;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClubServiceImpl extends BaseServiceImpl<ClubDTO, Club, BaseResultDTO<ClubDTO>> implements IClubService{

    @Autowired
    private Environment env;
    @Autowired
    private IClubRepository clubRepo;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private Settings settings;

    public ClubDTO findById(int id) throws NotFoundException {
            Club club = clubRepo.findById(id);
            if(club == null || club.isDeleted())
                throw new NotFoundException(env.getProperty("notfound"));

            cacheManager.getCache(settings.getCacheName()).put("club_api_"+id, toDTO(club));
            return toDTO(club);
    }

    @Override
    public BaseResultDTO<ClubDTO> getAll(Pageable page) {

            List<ClubDTO> dtos = clubRepo.findByDeleted(page,false)
                    .map(ent -> {
                        ClubDTO d =  toDTO(ent);
                        cacheManager.getCache(settings.getCacheName()).putIfAbsent("club_api_"+d.getId(), d);
                        return d;
                    })
                    .getContent();
            BaseResultDTO<ClubDTO> result = new ClubResultDTO();

            int cantPage = clubRepo.findByDeleted(page, false)
                            .getTotalPages();

            result.setPages(cantPage);
            result.setDtos(dtos);
            return result;

    }
    @Override
    public ClubDTO add(ClubDTO club) throws BadRequestException {

            Club exist = clubRepo.findByCancha(club.getCancha().trim().toUpperCase());
            if (exist == null || exist.isDeleted()){
                Club ent = toEntity(club);
                ent = clubRepo.save(ent);
                cacheManager.getCache(settings.getCacheName()).put("club_id_"+ent.getId(), toDTO(ent));
                return toDTO(clubRepo.findById(ent.getId()));

            }

            throw new BadRequestException(env.getProperty("canchaerror"));
    }


    @Override
    public void remove(int id) throws NotFoundException{
           Club club = clubRepo.findById(id);

           if(club == null || club.isDeleted()) {
               throw new NotFoundException(env.getProperty("notfound"));
           }
           cacheManager.getCache(settings.getCacheName()).evict("club_api_"+id);
           club.setDeleted(true);
           clubRepo.save(club);
    }

    @Override
    @CachePut(value =Settings.CACHE_NAME, key ="'club_api_' +#id")
    public ClubDTO edit(int id, ClubDTO club) throws NotFoundException, BadRequestException{

            if(club.getId() == id){

                Club entity = clubRepo.findById(id);

                if(club.getCancha().trim().toUpperCase().equals(entity.getCancha())){
                    if(!entity.isDeleted()){
                        clubRepo.save(toEntity(club));

                        return toDTO(entity);
                    } else{
                        throw new NotFoundException(env.getProperty("notfound"));
                    }

                } else {
                    Club exist = clubRepo.findByCancha(club.getCancha().trim().toUpperCase());

                    if(exist == null || exist.isDeleted()){
                        clubRepo.save(toEntity(club));
                        return toDTO(clubRepo.findById(id));
                    } else{
                        throw new BadRequestException(env.getProperty("canchaerror"));
                    }
                }

            } else{
                throw new BadRequestException(env.getProperty("primarykeyerror"));
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
        entity.setId(dto.getId());
        return entity;
    }

    @Override
    public BaseResultDTO<ClubDTO> getAll() {
        BaseResultDTO resultDTO = new ClubResultDTO();

        List<ClubDTO> result = clubRepo.findByDeleted(false)
                .stream().map(this::toDTO)
                .collect(Collectors.toList());


        result.forEach(d -> {
            cacheManager.getCache(settings.getCacheName()).putIfAbsent("club_api_"+d.getId(), d);
        });

        resultDTO.setDtos(result);

        return resultDTO;
    }
}
