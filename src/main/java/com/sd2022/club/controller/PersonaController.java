package com.sd2022.club.controller;


import com.sd2022.club.dtos.persona.PersonaDTO;
import com.sd2022.club.service.personaService.PersonaServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
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

    @GetMapping("/page/{page}")
    public ResponseEntity getAll(@PathVariable(value = "page") int page){
        return service.getAll(PageRequest.of(page, Integer.parseInt(env.getProperty("pagesize"))));
    }

    @PostMapping
    public ResponseEntity add(@RequestBody PersonaDTO persona){
        return service.add(persona);
    }

    @PutMapping("/{id}")
    public ResponseEntity edit(@PathVariable(value= "id") int id, @RequestBody PersonaDTO persona){

            return service.edit(id, persona);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable(value = "id") int id){

            return service.remove(id);

    }

    @GetMapping("/nombre/{nombre}page/{page}")
    public ResponseEntity getByNombre(
            @PathVariable(value = "nombre") String nombre,
            @PathVariable(value = "page") int page){
            return service.findByNombre(nombre.trim().toUpperCase(), PageRequest.of(page, Integer.parseInt(env.getProperty("pagesize"))));

    }

}
