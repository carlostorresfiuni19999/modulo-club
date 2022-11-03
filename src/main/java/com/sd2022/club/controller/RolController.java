package com.sd2022.club.controller;

import com.sd2022.club.dtos.rol.RolDTO;
import com.sd2022.club.dtos.rol.RolResultDTO;
import com.sd2022.club.service.rolService.RolServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/rol")
@Getter
@Setter
public class RolController {
    @Autowired
    private RolServiceImpl service;

    @Autowired
    private Environment env;

    @GetMapping("/{id}")
    public ResponseEntity<RolDTO> findById(@PathVariable(value = "id") int id){
        return service.findById(id);
    }

    @PostMapping
    public ResponseEntity<RolDTO> save(@RequestBody RolDTO rol){
        return service.add(rol);
    }

    @PutMapping("/{id}")
    public ResponseEntity edit(@PathVariable int id, @RequestBody RolDTO rol){
        return service.edit(id, rol);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable int id){
        return service.remove(id);
    }

    @GetMapping("page/{page}")
    public ResponseEntity<RolResultDTO> list(@PathVariable(value = "page") int page){
        return service.getAll(PageRequest.of(page, Integer.parseInt(env.getProperty("pagesize"))));
    }
}
