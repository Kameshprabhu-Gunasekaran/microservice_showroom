package com.authservice.service;

import com.authservice.dto.ResponseDTO;
import com.authservice.util.Constant;
import com.common.entity.Role;
import com.common.entity.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {

    @Value("${jwt.secret-key}")
    private String secretKey;

    @Autowired
    private WebClient webClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public ResponseDTO generateToken(final String email, final String rawPassword) {
        try {
            final User user = getUserByEmail(email);

            final Role role = fetchUserRole(user.getId());
            if (role == null) {
                return new ResponseDTO("User role not found", null, HttpStatus.NOT_FOUND.getReasonPhrase());
            }

            final Map<String, Object> claims = new HashMap<>();
            claims.put("role", role.getRole());

            final String token = createToken(claims, user.getEmail());

            return new ResponseDTO(Constant.CREATE, token, HttpStatus.OK.getReasonPhrase());

        } catch (Exception e) {
            e.printStackTrace();
            return new ResponseDTO("Token generation failed", null, HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase());
        }
    }

    private String createToken(final Map<String, Object> claims, final String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis()+ 1000 * 60 * 30))
                .signWith(SignatureAlgorithm.HS256,getSignInKey()).compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private User getUserByEmail(final String email) {
        return webClient.get()
                .uri("http://localhost:8082/api/v1/users/email/{email}", email)
                .retrieve()
                .bodyToMono(User.class)
                .block();
    }

    private Role fetchUserRole(final String userId) {
        return webClient.get()
                .uri("http://localhost:8082/api/v1/roles/user/{id}", userId)
                .retrieve()
                .bodyToMono(Role.class)
                .block();
    }
}
