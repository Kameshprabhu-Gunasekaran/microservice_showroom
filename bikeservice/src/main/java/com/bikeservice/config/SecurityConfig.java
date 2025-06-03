package com.bikeservice.config;

import com.bikeservice.security.AuthEntryPoint;
import com.bikeservice.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    private final AuthEntryPoint authEntryPoint;
    private final JwtAuthenticationFilter jwtAuthFilter;

    public SecurityConfig(AuthEntryPoint authEntryPoint, JwtAuthenticationFilter jwtAuthFilter) {
        this.authEntryPoint = authEntryPoint;
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authEntryPoint))
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/bike/create").permitAll()
                        .requestMatchers("/api/v1/showroom/create").permitAll()
                        .requestMatchers("/api/v1/sale/create").permitAll()
                        .requestMatchers("/api/v1/testride/create").permitAll()
                        .requestMatchers("/api/v1/bike/**").permitAll()
                        .requestMatchers("/api/v1/sale/**").permitAll()
                        .requestMatchers("/api/v1/showroom/**").permitAll()
                        .requestMatchers("/api/v1/testride/**").permitAll()
                        .requestMatchers("/api/v1/bike/update/**", "/api/v1/bike/remove/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_SALESMANAGER")
                        .requestMatchers("/api/v1/bike/retrieve/**")
                        .hasAnyAuthority("ROLE_CUSTOMER", "ROLE_SALESMAN", "ROLE_ADMIN", "ROLE_SALESMANAGER")
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
