package com.hotel.config;

import com.hotel.entity.Employee;
import com.hotel.entity.Role;
import com.hotel.repository.EmployeeRepository;
import com.hotel.repository.RoleRepository;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;

@Component
public class DataLoader implements ApplicationListener<ContextRefreshedEvent> {
    private boolean alreadySetup = false;

    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;

    public DataLoader(RoleRepository roleRepository, EmployeeRepository employeeRepository,
                      PasswordEncoder passwordEncoder) {
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onApplicationEvent(final @NotNull ContextRefreshedEvent event) {
        if (alreadySetup) {
            return;
        }

        // final Role adminRole = createRoleIfNotFound("ROLE_ADMIN");
        // final Role employeeRole = createRoleIfNotFound("ROLE_EMPLOYEE");
        // final Role guestRole = createRoleIfNotFound("ROLE_GUEST");

        List<Employee> employees = this.employeeRepository.findAll();

        if (employees.size() > 0) {
            employees.forEach((employee -> {
                if (employee.getPassword().startsWith("$2a$10$")) {
                    employee.setPassword(passwordEncoder.encode(employee.getPassword()));
                    this.employeeRepository.save(employee);
                }
            }));
        }


        alreadySetup = true;
    }

    @Transactional
    Role createRoleIfNotFound(final String name) {
        Role role = roleRepository.findByName(name);
        if (role == null) {
            role = new Role(name);
        }
        role = roleRepository.save(role);
        return role;
    }

}
