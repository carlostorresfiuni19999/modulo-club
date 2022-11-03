package com.sd2022.club.controller;

import com.sd2022.club.dtos.traspaso.TraspasoDTO;
import com.sd2022.club.service.traspasoService.TraspasoServiceImpl;
import org.springframework.core.env.Environment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;

@RestController
@RequestMapping("/traspaso")
public class TraspasoController {
    @Autowired
    private TraspasoServiceImpl service;

    @Autowired
    private Environment env;

    @GetMapping("/page/{page}")
    public ResponseEntity getAll(@PathVariable(value = "page") int page){
        return service.getAll(PageRequest.of(page, Integer.parseInt(env.getProperty("pagesize"))));
    }

    @PostMapping
    public ResponseEntity add(@RequestBody TraspasoDTO traspaso){
        return service.add(traspaso);
    }



    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(value="id") int id){
        return service.remove(id);
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable(value ="id") int id){
        return service.findById(id);
    }

    @GetMapping("/fechas/{inicio}/{fin}/{page}")
    public ResponseEntity filtrarEnFechas(@PathVariable("inicio") Date inicio, @PathVariable("fin") Date fin, @PathVariable(value = "page") int page){
        return service.filtrarEntreFechas(inicio, fin, PageRequest.of(page, Integer.parseInt(env.getProperty("pagenum"))));
    }



}
