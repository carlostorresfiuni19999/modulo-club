package com.sd2022.club.service.rolService;

import com.sd2022.club.dao.IPersonaRepository;
import com.sd2022.club.dao.IRolRepository;
import com.sd2022.club.dtos.rol.RolDTO;
import com.sd2022.club.dtos.rol.RolResultDTO;
import com.sd2022.club.service.baseService.BaseServiceImpl;
import com.sd2022.entities.models.Persona;
import com.sd2022.entities.models.Rol;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolServiceImpl extends BaseServiceImpl<RolDTO, Rol, RolResultDTO> implements IRolSerivice{

    @Autowired
    private IRolRepository rolRepo;

    @Autowired
    private IPersonaRepository personaRepo;

    private Logger log = Logger.getLogger(RolServiceImpl.class);

    @Autowired
    private Environment env;
    @Override
    public Rol toEntity(RolDTO dto) {
        Rol rol = new Rol();
        rol.setRol(dto.getRol().trim().toUpperCase());
        return rol;
    }

    @Override
    public ResponseEntity findByRol(String rol) {
        Rol ent = rolRepo.findByRol(rol);

        if(ent == null || ent.isDeleted()) {
            log.error(env.getProperty("notfound"));
            return new ResponseEntity<String>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<RolDTO>(toDTO(ent), HttpStatus.OK);
    }

    @Override
    public RolDTO toDTO(Rol entity) {
        RolDTO dto = new RolDTO();
        dto.setRol(entity.getRol());
        dto.setId(entity.getId());

        return dto;
    }

    @Override
    public ResponseEntity<RolDTO> findById(int id) {
        Rol ent = rolRepo.findById(id);

        if(ent == null || ent.isDeleted()) {
            log.error(env.getProperty("notfound"));
            return new ResponseEntity<RolDTO>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<RolDTO>(toDTO(ent), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<RolResultDTO> getAll(Pageable page) {
        RolResultDTO dtos = new RolResultDTO();

        List<RolDTO> result = rolRepo.findByDeleted(false, page)
                .map(this::toDTO)
                .getContent();

        dtos.setDtos(result);

        return new ResponseEntity<RolResultDTO>( dtos, HttpStatus.OK);
    }

    @Override
    public ResponseEntity remove(int id) {

        Persona p = personaRepo.findByRol(id);
       try{
           if(p != null) {
               p.setRol(null);
               personaRepo.save(p);
           }

           rolRepo.deleteById(id);
           return new ResponseEntity(HttpStatus.OK);
       } catch (Exception e){
           log.error(e);
           return new ResponseEntity( HttpStatus.INTERNAL_SERVER_ERROR);
       }
    }

    @Override
    public ResponseEntity<RolDTO> edit(int id, RolDTO dto) {
        if(id != dto.getId()){
            log.error(env.getProperty("primarykeyerror"));
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Rol entity = new Rol();
        if(entity == null || entity.isDeleted()){
            log.error(env.getProperty("notfound"));
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        entity.setRol(dto.getRol());

        return new ResponseEntity<RolDTO>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity add(RolDTO dto) {

        boolean exist = rolRepo.existsByRol(dto.getRol());

        if(!exist){
            rolRepo.save(toEntity((dto)));
            return new ResponseEntity<RolDTO>(toDTO(rolRepo.findByRol(dto.getRol())), HttpStatus.OK);
        }
        log.error(env.getProperty(env.getProperty("existsrolerror")));
        return new ResponseEntity(env.getProperty("existsrolerror"), HttpStatus.BAD_REQUEST);

    }
}
