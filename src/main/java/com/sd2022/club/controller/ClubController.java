package com.sd2022.club.controller;

import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.club.ClubDTO;
import com.sd2022.club.service.clubService.ClubServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/club")
@Getter
@Setter
public class ClubController {
    @Autowired
    private ClubServiceImpl service;

    @GetMapping("/{pageNum}")
    public BaseResultDTO<ClubDTO> getAll(@PathVariable(value="pageNum") int pageNum){
        return service.getAll(PageRequest.of(pageNum, 5));
    }


    @PostMapping()
    public ClubDTO add(@RequestBody ClubDTO club){
        return service.add(club);
    }

    @PutMapping("/{id}")
    public ClubDTO edit(@PathVariable int id, @RequestBody ClubDTO club){
        return service.edit(id, club);
    }

    @DeleteMapping("/{id}")
    public boolean delete(@PathVariable int id){
        return service.remove(id);
    }

    @GetMapping("/{sede}/{pagenum}")
    public BaseResultDTO<ClubDTO> findBySede(@PathVariable(value = "sede") String sede, @PathVariable(value = "pagenum") int pagenum){
        return service.findBySede( sede, PageRequest.of(pagenum, 5));
    }

    @GetMapping("/{cancha}")
    public ClubDTO findByCancha(@PathVariable(value = "cancha") String cancha){
        return service.findByCancha(cancha);
    }



}
