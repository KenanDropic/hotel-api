package com.hotel.config;

import com.hotel.entity.Employee;
import com.hotel.entity.SecurityEmployee;
import com.hotel.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class EmployeeDetails implements UserDetailsService {
    private final EmployeeRepository employeeRepository;

    public EmployeeDetails(EmployeeRepository employeeRepository) {
        this.employeeRepository = employeeRepository;
    }

    @Override
    public org.springframework.security.core.userdetails.UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Employee employee = employeeRepository.findByEmail(email);

        if (employee == null) {
            throw new UsernameNotFoundException("Employee details not found for the employee: " + email);
        }
        return new SecurityEmployee(employee);
    }
}
