package com.userService.controller;

import com.common.entity.Role;
import com.common.entity.User;
import com.userService.dto.RegisterRequestDTO;
import com.userService.dto.ResponseDTO;
import com.userService.dto.UserWithRoleDTO;
import com.userService.exception.BadRequestServiceException;
import com.userService.repository.RoleRepository;
import com.userService.repository.UserRepository;
import com.userService.service.UserService;
import com.userService.util.Constant;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserController(final UserService userService, final UserRepository userRepository, final RoleRepository roleRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @PostMapping("/create")
    public ResponseDTO create(@RequestBody final User user) {
        return this.userService.create(user);
    }

    @GetMapping("/retrieve")
    public ResponseDTO retrieve() {
        return this.userService.retrieveAll();
    }

    @GetMapping("/retrieve/{id}")
    public ResponseDTO retrieveById(@PathVariable("id") final String id) {
        return this.userService.retrieveById(id);
    }

    @PutMapping("/update/{id}")
    public ResponseDTO update(@PathVariable("id") final String id, @RequestBody final User updatedUser) {
        return this.userService.update(id, updatedUser);
    }

    @DeleteMapping("/remove/{id}")
    public ResponseDTO delete(@PathVariable("id") final String id) {
        return this.userService.delete(id);
    }

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> register(@RequestBody final RegisterRequestDTO request) {
        ResponseDTO response = userService.register(request);
        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping("/email/{email}")
    public User getUserByEmail(@PathVariable final String email) {
        return userService.getUserWithEmail(email);
    }
}
