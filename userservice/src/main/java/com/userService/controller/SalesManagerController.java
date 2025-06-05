package com.userService.controller;

import com.common.entity.SalesManager;
import com.userService.dto.ResponseDTO;
import com.userService.dto.SalesManagerDTO;
import com.userService.service.SalesManagerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/salesmanager")
public class SalesManagerController {

    private final SalesManagerService salesManagerService;

    public SalesManagerController(final SalesManagerService salesManagerService) {
        this.salesManagerService = salesManagerService;
    }

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody final SalesManager salesManager) {
        return this.salesManagerService.create(salesManager);
    }

    @GetMapping("/retrieve")
    public ResponseDTO retrieve() {
        return this.salesManagerService.retrieve();
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieveById(@PathVariable("id") final String id) {
        return this.salesManagerService.retrieveById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO update(@PathVariable("id") final String id, @RequestBody final SalesManagerDTO salesManagerDTO) {
        return this.salesManagerService.update(id, salesManagerDTO);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO delete(@PathVariable("id") final String id) {
        return this.salesManagerService.delete(id);
    }
}
