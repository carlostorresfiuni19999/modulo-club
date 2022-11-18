package com.sd2022.club.controller;

import com.sd2022.club.dtos.rol.RolDTO;
import com.sd2022.club.dtos.rol.RolResultDTO;
import com.sd2022.club.errors.BadRequestException;
import com.sd2022.club.errors.NotFoundException;
import com.sd2022.club.service.rolService.RolServiceImpl;
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
@RequestMapping("/rol")
@Getter
@Setter
public class RolController {

    private Logger log = Logger.getLogger(RolController.class);
    @Autowired
    private RolServiceImpl service;

    @Autowired
    private Environment env;

    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> findById(@PathVariable(value = "id") int id){
        try {
            RolDTO result = service.findById(id);
            return new ResponseEntity<>(result, HttpStatus.OK);
        } catch (NotFoundException e) {
            log.error(e);
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping
    public ResponseEntity<RolDTO> save(@RequestBody RolDTO rol){
        try {
            return new ResponseEntity<>(service.add(rol), HttpStatus.OK);
        } catch (BadRequestException e) {
           log.error(e);
           return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity edit(@PathVariable int id, @RequestBody RolDTO rol){
        try {
            return new ResponseEntity(service.edit(id, rol), HttpStatus.OK);
        } catch (BadRequestException e) {
            log.error(e);
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            log.error(e);
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable int id){
        try {
            service.remove(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (NotFoundException e) {
            log.error(e);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }

    }

    @CrossOrigin(origins = "*")
    @GetMapping("page/{page}")
    public ResponseEntity<RolResultDTO> list(@PathVariable(value = "page") int page){
        RolResultDTO result = service.getAll(PageRequest.of(page, Integer.parseInt(env.getProperty("pagesize"))));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }
}
