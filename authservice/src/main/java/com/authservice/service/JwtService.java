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

    @Autowired
    private WebClient webClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public static final String SECRET_KEY = "8c0bdcb872765826aeaa34f73b32686709b04486e22fc5d0f729d8b8913c3774";

//    @Autowired
//    public JwtService(WebClient.Builder webClientBuilder) {
//        this.webClient = webClientBuilder.baseUrl("http://localhost:8082").build();
//    }

    public ResponseDTO generateToken(final String email, final String password) {
        final User user = this.getUserByEmail(email);
        //final String encrypt = passwordEncoder.encode(password);

        final Role role = fetchUserRole(user.getId());

            Map<String, Object> claims = new HashMap<>();
        claims.put("role", role.getRole());

        String token = this.createToken(claims, email);

        return new ResponseDTO(Constant.CREATE, token, HttpStatus.OK.getReasonPhrase());
    }

    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 1000*60*30))
                .signWith(SignatureAlgorithm.HS256, getSignInKey())
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        System.out.println("Key length in bytes: " + keyBytes.length);
        return Keys.hmacShaKeyFor(keyBytes);
    }


    private User getUserByEmail(String email) {
        try {

           final  User existingUser = webClient.get()
                    .uri("http://localhost:8082/api/v1/users/email/{email}",email)
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            return existingUser;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private Role fetchUserRole(String userId) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/v1/roles/user/{id}").build(userId))
                    .retrieve()
                    .bodyToMono(Role.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
