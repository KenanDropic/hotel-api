package com.hotel.security;

import com.hotel.security.filter.JwtValidationFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Collections;
import java.util.List;

import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.Customizer.withDefaults;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
public class SecurityConfig {
    private final JwtValidationFilter jwtValidationFilter;

    public SecurityConfig(JwtValidationFilter jwtValidationFilter) {
        this.jwtValidationFilter = jwtValidationFilter;
    }


    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.antMatcher("/api/v1/**")
                .sessionManagement()
                .sessionCreationPolicy(STATELESS)
                .and()
                .cors()
                .configurationSource(request -> {
                    CorsConfiguration config = new CorsConfiguration();
                    config.setAllowedOrigins(Collections.singletonList("http://localhost:3000"));
                    config.setAllowedMethods(Collections.singletonList("*"));
                    config.setAllowedHeaders(Collections.singletonList("*"));
                    config.setExposedHeaders(List.of("Authorization"));
                    config.setAllowCredentials(true);
                    return config;
                }).and().csrf().disable()
                .logout(logout -> logout
                        .logoutUrl("/api/v1/auth/logout")
                        .invalidateHttpSession(true))
                .authorizeHttpRequests((auth) -> auth
                        .antMatchers(POST, "/api/v1/auth/register", "/api/v1/auth/login").permitAll()
                        .antMatchers(GET, "/api/v1/rooms", "/api/v1/rooms/{id}").permitAll()
                        .antMatchers(GET, "/api/v1/rooms/{id}/reservations").hasAnyRole("EMPLOYEE", "ADMIN")
                        .antMatchers(POST, "/api/v1/rooms").hasRole("ADMIN")
                        .antMatchers("/api/v1/rooms/{id}").hasRole("ADMIN")
                        .antMatchers("/api/v1/employee/**").hasRole("ADMIN")
                        .antMatchers("/api/v1/guests").hasAnyRole("ADMIN", "EMPLOYEE")
                        .antMatchers(DELETE, "/api/v1/guests/{id}").hasRole("ADMIN")
                        .antMatchers("/api/v1/invoices/**").hasAnyRole("ADMIN", "EMPLOYEE")
                )
                .addFilterBefore(jwtValidationFilter, BasicAuthenticationFilter.class)
                .httpBasic(withDefaults());

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}
