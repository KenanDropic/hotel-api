package com.hotel.security;


import com.hotel.entity.Employee;
import com.hotel.entity.Role;
import com.hotel.repository.EmployeeRepository;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class UsernamePwdAuthenticateProvider implements AuthenticationProvider {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public UsernamePwdAuthenticateProvider(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        // we will pass email into username field,so .getName() will return email not username;
        String username = authentication.getName();
        String pwd = authentication.getCredentials().toString();
        System.out.println("Email and pwd: " + username + pwd);
        Employee employee = employeeRepository.findByEmail(username);
        if (employee != null) {
            if (passwordEncoder.matches(pwd, employee.getPassword())) {
                return new UsernamePasswordAuthenticationToken(username, pwd, getGrantedAuthorities(employee.getRoles()));
            } else {
                throw new BadCredentialsException("Invalid credentials!");
            }
        } else {
            throw new BadCredentialsException("No user registered with this details!");
        }
    }

    private List<GrantedAuthority> getGrantedAuthorities(Collection<Role> roles) {
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        for (Role role : roles) {
            grantedAuthorities.add(new SimpleGrantedAuthority( role.getName()));
        }
        return grantedAuthorities;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}


