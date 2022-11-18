package com.sd2022.club.service.rolService;

import com.sd2022.club.dao.IPersonaRepository;
import com.sd2022.club.dao.IRolRepository;
import com.sd2022.club.dtos.rol.RolDTO;
import com.sd2022.club.dtos.rol.RolResultDTO;
import com.sd2022.club.errors.BadRequestException;
import com.sd2022.club.errors.NotFoundException;
import com.sd2022.club.service.baseService.BaseServiceImpl;
import com.sd2022.club.utils.Settings;
import com.sd2022.entities.models.Persona;
import com.sd2022.entities.models.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class RolServiceImpl extends BaseServiceImpl<RolDTO, Rol, RolResultDTO> implements IRolSerivice{

    @Autowired
    private IRolRepository rolRepo;

    @Autowired
    private IPersonaRepository personaRepo;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private  Settings settings;


    @Autowired
    private Environment env;
    @Override
    public Rol toEntity(RolDTO dto) {
        Rol rol = new Rol();
        rol.setRol(dto.getRol().trim().toUpperCase());
        return rol;
    }



    @Override
    public RolDTO toDTO(Rol entity) {
        RolDTO dto = new RolDTO();
        dto.setRol(entity.getRol());
        dto.setId(entity.getId());

        return dto;
    }

    @Override
    @Cacheable(value = Settings.CACHE_NAME, key = "'rol_api_' + #id")
    public RolDTO findById(int id) throws NotFoundException {
        Rol ent = rolRepo.findById(id);

        if(ent == null || ent.isDeleted()) {

            throw new NotFoundException(env.getProperty("notfound"));
        }
        return toDTO(ent);
    }

    @Override
    public RolResultDTO getAll(Pageable page) {
        RolResultDTO dtos = new RolResultDTO();

        System.out.println(settings.getCacheName());

        List<RolDTO> result = rolRepo.findByDeleted(false, page)
                .map(r -> {
                    RolDTO d = toDTO(r);
                    cacheManager.getCache(settings.getCacheName()).putIfAbsent("rol_api_"+r.getId(), d);
                    return d;
                })
                .getContent();




        dtos.setDtos(result);

        return dtos;
    }

    @Transactional
    @CacheEvict(value = Settings.CACHE_NAME, key = "'rol_api_' +#id")
    @Override
    public void remove(int id) throws NotFoundException {
        Rol deleted = rolRepo.findById(id);
        if (deleted == null)
            throw new NotFoundException(env.getProperty("notfound"));

        List<Persona> personas = personaRepo.findByRol(id);

        personas.forEach(p -> {
            p.setRol(null);
            cacheManager.getCache(settings.getCacheName()).evict("persona_api_"+p.getId());
            personaRepo.save(p);
        });

        rolRepo.deleteById(id);



       }



    @Override
    public RolDTO edit(int id, RolDTO dto) throws BadRequestException, NotFoundException {
        if(id != dto.getId()){
            throw new BadRequestException(env.getProperty("primarykeyerror"));
        }

        Rol entity = rolRepo.findById(id);
        if(entity == null || entity.isDeleted()){
            throw new NotFoundException(env.getProperty("notfound"));
        }
        if(!(entity.getRol().equals(dto.getRol()))){
            if(rolRepo.existsByRol(dto.getRol())){
                throw new BadRequestException(env.getProperty("existsrolerror"));
            } else{
                entity.setRol(dto.getRol());
            }

        } else{
            entity.setRol(dto.getRol());
        }

        cacheManager.getCache(settings.getCacheName()).put("rol_api_"+entity.getId(), toDTO(entity));
        rolRepo.save(entity);

        return toDTO(entity);
    }


    @Override
    public RolDTO add(RolDTO dto) throws BadRequestException {

        boolean exist = rolRepo.existsByRol(dto.getRol());

        if(!exist){
            Rol saved =rolRepo.save(toEntity((dto)));
            cacheManager.getCache(settings.getCacheName()).put("rol_api_"+saved.getId(), toDTO(saved));
            return toDTO(saved);
        }
        throw new BadRequestException(env.getProperty("rolexistserror"));

    }
}
