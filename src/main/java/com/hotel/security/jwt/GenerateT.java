package com.hotel.security.jwt;

import com.hotel.entity.Employee;
import com.hotel.entity.SecurityEmployee;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

@Component
public class GenerateT {
    @Value("${jwt.s.secret}")
    private String TSecret;

    public String generateToken(Employee employee) {
        SecurityEmployee securityUser = new SecurityEmployee(employee);
        System.out.println("Generated authorities:" + securityUser.getAuthorities());

        // expires in 10 days
        return Jwts.builder()
                .setIssuer("host:8080")
                .setSubject("JWT")
                .claim("username", securityUser.getUsername())
                .claim("authorities", populateAuthorities((securityUser.getAuthorities())))
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + 10 * 24 * 60 * 60 * 1000))
                .signWith(SignatureAlgorithm.HS512, TSecret)
                .compact();

    }

    private String populateAuthorities(Collection<? extends GrantedAuthority> collection) {
        List<String> authoritiesList = new ArrayList<>();
        for (GrantedAuthority authority : collection) {
            authoritiesList.add(authority.getAuthority());
        }
        return String.join(",", authoritiesList);
    }
}
