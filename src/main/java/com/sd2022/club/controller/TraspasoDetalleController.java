package com.sd2022.club.controller;

import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.traspasodetalle.TraspasoDetalleDTO;
import com.sd2022.club.errors.BadRequestException;
import com.sd2022.club.errors.NotFoundException;
import com.sd2022.club.service.traspasoDetalleService.TraspasoDetalleServiceImpl;
import org.jboss.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/traspaso/detalle")
public class TraspasoDetalleController {

    private final Logger log = Logger.getLogger(TraspasoDetalleController.class);
    @Autowired
    private TraspasoDetalleServiceImpl service;

    @Autowired
    private Environment env;


    @CrossOrigin(origins = "*")
    @GetMapping("/page/{page}/{id}")
    public ResponseEntity findByIdTraspaso(@PathVariable(value = "page") int page, @PathVariable(value = "id") int id){

        BaseResultDTO result =
                service.getByIdTraspaso(id, PageRequest.of(page, Integer.parseInt(env.getProperty("pagesize"))));

        return new ResponseEntity(result, HttpStatus.OK);
    }

}
