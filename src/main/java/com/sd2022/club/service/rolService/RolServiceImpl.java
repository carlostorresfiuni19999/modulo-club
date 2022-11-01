package com.sd2022.club.service.rolService;

import com.sd2022.club.dao.IPersonaRepository;
import com.sd2022.club.dao.IRolRepository;
import com.sd2022.club.dtos.rol.RolDTO;
import com.sd2022.club.dtos.rol.RolResultDTO;
import com.sd2022.club.service.baseService.BaseServiceImpl;
import com.sd2022.entities.models.Rol;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RolServiceImpl extends BaseServiceImpl<RolDTO, Rol, RolResultDTO> implements IRolSerivice{

    private IRolRepository rolRepo;
    private IPersonaRepository personaRepo;

    @Override
    public Rol toEntity(RolDTO dto) {
        Rol rol = new Rol();
        rol.setRol(dto.getRol().trim().toUpperCase());
        return rol;
    }

    @Override
    public ResponseEntity findByRol(String rol) {
        Rol ent = rolRepo.findByRol(rol);

        if(ent == null || ent.isDeleted()) return new ResponseEntity<String>("No se encontro resultado",HttpStatus.NOT_FOUND);
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

        if(ent == null || ent.isDeleted()) return new ResponseEntity<RolDTO>(new RolDTO(), HttpStatus.NOT_FOUND);
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
       try{
           rolRepo.deleteById(id);
           return new ResponseEntity<String>("Borrado con exito", HttpStatus.OK);
       } catch (Exception e){
           return new ResponseEntity<String>("Ocurrio un error al borrar el registro", HttpStatus.BAD_REQUEST);
       }
    }

    @Override
    public ResponseEntity<RolDTO> edit(int id, RolDTO dto) {
        if(id != dto.getId()){
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        Rol entity = new Rol();
        if(entity == null || entity.isDeleted()){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }

        entity.setRol(dto.getRol());

        return new ResponseEntity<RolDTO>(dto, HttpStatus.OK);
    }

    @Override
    public ResponseEntity add(RolDTO dto) {
        Rol ent = rolRepo.findById(dto.getId());
        if(ent == null){
            rolRepo.save(toEntity((dto)));
            return new ResponseEntity<RolDTO>(toDTO(rolRepo.findById(dto.getId())), HttpStatus.OK);
        }

        return new ResponseEntity("Ya existe el rol que intentas crear", HttpStatus.BAD_REQUEST);

    }
}
