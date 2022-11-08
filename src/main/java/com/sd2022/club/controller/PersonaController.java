package com.sd2022.club.controller;


import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.persona.PersonaDTO;
import com.sd2022.club.errors.BadRequestException;
import com.sd2022.club.errors.NotFoundException;
import com.sd2022.club.service.personaService.PersonaServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/persona")
@Getter
@Setter
public class PersonaController {
    @Autowired
    private PersonaServiceImpl service;

    @Autowired
    private Environment env;

    private Logger log = Logger.getLogger(PersonaController.class);

    @GetMapping("/page/{page}")
    public ResponseEntity getAll(@PathVariable(value = "page") int page){
        BaseResultDTO result = service.getAll(PageRequest.of(page, Integer.parseInt(env.getProperty("pagesize"))));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity add(@RequestBody PersonaDTO persona){
        PersonaDTO result;
        try{
            result = service.add(persona);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (BadRequestException e){
            log.error(e);
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e){
            log.error(e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity edit(@PathVariable(value= "id") int id, @RequestBody PersonaDTO persona){
        PersonaDTO result;
        try{
            result = service.edit(id, persona);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (BadRequestException e){
            log.error(e);
            return new ResponseEntity(e.getMessage(),HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e){
            log.error(e);
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }catch (Exception e){
            log.error(e);
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") int id){
            try{
                service.remove(id);
                return new ResponseEntity(HttpStatus.OK);
            } catch (NotFoundException e){
                log.error(e);
                return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
            }


    }



}
