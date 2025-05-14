package com.userService.controller;

import com.userService.dto.ResponseDTO;
import com.userService.dto.SalesmanDTO;
import com.userService.service.SalesmanService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/salesman")
public class SalesmanController {

    private final SalesmanService salesmanService;

    public SalesmanController(SalesmanService salesmanService) {
        this.salesmanService = salesmanService;
    }

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody SalesmanDTO dto) {
        return this.salesmanService.create(dto);
    }

    @GetMapping("/retrieve")
    public ResponseDTO retrieve() {
        return this.salesmanService.retrieve();
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieveById(@PathVariable("id") String id) {
        return this.salesmanService.retrieveById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO update(@PathVariable("id") String id, @RequestBody SalesmanDTO dto) {
        return this.salesmanService.update(id, dto);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO delete(@PathVariable("id") String id) {
        return this.salesmanService.delete(id);
    }
}
