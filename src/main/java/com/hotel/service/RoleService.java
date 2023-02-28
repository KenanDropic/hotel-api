package com.hotel.service;

import com.hotel.entity.Employee;
import com.hotel.entity.Role;
import com.hotel.exception.exceptions.NotFoundExc;
import com.hotel.repository.EmployeeRepository;
import com.hotel.repository.RoleRepository;
import com.hotel.utils.dto.Employee.EmployeeRoleDto;
import com.hotel.utils.payload.ResponsePayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class RoleService {
    private final RoleRepository roleRepository;
    private final EmployeeRepository employeeRepository;

    public RoleService(RoleRepository roleRepository,
                       EmployeeRepository employeeRepository) {
        this.roleRepository = roleRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Role> findEmployeeRoles(Long userId) {
        Employee employee = this.employeeRepository
                .findById(userId)
                .orElseThrow(() -> new NotFoundExc("Employee not found!"));
        return this.roleRepository.findByEmployees(employee);
    }

    public ResponseEntity<ResponsePayload<List<Role>>> updateEmployeeRoles(Long id, EmployeeRoleDto role, String updateType) {
        Employee employee = this.employeeRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundExc("Employee not found!"));


        if (!role.getRole().equals("ROLE_EMPLOYEE")) {
            if (Objects.equals(updateType, "remove")) {
                // to do - remove role from employee
            }
            employee.addRole(this.roleRepository.findByName(role.getRole()));
        } else {
            return ResponseEntity
                    .status(400)
                    .body(new ResponsePayload<>(false, "This user already has role employee"));
        }

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(
                        true,
                        "Update is successful",
                        this.roleRepository.findByEmployees(employee)));
    }
}
