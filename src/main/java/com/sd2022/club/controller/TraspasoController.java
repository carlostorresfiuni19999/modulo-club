package com.sd2022.club.controller;

import com.sd2022.club.service.traspasoService.TraspasoServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/traspaso")
public class TraspasoController {
    @Autowired
    private TraspasoServiceImpl service;

    @GetMapping("/page/{page}")
    public ResponseEntity getAll(@PathVariable(value = "page") int page){
        return service.getAll(PageRequest.of(page, 5));
    }




}
