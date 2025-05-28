package com.authservice.controller;

import com.authservice.dto.ResponseDTO;
import com.authservice.dto.UserSignInRequestDTO;
import com.authservice.service.JwtService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {

    @Autowired
    public JwtService jwtService;

    @PostMapping("/login")
    public ResponseEntity<ResponseDTO> login(@RequestBody UserSignInRequestDTO request) {
        ResponseDTO response = jwtService.generateToken(request.getEmail());
        return ResponseEntity.ok(response);
    }
}
