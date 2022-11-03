package com.sd2022.club.controller;

import com.sd2022.club.dtos.traspasodetalle.TraspasoDetalleDTO;
import com.sd2022.club.service.traspasoDetalleService.TraspasoDetalleServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/traspaso/detalle")
public class TraspasoDetalleController {

    @Autowired
    private TraspasoDetalleServiceImpl service;

    @Autowired
    private Environment env;

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable(value = "id") int id){
        return service.findById(id);
    }

    @GetMapping("/page/{page}")
    public ResponseEntity getAll(@PathVariable(value ="page") int page){
        return service.getAll(PageRequest.of(page, Integer.parseInt(env.getProperty("pagesize"))));
    }

    @GetMapping("/page/{page}/{id}")
    public ResponseEntity findByIdTraspaso(@PathVariable(value = "page") int page, @PathVariable(value = "id") int id){
        return service.getByIdTraspaso(id, PageRequest.of(page, Integer.parseInt(env.getProperty("pagesize"))));
    }

    @PostMapping
    public ResponseEntity add(@RequestBody TraspasoDetalleDTO dto){
        return service.add(dto);
    }

    @PutMapping("/{id}")
    public ResponseEntity edit(@PathVariable(value = "id") int id, @RequestBody TraspasoDetalleDTO dto){
        return service.edit(id, dto);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") int id){
        return service.remove(id);
    }
}
