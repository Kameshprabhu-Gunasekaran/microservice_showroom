package com.authservice.service;

import com.authservice.dto.ResponseDTO;
import com.authservice.util.Constant;
import com.common.entity.User;
import io.jsonwebtoken.Claims;
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
import java.util.function.Function;

@Service
public class JwtService {

    @Autowired
    private WebClient webClient;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private static final String SECRET_KEY = "83a29bc2-a865-4b70-a645-4c7f83dd6282";
    private static final long EXPIRATION_TIME = 3600000;

    @Autowired
    public JwtService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("http://localhost:8082").build();
    }

    public ResponseDTO generateToken(final String email, final String password) {
        final User user = getUserByEmail(email);

        if (user == null) {
            return new ResponseDTO("NOT FOUND", null, "User not found");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            return new ResponseDTO("BAD REQUEST", null, "INVALID PASSWORD");
        }

        String role = fetchUserRole(user.getId());

            Map<String, Object> claims = new HashMap<>();
        claims.put("role", role);

        String token = createToken(claims, email);

        return new ResponseDTO(Constant.CREATE, token, HttpStatus.OK.getReasonPhrase());
    }

    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SignatureAlgorithm.HS256, getSignInKey())
                .compact();
    }

    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser()
                .setSigningKey(getSignInKey())
                .parseClaimsJws(token)
                .getBody();
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    public boolean isTokenValid(String token, String userEmail) {
        final String username = extractUsername(token);
        return (username.equals(userEmail) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        final Date expiration = extractClaim(token, Claims::getExpiration);
        return expiration.before(new Date());
    }

    private User getUserByEmail(String email) {
        try {

            User existingUser =
             webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/v1/users/email/{email}").build(email))
                    .retrieve()
                    .bodyToMono(User.class)
                    .block();
            return existingUser;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String fetchUserRole(String userId) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder.path("/api/v1/roles/user/{id}").build(userId))
                    .retrieve()
                    .bodyToMono(String.class)
                    .block();
        } catch (Exception e) {
            e.printStackTrace();
            return "ROLE_USER";
        }
    }
}
