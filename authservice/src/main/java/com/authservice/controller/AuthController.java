package com.authservice.controller;

import com.authservice.dto.ResponseDTO;
import com.authservice.dto.UserSignInRequestDTO;
import com.authservice.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody UserSignInRequestDTO request) {
        ResponseDTO response = authService.login(request);
        return ResponseEntity.ok(response);
    }
}
