package com.sd2022.club.controller;

import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.traspaso.TraspasoCreateDTO;
import com.sd2022.club.dtos.traspaso.TraspasoDTO;
import com.sd2022.club.errors.BadRequestException;
import com.sd2022.club.errors.NotFoundException;
import com.sd2022.club.service.traspasoService.TraspasoServiceImpl;
import org.apache.log4j.Logger;
import org.springframework.cache.CacheManager;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;




@RestController
@RequestMapping("/traspaso")
public class TraspasoController {
    private final Logger log = Logger.getLogger(TraspasoController.class);
    @Autowired
    private TraspasoServiceImpl service;

    @Autowired
    private Environment env;



    @GetMapping("/page/{page}")
    public ResponseEntity getAll(@PathVariable(value = "page") int page){
        BaseResultDTO result =
                service.getAll(PageRequest.of(page, Integer.parseInt(env.getProperty("pagesize"))));

        return new ResponseEntity(result, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity add(@RequestBody TraspasoCreateDTO traspaso){
        TraspasoDTO result = service.add(traspaso);
        return new ResponseEntity<>(result, HttpStatus.OK);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(value="id") int id){
        try {
            service.remove(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (NotFoundException e) {
            log.error(e);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable(value ="id") int id){
        TraspasoDTO result;
        try {
            result = service.findById(id);
        } catch (NotFoundException e) {
            log.error(e);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/fechas/{inicio}/{fin}/{page}")
    public ResponseEntity filtrarEnFechas(@PathVariable("inicio") String inicio, @PathVariable("fin") String fin, @PathVariable(value = "page") int page){
        try {
            BaseResultDTO result =
                    service.filtrarEntreFechas(
                            inicio, fin, PageRequest.of(page, Integer.parseInt(env.getProperty("pagesize")))
                    );

            return new ResponseEntity(result, HttpStatus.OK);
        } catch (BadRequestException e) {
            log.error(e);
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity edit(@PathVariable(value = "id") int id, @RequestBody TraspasoCreateDTO dto){
        try {
            TraspasoDTO result = service.save(dto);
            return new ResponseEntity(result, HttpStatus.OK);
        } catch (NotFoundException e) {
            log.error(e);
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (BadRequestException e) {
            log.error(e);
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
