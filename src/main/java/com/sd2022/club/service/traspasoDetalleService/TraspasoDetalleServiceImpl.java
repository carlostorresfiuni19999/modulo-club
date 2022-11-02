package com.sd2022.club.service.traspasoDetalleService;

import com.sd2022.club.dao.IClubRepository;
import com.sd2022.club.dao.IPersonaRepository;
import com.sd2022.club.dao.ITraspasoDetalleRepository;
import com.sd2022.club.dao.ITraspasoRepository;
import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.traspasodetalle.TraspasoDetalleDTO;
import com.sd2022.club.dtos.traspasodetalle.TraspasoDetalleResultDTO;
import com.sd2022.club.service.baseService.BaseServiceImpl;
import com.sd2022.entities.models.Club;
import com.sd2022.entities.models.Persona;
import com.sd2022.entities.models.Traspaso;
import com.sd2022.entities.models.TraspasoDetalle;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TraspasoDetalleServiceImpl extends BaseServiceImpl<TraspasoDetalleDTO, TraspasoDetalle, BaseResultDTO<TraspasoDetalleDTO>> implements ITraspasoDetalleService {

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
            throw new Exception("No se puede hacer um traspaso sin la cabecera principal");
        }
        ent.setTraspaso(cabecera);
        Club destino = clubRepo.findById(dto.getClubDestino());
        Persona traspasable = personaRepo.findById(dto.getIdPersona());

        if(traspasable == null || traspasable.isDeleted()) {
            throw new Exception("Jugador Seleccionado no existe");
        }

        if(!(traspasable.getRol() != null && traspasable.getRol().getRol().equals("JUGADOR"))){
            throw new Exception("No se pueden trasferir a una Persona que no es jugador");

        }

        Club clubOrigen = clubRepo.findById(traspasable.getClub().getId());

        if(clubOrigen == null || clubOrigen.isDeleted()){
            throw new Exception("No se ha encontrado ningun club asociado al jugador");
        }

        if(clubOrigen.getId() == destino.getId()){
            throw new Exception("No se pueden trasferir al mismo club de procedencia");

        }



        if(destino == null || destino.isDeleted()){
            throw new Exception("No existe el club destino");

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

        BaseResultDTO<TraspasoDetalleDTO> result = new TraspasoDetalleResultDTO();

        result.setDtos(dtos);

        return new ResponseEntity<BaseResultDTO<TraspasoDetalleDTO>>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity remove(int id) {
        try{
            traspasoDetalleRepo.deleteById(id);
            return new ResponseEntity(HttpStatus.OK);
        }catch (Exception e){
            e.printStackTrace();
        }

        return new ResponseEntity(HttpStatus.BAD_REQUEST);
    }

    @Override
    public ResponseEntity<TraspasoDetalleDTO> edit(int id, TraspasoDetalleDTO dto) {
        if(id != dto.getId()){
            return new ResponseEntity("No se actualizan las claves", HttpStatus.BAD_REQUEST);

        }

        TraspasoDetalle ent = traspasoDetalleRepo.findById(id);

        if(ent.getTraspaso().getId() != dto.getIdTraspaso()){
            return new ResponseEntity("No se cambian de Cabeceras", HttpStatus.BAD_REQUEST);
        }

        try{
            ent = toEntity(dto);
            traspasoDetalleRepo.save(ent);
            return new ResponseEntity<>(dto, HttpStatus.OK);
        } catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<TraspasoDetalleDTO> add(TraspasoDetalleDTO dto) {
        return null;
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
