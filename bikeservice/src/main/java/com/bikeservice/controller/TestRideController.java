package com.bikeservice.controller;

import com.bikeservice.dto.ResponseDTO;
import com.bikeservice.dto.TestRideDTO;
import com.bikeservice.service.TestRideService;
import com.common.entity.TestRide;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/testride")
public class TestRideController {

    private final TestRideService testRideService;

    public TestRideController(TestRideService testRideService) {
        this.testRideService = testRideService;
    }

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody TestRide testRide) {
        return this.testRideService.create(testRide);
    }

    @GetMapping("/retrieve")
    public ResponseDTO retrieve() {
        return this.testRideService.retrieve();
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieveById(@PathVariable("id") String id) {
        return this.testRideService.retrieveById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO update(@PathVariable("id") String id, @RequestBody TestRideDTO dto) {
        return this.testRideService.update(id, dto);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO delete(@PathVariable("id") String id) {
        return this.testRideService.delete(id);
    }
}
