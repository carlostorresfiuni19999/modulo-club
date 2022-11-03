package com.sd2022.club.service.traspasoService;

import com.sd2022.club.dao.IPersonaRepository;
import com.sd2022.club.dao.ITraspasoDetalleRepository;
import com.sd2022.club.dao.ITraspasoRepository;
import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.traspaso.TraspasoDTO;
import com.sd2022.club.dtos.traspaso.TraspasoDTOResult;
import com.sd2022.club.service.baseService.BaseServiceImpl;
import com.sd2022.club.service.traspasoDetalleService.TraspasoDetalleServiceImpl;
import com.sd2022.entities.models.Club;
import com.sd2022.entities.models.Persona;
import com.sd2022.entities.models.Traspaso;
import com.sd2022.entities.models.TraspasoDetalle;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;


import java.util.Date;
import java.util.List;


@Service
public class TraspasoServiceImpl extends BaseServiceImpl<TraspasoDTO, Traspaso, BaseResultDTO<TraspasoDTO>> implements ITraspasoService{

    private Logger log = Logger.getLogger(TraspasoServiceImpl.class);
    @Autowired
    private ITraspasoRepository traspasoRepo;

    @Autowired
    private TraspasoDetalleServiceImpl service;

    @Autowired
    private IPersonaRepository personaRepo;

    @Autowired
    private ITraspasoDetalleRepository detalleRepo;

    @Autowired
    private Environment env;
    @Override
    public Traspaso toEntity(TraspasoDTO dto) {
        Traspaso cabecera = new Traspaso();
        cabecera.setFechaTraspaso(new Date());
        return cabecera;
    }

    @Override
    public TraspasoDTO toDTO(Traspaso entity) {
        TraspasoDTO dto = new TraspasoDTO();
        dto.setId(entity.getId());
        dto.setFechaTraspaso(entity.getFechaTraspaso());
        return dto;
    }

    @Override
    public ResponseEntity<TraspasoDTO> findById(int id) {
        Traspaso traspaso = traspasoRepo.findById(id);

        if(traspaso == null) {
            log.error(env.getProperty("notfound"));
            return new ResponseEntity(HttpStatus.NOT_FOUND);

        }
        return new ResponseEntity<TraspasoDTO>(toDTO(traspaso), HttpStatus.OK);
    }

    @Override
    public ResponseEntity<BaseResultDTO<TraspasoDTO>> getAll(Pageable page) {
        List<TraspasoDTO> dtos = traspasoRepo.findAll(page)
                .map(this::toDTO)
                .getContent();

        BaseResultDTO<TraspasoDTO> result = new TraspasoDTOResult();
        result.setDtos(dtos);
        return new ResponseEntity<BaseResultDTO<TraspasoDTO>>(result, HttpStatus.OK);
    }

    @Override
    public ResponseEntity remove(int id) {
        Traspaso cabecera = traspasoRepo.findById(id);
        if(cabecera == null) {
            log.error(env.getProperty("notfound"));
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }



        List<TraspasoDetalle> detalles = detalleRepo.findByIdTraspaso(id);

        for(TraspasoDetalle detalle : detalles){
            try{
                Persona p = detalle.getJugador();
                Club origen = detalle.getClubOrigen();
                p.setClub(origen);
                personaRepo.save(p);
               detalleRepo.deleteById(detalle.getId());
            } catch (Exception e){
                log.error(e);
                return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
            }
        }

        try{
            traspasoRepo.deleteById(id);
        } catch (Exception e){
            log.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }

    @Override
    public ResponseEntity<TraspasoDTO> edit(int id, TraspasoDTO dto) {
        return null;
    }

    @Override
    public ResponseEntity<TraspasoDTO> add(TraspasoDTO dto) {
        Traspaso t = toEntity(dto);
        try {
            t = traspasoRepo.save(t);
        } catch (Exception e){
            log.error(e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }

        return new ResponseEntity(t, HttpStatus.OK);
    }

    @Override
    public ResponseEntity filtrarEntreFechas(Date inicio, Date fin, Pageable page) {
        List<TraspasoDTO> dtos = traspasoRepo.filtrarEntreFechas(inicio, fin, page)
                .map(this::toDTO)
                .getContent();

        BaseResultDTO<TraspasoDTO> result = new TraspasoDTOResult();
        result.setDtos(dtos);
        return new ResponseEntity<BaseResultDTO<TraspasoDTO>>(result, HttpStatus.OK);
    }






}
