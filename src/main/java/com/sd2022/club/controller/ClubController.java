package com.sd2022.club.controller;

import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.club.ClubDTO;
import com.sd2022.club.service.clubService.ClubServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/club")
@Getter
@Setter
public class ClubController {
    @Autowired
    private ClubServiceImpl service;

    @Autowired
    private Environment env;

    @GetMapping("/page/{pageNum}")
    public ResponseEntity<BaseResultDTO<ClubDTO>> getAll(@PathVariable(value="pageNum") int pageNum){
        return service.getAll(PageRequest.of(pageNum, Integer.parseInt(env.getProperty("pagesize"))));
    }


    @PostMapping()
    public ResponseEntity<ClubDTO> add(@RequestBody ClubDTO club){
        return service.add(club);
    }

    @PutMapping("/{id}")
    public ResponseEntity edit(@PathVariable int id, @RequestBody ClubDTO club){
        return service.edit(id, club);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable int id){
        return service.remove(id);
    }

    @GetMapping("/{sede}/{pagenum}")
    public ResponseEntity<BaseResultDTO<ClubDTO>> findBySede(@PathVariable(value = "sede") String sede, @PathVariable(value = "pagenum") int pagenum){
        return service.findBySede( sede, PageRequest.of(pagenum, Integer.parseInt(env.getProperty("pagesize"))));
    }

    @GetMapping("/cancha/{cancha}")
    public ResponseEntity<ClubDTO> findByCancha(@PathVariable(value = "cancha") String cancha){
        return service.findByCancha(cancha);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClubDTO> findById(@PathVariable(value = "id") int id){
        return service.findById(id);
    }



}
