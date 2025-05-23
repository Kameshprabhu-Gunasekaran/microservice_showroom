package com.authservice.service;

import com.authservice.dto.ResponseDTO;
import com.authservice.dto.UserSignInRequestDTO;
import com.authservice.exception.BadRequestServiceException;
import com.authservice.repository.UserRepository;
import com.common.entity.User;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, JwtService jwtService, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseDTO login(UserSignInRequestDTO request) {
        User user = userRepository.findByEmail(request.getUsername())
                .orElseThrow(() -> new BadRequestServiceException("Invalid credentials"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BadRequestServiceException("Invalid credentials");
        }

        String role = user.getRole().getRole().name();
        String token = jwtService.generateToken(user.getEmail(), role);

        return new ResponseDTO("LOGIN_SUCCESS", token, "200");
    }
}

