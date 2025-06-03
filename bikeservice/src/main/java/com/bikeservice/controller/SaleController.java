package com.bikeservice.controller;

import com.bikeservice.dto.BikeDTO;
import com.bikeservice.dto.ResponseDTO;
import com.bikeservice.dto.SaleDTO;
import com.bikeservice.service.BikeService;
import com.bikeservice.service.SaleService;
import com.common.entity.Sale;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/sale")
public class SaleController {

    private final SaleService saleService;

    public SaleController(SaleService saleService) {
        this.saleService = saleService;
    }

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody Sale sale) {
        return this.saleService.create(sale);
    }

    @GetMapping("/retrieve")
    public ResponseDTO retrieve() {
        return this.saleService.retrieve();
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieveById(@PathVariable("id") String id) {
        return this.saleService.retrieveById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO update(@PathVariable("id") String id, @RequestBody SaleDTO dto) {
        return this.saleService.update(id, dto);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO delete(@PathVariable("id") String id) {
        return this.saleService.delete(id);
    }
}
