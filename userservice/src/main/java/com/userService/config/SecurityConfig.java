package com.userService.config;

import com.userService.security.AuthEntryPoint;
import com.userService.security.JwtAuthenticationFilter;
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
                        .requestMatchers("/api/v1/auth/**").permitAll()

                        .requestMatchers("/api/v1/user/create", "/api/v1/user/update/**", "/api/v1/user/delete/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")

                        .requestMatchers("/api/v1/user/view/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
