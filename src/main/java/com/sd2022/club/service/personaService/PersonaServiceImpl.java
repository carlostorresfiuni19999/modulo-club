package com.sd2022.club.service.personaService;

import com.sd2022.club.dao.IClubRepository;
import com.sd2022.club.dao.IPersonaRepository;
import com.sd2022.club.dao.IRolRepository;
import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.persona.PersonaDTO;
import com.sd2022.club.dtos.persona.PersonaResultDTO;
import com.sd2022.club.errors.BadRequestException;
import com.sd2022.club.errors.NotFoundException;
import com.sd2022.club.service.baseService.BaseServiceImpl;
import com.sd2022.club.utils.Settings;
import com.sd2022.entities.models.Club;
import com.sd2022.entities.models.Persona;
import com.sd2022.entities.models.Rol;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PersonaServiceImpl extends BaseServiceImpl<PersonaDTO, Persona, BaseResultDTO<PersonaDTO>> implements IPersonaService{

    @Autowired
    private IPersonaRepository personaRepo;

    @Autowired
    private IRolRepository rolRepo;

    @Autowired
    private IClubRepository clubRepo;

    @Autowired
    private Environment env;

    @Autowired
    private CacheManager cacheManager;

    @Autowired
    private Settings settings;


    private Logger log = Logger.getLogger(PersonaServiceImpl.class);

    @Cacheable(value = Settings.CACHE_NAME,key = "'persona_api_' +#id")
    @Override
    public PersonaDTO findById(int id) throws NotFoundException {
        Persona p = personaRepo.findById(id);
        if(p == null || p.isDeleted()){
           throw new NotFoundException(env.getProperty("notfound"));
        }
        return toDTO(p);
    }

    @Override
    public BaseResultDTO<PersonaDTO> getAll(Pageable page){
        List<PersonaDTO> dtos = personaRepo.findByDeleted(false, page)
                .map(r -> {
                    PersonaDTO d = toDTO(r);
                    cacheManager.getCache(settings.getCacheName()).putIfAbsent("persona_api_"+d.getId(), d);
                    return d;
                })
                .getContent();

        BaseResultDTO<PersonaDTO> result = new PersonaResultDTO();

        result.setPages(personaRepo.findByDeleted(false, page).getTotalPages());

        result.setDtos(dtos);

        return result;

    }

    @CacheEvict(value = Settings.CACHE_NAME, key = "'persona_api_' +#id")
    @Override
    public void remove(int id) throws  NotFoundException{

        Persona del = personaRepo.findById(id);
        if(del == null || del.isDeleted()){
            throw new NotFoundException(env.getProperty("notfound"));
        }
        try {
            del.setRol(null);
            del.setDeleted(true);
            personaRepo.save(del);
        } catch (Exception e){
            log.error(e);
        }


    }


    @Override
    public PersonaDTO edit (int id, PersonaDTO dto) throws BadRequestException, NotFoundException{
        if(id != dto.getId()){
            throw new BadRequestException(env.getProperty("primarykeyerror"));
        } else{
            Persona p = personaRepo.findById(dto.getId());

            Rol rol = rolRepo.findById(dto.getIdRol());
            if(personaRepo.existsByEmail(dto.getEmail()) && !(p.getEmail().equals(dto.getEmail()))){
                throw new BadRequestException(env.getProperty("existemailerror"));
            }
            if(personaRepo.existsByUsername(dto.getUsername()) && !(p.getUsername().equals(dto.getUsername()))){
                throw new BadRequestException(env.getProperty("existusernameerror"));
            }
            if(rol == null || rol.equals(env.getProperty("roldefault"))){
                throw new BadRequestException(env.getProperty("rolerror"));
            }



            if(p == null || p.isDeleted()){
                throw new NotFoundException(env.getProperty("notfound"));
            } else{
                Persona upd = null;
                try {
                    upd = toEntity(dto);
                } catch (ParseException e) {
                    throw new BadRequestException(env.getProperty("fechaserror")+" "+env.getProperty("formatofecha"));
                }
                upd.setId(dto.getId());
                cacheManager.getCache(settings.getCacheName()).put("persona_api_"+dto.getId(), dto);
                personaRepo.save(upd);

                upd = personaRepo.findById(dto.getId());
                return toDTO(upd);
            }
        }
    }


    @Override
    public PersonaDTO add(PersonaDTO dto) throws BadRequestException{

        Persona toSave = null;
        try {
            toSave = toEntity(dto);
        } catch (ParseException e) {
            throw new BadRequestException(env.getProperty("fechaserror")+" "+env.getProperty("formatofecha"));
        }

        if(personaRepo.existsByEmail(dto.getEmail())){
            throw new BadRequestException(env.getProperty("existemailerror"));

        }
        if(personaRepo.existsByUsername(dto.getUsername())){
            throw new BadRequestException(env.getProperty("existusernameerror"));
        }

        if((toSave.getRol() == null || toSave.getRol().equals(env.getProperty("roldefault")))
        && (toSave.getClub() != null)){
            throw new BadRequestException(env.getProperty("rolerror"));
        }

        toSave = personaRepo.save(toSave);
        cacheManager.getCache(settings.getCacheName()).put("persona_api_"+toSave.getId(), toDTO(toSave));
        return  toDTO(toSave);

    }




    @Override
    public BaseResultDTO<PersonaDTO> findByCat(int cat, Pageable page) {
        List<PersonaDTO> dtos = personaRepo.findByCatAndDeleted(cat, false, page)
                .map(r -> {
                    PersonaDTO d = toDTO(r);
                    cacheManager.getCache(settings.getCacheName()).putIfAbsent("persona_api_"+d.getId(), d);
                    return d;
                })
                .getContent();

        BaseResultDTO<PersonaDTO> result = new PersonaResultDTO();

        result.setDtos(dtos);

        return result;

    }

    @Override
    public BaseResultDTO<PersonaDTO> getAll() {
         BaseResultDTO<PersonaDTO> result = new PersonaResultDTO();
         result.setDtos(personaRepo.findByDeleted(false).stream().map(this::toDTO).collect(Collectors.toList()));
         result.setPages(0);

         result.getDtos().forEach(d -> {
             cacheManager.getCache(settings.getCacheName()).putIfAbsent("persona_api_"+d.getId(), d);
         });
         return result;
    }


    @Override
    public Persona toEntity(PersonaDTO dto) throws ParseException {
        Persona ent = new Persona();

        SimpleDateFormat format = new SimpleDateFormat(env.getProperty("formatofecha"));
        ent.setNombre(dto.getNombre());
        ent.setApellido(dto.getApellido());
        ent.setCat(dto.getCat());
        Club club = clubRepo.findById(dto.getIdClub());
        ent.setClub(club);
        ent.setEmail(dto.getEmail());
        ent.setDocumento(dto.getDocumento());
        ent.setDocumentoTipo(dto.getDocumentoTipo());
        ent.setUsername(dto.getUsername());
        ent.setNacimiento(format.parse(dto.getNacimiento()));

        Rol rol = rolRepo.findById(dto.getIdRol());
        ent.setRol(rol);

        return ent;
    }

    @Scheduled(cron = Settings.CRON)
    public void loadPersonas(){
        personaRepo.findByDeleted(false)
                .stream().map(this::toDTO)
                .forEach(d -> {
                    cacheManager.getCache(settings.getCacheName()).putIfAbsent("persona_api_"+d.getId(), d);
                });
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
        String nacimiento = entity.getNacimiento().toString();
        dto.setNacimiento(nacimiento);
        if(entity.getClub() == null || entity.getClub().isDeleted()){
            dto.setIdClub(-1);
        }else{
            dto.setIdClub(entity.getClub().getId());
        }

        if(entity.getRol() == null){
            dto.setIdRol(-1);
        }else{
            dto.setIdRol(entity.getRol().getId());
        }

        return dto;
    }
}
