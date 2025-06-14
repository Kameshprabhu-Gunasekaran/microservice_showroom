package com.userService.config;

import com.userService.security.AuthEntryPoint;
import com.userService.security.JwtAuthenticationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

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
                        .requestMatchers("/api/v1/roles/**").permitAll()

                        .requestMatchers("/api/v1/users/**").permitAll()
                        .requestMatchers("/api/v1/users/create").permitAll()
                        .requestMatchers("/api/v1/roles/create").permitAll()
                        .requestMatchers("/api/v1/customers/create").permitAll()
                        .requestMatchers("/api/v1/feedbacks/create").permitAll()
                        .requestMatchers("/api/v1/salesmanager/create").permitAll()
                        .requestMatchers("/api/v1/salesman/create").permitAll()
                        .requestMatchers("/api/v1/salesmanager/**").permitAll()
                        .requestMatchers("/api/v1/salesman/**").permitAll()
                        .requestMatchers("/api/v1/feedbacks/**").permitAll()
                        .requestMatchers("/api/v1/customers/**").permitAll()
                        .requestMatchers("/api/v1/user/view/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER")
                        .requestMatchers("/api/v1/users/email/**")
                        .hasAnyAuthority("ROLE_ADMIN", "ROLE_MANAGER")

                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
