package com.hotel.entity;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.Serial;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;

public class SecurityEmployee implements UserDetails {
    @Serial
    private static final long serialVersionUID = -6690946490872875352L;
    private final Employee employee;

    public SecurityEmployee(Employee employee) {
        super();
        this.employee = employee;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (Role role : this.employee.getRoles()) {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        }
        return authorities;
    }

    public Set<Role> getRoles() {
        return this.employee.getRoles();
    }

    @SuppressWarnings("unused")
    public void setRoles(Set<Role> roles) {
        this.employee.setRoles(roles);
    }

    @SuppressWarnings("unused")
    public void addRole(Role role) {
        this.getRoles().add(role);
    }

    @Override
    public String getPassword() {
        return employee.getPassword();
    }

    @Override
    public String getUsername() {
        return employee.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}


