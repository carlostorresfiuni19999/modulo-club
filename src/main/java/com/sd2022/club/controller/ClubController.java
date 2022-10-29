package com.sd2022.club.controller;

import com.sd2022.club.models.dtos.ClubDTO;
import com.sd2022.club.service.ClubService;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/club")
@Getter
@Setter
public class ClubController {
    @Autowired
    private ClubService service;

    @GetMapping()
    public Page<ClubDTO> getAll(Pageable page){
        return service.getAll(page);
    }

    @PostMapping()
    public ClubDTO add(@RequestBody ClubDTO club){
        return service.add(club);
    }

    @PutMapping("/{id}")
    public ClubDTO edit(@PathVariable int id, @RequestBody ClubDTO club){
        return service.edit(id, club);
    }




}
