package com.userService.controller;

import com.common.entity.Customer;
import com.userService.dto.CustomerDTO;
import com.userService.dto.ResponseDTO;
import com.userService.service.CustomerService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/customers")
public class CustomerController {

    private final CustomerService customerService;

    public CustomerController(final CustomerService customerService) {
        this.customerService = customerService;
    }

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody final Customer customer) {
        return this.customerService.create(customer);
    }

    @GetMapping("/retrieve-all")
    public ResponseDTO retrieve() {
        return this.customerService.retrieveAll();
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieveById(@PathVariable("id") final String id) {
        return this.customerService.retrieveById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO update(@PathVariable("id") final String id, @RequestBody final CustomerDTO customerDTO) {
        return this.customerService.update(id, customerDTO);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO delete(@PathVariable("id") final String id) {
        return this.customerService.delete(id);
    }
}
