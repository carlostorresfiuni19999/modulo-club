package com.sd2022.club.controller;

import com.sd2022.club.dtos.base.BaseResultDTO;
import com.sd2022.club.dtos.club.ClubDTO;
import com.sd2022.club.errors.BadRequestException;
import com.sd2022.club.errors.NotFoundException;
import com.sd2022.club.service.clubService.ClubServiceImpl;
import lombok.Getter;
import lombok.Setter;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
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

    private Logger log = Logger.getLogger(ClubController.class);

    @GetMapping("/page/{pageNum}")
    public ResponseEntity<BaseResultDTO<ClubDTO>> getAll(@PathVariable(value="pageNum") int pageNum){
        BaseResultDTO result = service.getAll(PageRequest.of(pageNum, Integer.parseInt(env.getProperty("pagesize"))));
        return new ResponseEntity<>(result, HttpStatus.OK);
    }


    @PostMapping()
    public ResponseEntity<ClubDTO> add(@RequestBody ClubDTO club){

        try{
            ClubDTO save= service.add(club);
            return new ResponseEntity<ClubDTO>(save, HttpStatus.OK);
        }catch (BadRequestException e){
            log.error(e);
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PutMapping("/{id}")
    public ResponseEntity edit(@PathVariable int id, @RequestBody ClubDTO club){
        try{
            ClubDTO save= service.edit(id, club);
            return new ResponseEntity<ClubDTO>(save, HttpStatus.OK);
        }catch (BadRequestException e){
            log.error(e);
            return new ResponseEntity(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (NotFoundException e) {
            log.error(e);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity delete(@PathVariable int id){
        try{
            service.remove(id);
            return new ResponseEntity(HttpStatus.OK);
        } catch (NotFoundException e){
            log.error(e);
            return new ResponseEntity(HttpStatus.NOT_FOUND);
        }
    }



    @GetMapping("/{id}")
    public ResponseEntity<ClubDTO> findById(@PathVariable(value = "id") int id){
        try {
            ClubDTO resp = service.findById(id);
            return new ResponseEntity<>(resp, HttpStatus.OK);
        } catch (NotFoundException e) {
            return new ResponseEntity(e.getMessage(), HttpStatus.NOT_FOUND);
        }
    }



}
