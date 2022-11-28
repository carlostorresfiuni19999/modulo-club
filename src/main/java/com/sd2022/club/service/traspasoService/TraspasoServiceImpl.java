package com.sd2022.club.service.traspasoService;

import com.sd2022.club.dao.IPersonaRepository;
import com.sd2022.club.dao.ITraspasoDetalleRepository;
import com.sd2022.club.dao.ITraspasoRepository;
import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.traspaso.TraspasoCreateDTO;
import com.sd2022.club.dtos.traspaso.TraspasoDTO;
import com.sd2022.club.dtos.traspaso.TraspasoDTOResult;
import com.sd2022.club.dtos.traspasodetalle.TraspasoDetalleDTO;
import com.sd2022.club.errors.BadRequestException;
import com.sd2022.club.errors.NotFoundException;
import com.sd2022.club.service.baseService.BaseServiceImpl;
import com.sd2022.club.service.traspasoDetalleService.TraspasoDetalleServiceImpl;
import com.sd2022.club.utils.Settings;
import com.sd2022.entities.models.Club;
import com.sd2022.entities.models.Persona;
import com.sd2022.entities.models.Traspaso;
import com.sd2022.entities.models.TraspasoDetalle;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.*;


@Service
public class TraspasoServiceImpl extends BaseServiceImpl<TraspasoDTO, Traspaso, BaseResultDTO<TraspasoDTO>> implements ITraspasoService{

    private final Logger log = Logger.getLogger(TraspasoServiceImpl.class);
    @Autowired
    private ITraspasoRepository traspasoRepo;

    @Autowired
    private TraspasoDetalleServiceImpl service;

    @Autowired
    private IPersonaRepository personaRepo;

    @Autowired
    private ITraspasoDetalleRepository detalleRepo;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private Environment env;

    @Autowired
    private Settings settings;

    @Override
    public Traspaso toEntity(TraspasoDTO dto) {
        Traspaso cabecera = new Traspaso();
        cabecera.setFechaTraspaso(new Date());
        cabecera.setDescripcion(dto.getDescripcion());
        return cabecera;
    }


    @Override
    public TraspasoDTO toDTO(Traspaso entity) {
        TraspasoDTO dto = new TraspasoDTO();
        dto.setId(entity.getId());
        Date fechaTraspaso = entity.getFechaTraspaso();
        dto.setFechaTraspaso(fechaTraspaso.toString());
        dto.setDescripcion(entity.getDescripcion());
        return dto;
    }

    @Transactional
    @Cacheable(value = Settings.CACHE_NAME, key = "'traspaso_api_' +#id")
    @Override
    public TraspasoDTO findById(int id) throws NotFoundException {
        Traspaso traspaso = traspasoRepo.findById(id);

        if(traspaso == null) {
           throw new NotFoundException(env.getProperty("notfound"));
        }
        return toDTO(traspaso);
    }

    @Override
    @Transactional
    public BaseResultDTO<TraspasoDTO> getAll(Pageable page) {
        List<TraspasoDTO> dtos = traspasoRepo.findAll(page)
                .map(r -> {
                    TraspasoDTO d = toDTO(r);
                    cacheManager.getCache(settings.getCacheName()).putIfAbsent("traspaso_api_"+d.getId(), d);
                    return d;
                })
                .getContent();

        BaseResultDTO<TraspasoDTO> result = new TraspasoDTOResult();
        result.setDtos(dtos);
        result.setPages(traspasoRepo.findAll(page).getTotalPages());
        return result;
    }

    @Override
    @Transactional
    public void remove(int id) throws NotFoundException {
        Traspaso cabecera = traspasoRepo.findById(id);
        if(cabecera == null) {
            throw new NotFoundException(env.getProperty("notfound"));
        }



        List<TraspasoDetalle> detalles = cabecera.getTraspasoDetalles();

        for(TraspasoDetalle detalle : detalles){
            try{
                Persona p = detalle.getJugador();
                Club origen = detalle.getClubOrigen();
                p.setClub(origen);
                cacheManager.getCache(settings.getCacheName()).evictIfPresent("traspaso_detalle_api_"+detalle.getId());
                cacheManager.getCache(settings.getCacheName()).evictIfPresent("persona_api_"+p.getId());
                personaRepo.save(p);
                detalleRepo.deleteById(detalle.getId());
            } catch (Exception e){
                log.error(e);
            }
        }
        cacheManager.getCache(settings.getCacheName()).evictIfPresent("traspaso_api_"+id);
        traspasoRepo.deleteById(id);


    }

    @Override
    public TraspasoDTO edit(int id, TraspasoDTO dto) {
        return null;
    }

    @Override
    public TraspasoDTO add(TraspasoDTO dto) {
        return null;
    }

    @Override
    @Transactional
    public BaseResultDTO<TraspasoDTO> filtrarEntreFechas(String inicio, String fin, Pageable page) throws BadRequestException {

        Date fechaInicio;
        Date fechaFin;

        SimpleDateFormat format = new SimpleDateFormat(env.getProperty("formatofecha"));

        try{
            fechaInicio= format.parse(inicio);
            fechaFin =format.parse(fin);
        } catch (Exception e){
            throw new BadRequestException(env.getProperty("fechaserror")+" "+env.getProperty("formatofecha"));
        }
        List<TraspasoDTO> dtos = traspasoRepo.filtrarEntreFechas(fechaInicio, fechaFin, page)
                .map(r -> {
                    TraspasoDTO d = toDTO(r);
                    cacheManager.getCache(settings.getCacheName()).putIfAbsent("traspaso_api_"+d.getId(), d);
                    return d;
                })
                .getContent();

        BaseResultDTO<TraspasoDTO> result = new TraspasoDTOResult();
        result.setDtos(dtos);
        return result;
    }


    @Override
    @Transactional
    public TraspasoDTO save(TraspasoCreateDTO traspaso) throws NotFoundException, BadRequestException {

        Persona p;
        Club c;
        Traspaso cabecera = toEntity(traspaso);

            for (TraspasoDetalleDTO detalle : traspaso.getDetalles()) {

                detalle.setIdTraspaso(cabecera.getId());
                TraspasoDetalle nuevoDetalle = service.toEntity(detalle);
                p = nuevoDetalle.getJugador();
                c = nuevoDetalle.getClubDestino();
                p.setClub(c);
                cacheManager.getCache(settings.getCacheName()).evict("persona_api_"+p.getId());
                personaRepo.save(p);
                cabecera.addDetalle(nuevoDetalle);
            }


        cabecera = traspasoRepo.save(cabecera);

        cacheManager.getCache(settings.getCacheName()).put("traspaso_api_"+cabecera.getId(), toDTO(cabecera));
        return toDTO(cabecera);
    }

    @Transactional
    public TraspasoDTO toEdit(int id, TraspasoCreateDTO traspaso) throws BadRequestException, NotFoundException {
        if(id != traspaso.getId()){
            throw new BadRequestException(env.getProperty("primarykeyerror"));
        }


        Traspaso cabecera = traspasoRepo.findById(id);
        cabecera.setFechaTraspaso(new Date());




        if(cabecera == null )
            throw new NotFoundException(env.getProperty("notfound"));

        List<TraspasoDetalle> nuevoDetalles = new ArrayList<>();

        for (TraspasoDetalleDTO detalle : traspaso.getDetalles()) {
            detalle.setIdTraspaso(cabecera.getId());
            TraspasoDetalle nuevoDetalle = service.toEntity(detalle);
            Persona p = nuevoDetalle.getJugador();
            Club c = nuevoDetalle.getClubDestino();
            p.setClub(c);
            cacheManager.getCache(settings.getCacheName()).evict("persona_api_"+p.getId());
            personaRepo.save(p);
            nuevoDetalles.add(nuevoDetalle);

        }

        cacheManager.getCache(settings.getCacheName()).evictIfPresent("traspaso_api_"+cabecera.getId());
        cabecera.editDetalle(nuevoDetalles);
        cabecera = traspasoRepo.save(cabecera);

        return toDTO(cabecera);
    }
}
