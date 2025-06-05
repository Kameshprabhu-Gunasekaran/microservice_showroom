package com.bikeservice.controller;

import com.bikeservice.dto.BikeDTO;
import com.bikeservice.dto.ResponseDTO;
import com.bikeservice.service.BikeService;
import com.common.entity.Bike;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/bike")
public class BikeController {

    private final BikeService bikeService;

    public BikeController(final BikeService bikeService) {
        this.bikeService = bikeService;
    }

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody final Bike bike) {
        return this.bikeService.create(bike);
    }

    @GetMapping("/retrieve")
    public ResponseDTO retrieve() {
        return this.bikeService.retrieve();
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieveById(@PathVariable("id") final String id) {
        return this.bikeService.retrieveById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO update(@PathVariable("id") final String id, @RequestBody final BikeDTO dto) {
        return this.bikeService.update(id, dto);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO delete(@PathVariable("id") final String id) {
        return this.bikeService.delete(id);
    }
}
