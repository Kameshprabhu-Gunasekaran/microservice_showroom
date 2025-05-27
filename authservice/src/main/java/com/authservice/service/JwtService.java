package com.authservice.service;

import com.authservice.dto.ResponseDTO;
import com.authservice.repository.RoleRepository;
import com.authservice.repository.UserRepository;
import com.authservice.util.Constant;
import com.common.entity.Role;
import com.common.entity.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import static java.security.KeyRep.Type.SECRET;

@Service
public class JwtService {

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public RoleRepository roleRepository;

    private static final String SECRET_KEY = "8482B4D62516555367566B59703373367639792F423F452468576D5A71347437";
    private static final long EXPIRATION_TIME = 3600000;

    private Key getSignInKey() {
        return Keys.hmacShaKeyFor(SECRET_KEY.getBytes());
    }

    public ResponseDTO generateToken(final String email) {
       final Optional<User> user = this.userRepository.findByEmail(email);
        final Map<String, Object> claims = new HashMap<>();
        String userId = "";
        if (user.isPresent()) {
            final User userdto = user.get();
            userId = userdto.getId();
        }
        final Role roles = this.roleRepository.findRoleByUserId(userId);
        claims.put("role", roles.getRole());
        return new ResponseDTO(Constant.CREATE, createToken(claims, email), HttpStatus.OK.getReasonPhrase());
    }

    private String createToken(Map<String, Object> claims, String email) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 30))
                .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
    }

    private Key getSignKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = Jwts.parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claimsResolver.apply(claims);
    }
}
