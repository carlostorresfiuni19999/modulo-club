package com.sd2022.club.service.personaService;

import com.sd2022.club.dao.IClubRepository;
import com.sd2022.club.dao.IPersonaRepository;
import com.sd2022.club.dao.IRolRepository;
import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.persona.PersonaDTO;
import com.sd2022.club.dtos.persona.PersonaResultDTO;
import com.sd2022.club.service.baseService.BaseServiceImpl;
import com.sd2022.entities.models.Club;
import com.sd2022.entities.models.Persona;
import com.sd2022.entities.models.Rol;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class PersonaServiceImpl extends BaseServiceImpl<PersonaDTO, Persona, BaseResultDTO<PersonaDTO>> implements IPersonaService{

    @Autowired
    private IPersonaRepository personaRepo;

    @Autowired
    private IRolRepository rolRepo;

    @Autowired
    private IClubRepository clubRepo;

    @Override
    public ResponseEntity<PersonaDTO> findById(int id) {
        Persona p = personaRepo.findById(id);
        if(p == null || p.isDeleted()){
            return new ResponseEntity("No existe el Recurso", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<PersonaDTO>(toDTO(p), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResultDTO<PersonaDTO>> getAll(Pageable page) {
        List<PersonaDTO> dtos = personaRepo.findByDeleted(false, page)
                .map(this::toDTO)
                .getContent();

        BaseResultDTO<PersonaDTO> result = new PersonaResultDTO();

        result.setDtos(dtos);

        return new ResponseEntity<BaseResultDTO<PersonaDTO>>(result, HttpStatus.OK);

    }

    @Override
    public ResponseEntity remove(int id) {

        Persona del = personaRepo.findById(id);
        if(del == null || del.isDeleted()){
            return new ResponseEntity<String>("No existe el Recurso", HttpStatus.NOT_FOUND);
        }
        del.setDeleted(true);
        personaRepo.save(del);

        return new ResponseEntity("Se ha borrado con exito",HttpStatus.OK);
    }

    @Override
    public ResponseEntity edit(int id, PersonaDTO dto) {
        if(id != dto.getId()){
            return new ResponseEntity<String>("No se puede actualizar", HttpStatus.BAD_REQUEST);
        } else{
            Persona p = personaRepo.findById(dto.getId());

            if(p == null || p.isDeleted()){
                return new ResponseEntity<String>("No existe el Recurso", HttpStatus.NOT_FOUND);
            } else{
                Persona upd = toEntity(dto);
                upd.setId(dto.getId());

                personaRepo.save(upd);

                upd = personaRepo.findById(dto.getId());
                return new ResponseEntity<PersonaDTO>(toDTO(upd), HttpStatus.OK);
            }
        }
    }

    @Override
    public ResponseEntity<PersonaDTO> add(PersonaDTO dto) {
        personaRepo.save(toEntity(dto));

        Persona personaSaved = personaRepo.findById(dto.getId());

        return new ResponseEntity<PersonaDTO>(toDTO(personaSaved), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<BaseResultDTO<PersonaDTO>> findByNombre(String nombre, Pageable page) {
        List<PersonaDTO> dtos = personaRepo.findByNombreAndDeleted(nombre, false, page)
                .map(this::toDTO)
                .getContent();

        BaseResultDTO<PersonaDTO> result = new PersonaResultDTO();

        result.setDtos(dtos);

        return new ResponseEntity<BaseResultDTO<PersonaDTO>>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResultDTO<PersonaDTO>> findByApellido(String apellido, Pageable page) {
        List<PersonaDTO> dtos = personaRepo.findByApellidoAndDeleted(apellido, false, page)
                .map(this::toDTO)
                .getContent();

        BaseResultDTO<PersonaDTO> result = new PersonaResultDTO();

        result.setDtos(dtos);

        return new ResponseEntity<BaseResultDTO<PersonaDTO>>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResultDTO<PersonaDTO>> findByCat(int cat, Pageable page) {
        List<PersonaDTO> dtos = personaRepo.findByCatAndDeleted(cat, false, page)
                .map(this::toDTO)
                .getContent();

        BaseResultDTO<PersonaDTO> result = new PersonaResultDTO();

        result.setDtos(dtos);

        return new ResponseEntity<BaseResultDTO<PersonaDTO>>(result, HttpStatus.OK);

    }

    @Override
    public ResponseEntity<PersonaDTO> findByUsername(String username) {
        Persona ent = personaRepo.findByUsername(username);

        if(ent == null || ent.isDeleted()){
            return new ResponseEntity("Recurso no encontrado", HttpStatus.NOT_FOUND);
        } else{
            return new ResponseEntity<PersonaDTO>(toDTO(ent), HttpStatus.OK);
        }
    }

    @Override
    public ResponseEntity<PersonaDTO> findByEmail(String email) {
        Persona ent = personaRepo.findByEmail(email);

        if(ent == null || ent.isDeleted()){
            return new ResponseEntity("Recurso no encontrado", HttpStatus.NOT_FOUND);
        } else{
            return new ResponseEntity<PersonaDTO>(toDTO(ent), HttpStatus.OK);
        }
    }

    @Override
    public Persona toEntity(PersonaDTO dto) {
        Persona ent = new Persona();

        ent.setNombre(dto.getNombre());
        ent.setApellido(dto.getApellido());
        ent.setCat(dto.getCat());

        Club club = clubRepo.findById(dto.getIdClub());
        ent.setClub(club == null ? null : club);

        ent.setEmail(dto.getEmail());
        ent.setDocumento(dto.getDocumento());
        ent.setDocumentoTipo(dto.getDocumentoTipo());
        ent.setUsername(dto.getUsername());
        ent.setNacimiento(new Date(dto.getNacimiento()));

        Rol rol = rolRepo.findById(dto.getIdRol());
        ent.setRol(rol);

        return ent;
    }

    @Override
    public PersonaDTO toDTO(Persona entity) {
        PersonaDTO dto = new PersonaDTO();
        dto.setId(entity.getId());
        dto.setNombre(entity.getNombre());
        dto.setApellido(entity.getApellido());
        dto.setDocumento(entity.getDocumento());
        dto.setDocumentoTipo(entity.getDocumentoTipo());
        dto.setCat(entity.getCat());
        dto.setEmail(entity.getEmail());
        dto.setUsername(entity.getUsername());
        dto.setNacimiento(entity.getNacimiento().toString());
        dto.setIdClub(entity.getClub().getId());
        dto.setIdRol(entity.getRol().getId());
        return dto;
    }
}
