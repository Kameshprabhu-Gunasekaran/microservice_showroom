package com.userService.controller;

import com.common.entity.Role;
import com.userService.dto.ResponseDTO;
import com.userService.dto.RoleDTO;
import com.userService.service.RoleService;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {

    private final RoleService roleService;

    public RoleController(final RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody final RoleDTO roleDTO) {
        return this.roleService.create(roleDTO);
    }

    @GetMapping("/retrieve")
    public ResponseDTO retrieve() {
        return this.roleService.retrieveAll();
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieveById(@PathVariable("id") final String id) {
        return this.roleService.retrieveById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO update(@PathVariable("id") final String id, @RequestBody final String updatedRole) {
        return this.roleService.update(id, updatedRole);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO delete(@PathVariable("id") final String id) {
        return this.roleService.delete(id);
    }
    @GetMapping("/user/{id}")
    public Role user(@PathVariable final String id)
    {
        return this.roleService.findUser(id);
    }
}
