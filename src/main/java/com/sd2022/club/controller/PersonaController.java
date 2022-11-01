package com.sd2022.club.controller;


import com.sd2022.club.dtos.persona.PersonaDTO;
import com.sd2022.club.service.personaService.PersonaServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
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


    @GetMapping("/page/{page}")
    public ResponseEntity getAll(@PathVariable(value = "page") int page){
        try{
            return service.getAll(PageRequest.of(page, 5));
        } catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }

    }

    @PostMapping
    public ResponseEntity add(@RequestBody PersonaDTO persona){
        try {
            return service.add(persona);
        } catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity edit(@PathVariable(value= "id") int id, @RequestBody PersonaDTO persona){
        try{
            return service.edit(id, persona);
        } catch (Exception e){
            return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }


}
