package com.sd2022.club.service.traspasoDetalleService;

import com.sd2022.club.dao.IClubRepository;
import com.sd2022.club.dao.IPersonaRepository;
import com.sd2022.club.dao.ITraspasoDetalleRepository;
import com.sd2022.club.dao.ITraspasoRepository;
import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.traspasodetalle.TraspasoDetalleDTO;
import com.sd2022.club.dtos.traspasodetalle.TraspasoDetalleResultDTO;
import com.sd2022.club.service.baseService.BaseServiceImpl;
import com.sd2022.entities.models.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraspasoDetalleServiceImpl extends BaseServiceImpl<TraspasoDetalleDTO, TraspasoDetalle, BaseResultDTO<TraspasoDetalleDTO>> implements ITraspasoDetalleService {

    private Logger log = Logger.getLogger(TraspasoDetalleServiceImpl.class);
    @Autowired
    private Environment env;
    @Autowired
    private ITraspasoRepository traspasoRepo;

    @Autowired
    private ITraspasoDetalleRepository traspasoDetalleRepo;

    @Autowired
    private IPersonaRepository personaRepo;
    @Autowired
    private IClubRepository clubRepo;
    @Override
    public TraspasoDetalle toEntity(TraspasoDetalleDTO dto) throws Exception {
        TraspasoDetalle ent = new TraspasoDetalle();
        Traspaso cabecera = traspasoRepo.findById(dto.getIdTraspaso());

        if(cabecera == null){
            throw new Exception(env.getProperty("cabeceraerror"));
        }
        ent.setTraspaso(cabecera);
        Club destino = clubRepo.findById(dto.getClubDestino());
        Persona traspasable = personaRepo.findById(dto.getIdPersona());

        if(traspasable == null || traspasable.isDeleted()) {
            throw new Exception(env.getProperty("personanotfound"));
        }

        if(traspasable.getRol().getRol().equals(env.getProperty("roldefault"))){
            throw new Exception(env.getProperty("notransferible"));
        }

        Club clubOrigen = clubRepo.findById(traspasable.getClub().getId());

        if(clubOrigen == null || clubOrigen.isDeleted()){
            throw new Exception(env.getProperty("cluborigennotfound"));
        }
        if( destino == null ||destino.isDeleted()){
            throw new Exception(env.getProperty("clubdestinoerror"));
        }



        ent.setClubDestino(destino);
        ent.setCosto(dto.getCosto());
        ent.setJugador(traspasable);
        ent.setClubOrigen(clubOrigen);


        return ent;
    }

    @Override
    public TraspasoDetalleDTO toDTO(TraspasoDetalle entity) {
        TraspasoDetalleDTO dto = new TraspasoDetalleDTO();
        dto.setIdTraspaso(entity.getTraspaso().getId());
        dto.setCosto(entity.getCosto());
        dto.setClubOrigen(entity.getClubOrigen().getId());
        dto.setClubDestino(entity.getClubDestino().getId());
        dto.setIdPersona(entity.getJugador().getId());
        dto.setId(entity.getId());
        return dto;
    }

    @Override
    public ResponseEntity<TraspasoDetalleDTO> findById(int id) {
        TraspasoDetalle ent = traspasoDetalleRepo.findById(id);

        if(ent != null){
            TraspasoDetalleDTO dto = toDTO(ent);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    @Override
    public ResponseEntity<BaseResultDTO<TraspasoDetalleDTO>> getAll(Pageable page) {
        List<TraspasoDetalleDTO> dtos = traspasoDetalleRepo.findAll(page)
                .map(this::toDTO)
                .getContent();

        TraspasoDetalleResultDTO result = new TraspasoDetalleResultDTO();

        result.setDtos(dtos);
        return new ResponseEntity<BaseResultDTO<TraspasoDetalleDTO>>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity<String> remove(int id) {
        try{
            TraspasoDetalle del = traspasoDetalleRepo.findById(id);
            Traspaso cabecera = del.getTraspaso();
            Club c = del.getClubOrigen();
            Persona p = del.getJugador();
            p.setClub(c);
            personaRepo.save(p);
            traspasoDetalleRepo.deleteById(id);
            return new ResponseEntity<>(HttpStatus.OK);
        }catch (Exception e){
            log.error(e);
        }

        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<TraspasoDetalleDTO> edit(int id, TraspasoDetalleDTO dto) {
        if(id != dto.getId()){
            log.error(env.getProperty("primarykeyerror"));
            return new ResponseEntity(env.getProperty("primarykeyerror"), HttpStatus.BAD_REQUEST);

        }

        TraspasoDetalle ent = traspasoDetalleRepo.findById(id);

        if(ent.getTraspaso().getId() != dto.getIdTraspaso()){
            log.error(env.getProperty("cabeceraerror"));
            return new ResponseEntity(env.getProperty("cabeceraerror"), HttpStatus.BAD_REQUEST);
        }

        try{
            Traspaso cabecera = traspasoRepo.findById(dto.getIdTraspaso());

            Persona persona = personaRepo.findById(dto.getIdPersona());

            if(persona.getRol().getRol().equals(env.getProperty("defaultrol"))){
                    log.error(env.getProperty("notrasnferible"));
                return new ResponseEntity(env.getProperty("notransferible"), HttpStatus.BAD_REQUEST);
            }

            Club dest = clubRepo.findById(dto.getClubDestino());
            persona.setClub(dest);
            personaRepo.save(persona);

            traspasoRepo.save(cabecera);
            try{
                ent = toEntity(dto);
            } catch (Exception e1){
                log.error(e1);
                return new ResponseEntity(e1.getMessage(), HttpStatus.BAD_REQUEST);
            }


            traspasoDetalleRepo.save(ent);

            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e){
            log.error(e);
            return new ResponseEntity( HttpStatus.INTERNAL_SERVER_ERROR);

        }
    }

    @Override
    public ResponseEntity<TraspasoDetalleDTO> add(TraspasoDetalleDTO dto) {
        TraspasoDetalle td;
        try {
            td = toEntity(dto);

            if(td.getClubDestino().getId() == dto.getClubOrigen()){
                log.error(env.getProperty("cluborigenerror"));
                return new ResponseEntity(env.getProperty("cluborigenerror"), HttpStatus.BAD_REQUEST);
            }
        } catch (Exception e) {
            log.error(e);
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
            Persona p = td.getJugador();
            Traspaso cabecera = td.getTraspaso();




            if((p.getRol().getRol().equals(env.getProperty("defaultrol")))){
                log.error(env.getProperty("notransferible"));
                return new ResponseEntity(env.getProperty("notransferible"), HttpStatus.BAD_REQUEST);
            }

            p.setClub(td.getClubDestino());
            personaRepo.save(p);
            td = traspasoDetalleRepo.save(td);
            traspasoRepo.save(cabecera);


        return new ResponseEntity(toDTO(td), HttpStatus.OK);
    }

    @Override
    public ResponseEntity getByIdTraspaso(int idTraspaso, Pageable page) {
        List<TraspasoDetalleDTO> dtos = traspasoDetalleRepo.findByIdTraspaso(idTraspaso, page)
                .map(this::toDTO)
                .getContent();

        TraspasoDetalleResultDTO result = new TraspasoDetalleResultDTO();

        result.setDtos(dtos);

        return new ResponseEntity(result, HttpStatus.OK);
    }
}
