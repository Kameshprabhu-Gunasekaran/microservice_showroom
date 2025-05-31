package com.bikeservice.controller;

import com.bikeservice.dto.ResponseDTO;
import com.bikeservice.dto.ShowroomDTO;
import com.bikeservice.service.ShowroomService;
import com.common.entity.Showroom;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/showroom")
public class ShowroomController {

    private final ShowroomService showroomService;

    public ShowroomController(ShowroomService showroomService) {
        this.showroomService = showroomService;
    }

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody Showroom showroom) {
        return this.showroomService.create(showroom);
    }

    @GetMapping("/retrieve")
    public ResponseDTO retrieve() {
        return this.showroomService.retrieve();
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieveById(@PathVariable("id") String id) {
        return this.showroomService.retrieveById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO update(@PathVariable("id") String id, @RequestBody ShowroomDTO dto) {
        return this.showroomService.update(id, dto);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO delete(@PathVariable("id") String id) {
        return this.showroomService.delete(id);
    }
}
