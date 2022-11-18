package com.sd2022.club.service.traspasoDetalleService;

import com.sd2022.club.dao.IClubRepository;
import com.sd2022.club.dao.IPersonaRepository;
import com.sd2022.club.dao.ITraspasoDetalleRepository;
import com.sd2022.club.dao.ITraspasoRepository;
import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.traspasodetalle.TraspasoDetalleDTO;
import com.sd2022.club.dtos.traspasodetalle.TraspasoDetalleResultDTO;
import com.sd2022.club.errors.BadRequestException;
import com.sd2022.club.errors.NotFoundException;
import com.sd2022.club.service.baseService.BaseServiceImpl;
import com.sd2022.club.utils.Settings;
import com.sd2022.entities.models.*;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
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

    @Autowired
    private CacheManager cacheManager;


    @Autowired
    private Settings settings;

    @Override
    public TraspasoDetalle toEntity(TraspasoDetalleDTO dto) throws NotFoundException, BadRequestException {
        TraspasoDetalle ent = new TraspasoDetalle();
        Traspaso cabecera = traspasoRepo.findById(dto.getIdTraspaso());

        if(cabecera == null){
            throw new BadRequestException(env.getProperty("cabeceraerror"));
        }
        ent.setTraspaso(cabecera);
        Club destino = clubRepo.findById(dto.getClubDestino());
        Persona traspasable = personaRepo.findById(dto.getIdPersona());

        if(traspasable == null || traspasable.isDeleted()) {
            throw new NotFoundException(env.getProperty("personanotfound"));
        }

        if(traspasable.getRol().getRol().equals(env.getProperty("roldefault"))){
            throw new BadRequestException(env.getProperty("notransferible"));
        }

        Club clubOrigen = clubRepo.findById(traspasable.getClub().getId());

        if(clubOrigen == null || clubOrigen.isDeleted()){
            throw new NotFoundException(env.getProperty("cluborigennotfound"));
        }
        if( destino == null ||destino.isDeleted()){
            throw new BadRequestException(env.getProperty("clubdestinoerror"));
        }

        ent.setId(dto.getId());
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


    public TraspasoDetalleDTO findById(int id) throws NotFoundException {
       return null;
    }

    @Override
    public BaseResultDTO<TraspasoDetalleDTO> getAll(Pageable page) {
        return null;
    }

    @Override
    public void remove(int id) throws NotFoundException {
    }

    @Override
    public TraspasoDetalleDTO edit(int id, TraspasoDetalleDTO dto) throws NotFoundException, BadRequestException {
        return null;
    }

    @Override
    public TraspasoDetalleDTO add(TraspasoDetalleDTO dto) throws NotFoundException, BadRequestException {
        return null;
    }

    @Override
    public TraspasoDetalleResultDTO getByIdTraspaso(int idTraspaso, Pageable page) {
        List<TraspasoDetalleDTO> dtos = traspasoDetalleRepo.findByIdTraspaso(idTraspaso, page)
                .map(r -> {
                    TraspasoDetalleDTO d = toDTO(r);
                    cacheManager.getCache(settings.getCacheName()).putIfAbsent("traspaso_detalle_api_"+d.getId(), d);
                    return d;
                })
                .getContent();

        TraspasoDetalleResultDTO result = new TraspasoDetalleResultDTO();

        result.setDtos(dtos);

        return result;
    }
}
