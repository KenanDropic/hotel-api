package com.hotel.service;

import com.hotel.entity.Employee;
import com.hotel.exception.exceptions.NotFoundExc;
import com.hotel.repository.EmployeeRepository;
import com.hotel.repository.RoleRepository;
import com.hotel.utils.UpdateBodyValidation;
import com.hotel.utils.dto.Employee.UpdateEmployeeDto;
import com.hotel.utils.mapper.EmployeeMapper;
import com.hotel.utils.paginationSorting.Pagination;
import com.hotel.utils.paginationSorting.Sorting;
import com.hotel.utils.dto.Employee.CreateEmployeeDto;
import com.hotel.utils.dto.Employee.SearchEmployeeDto;
import com.hotel.utils.payload.PaginationResponse;
import com.hotel.utils.payload.ResponsePayload;
import com.hotel.utils.projections.EmployeeView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import java.util.*;

@Service
@Transactional
@Slf4j
public class EmployeeService {
    private final EmployeeRepository employeeRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final EmployeeMapper employeeMapper;

    public EmployeeService(EmployeeRepository employeeRepository, PasswordEncoder passwordEncoder,
                           RoleRepository roleRepository, EmployeeMapper employeeMapper) {
        this.employeeRepository = employeeRepository;
        this.passwordEncoder = passwordEncoder;
        this.roleRepository = roleRepository;
        this.employeeMapper = employeeMapper;
    }

    public ResponseEntity<PaginationResponse> findAllEmployeesWithPaginationAndSorting
            (SearchEmployeeDto searchParams,
             int page, int pageSize) {
        Sorting sorting = new Sorting();
        sorting.containsDirection(searchParams.getDirection());
        sorting.containsField(Arrays.asList("first_name", "created_at", "employee_id"), searchParams.getField());

        Sort sort = searchParams.getDirection().equals("ASC") ?
                Sort.by(Objects.requireNonNull(searchParams.getField()).equals("employee_id") ?
                        "employee_id" : Objects.requireNonNull(searchParams.getField()).equals("first_name") ?
                        "firstName" :
                        "createdAt").ascending() :
                Sort.by(Objects.requireNonNull(searchParams.getField()).equals("employee_id") ?
                        "employee_id" : Objects.requireNonNull(searchParams.getField()).equals("first_name") ?
                        "firstName" :
                        "createdAt").descending();

        Pageable paging = PageRequest.of(page - 1, pageSize, sort);

        Page<EmployeeView> employees = this.employeeRepository.findEmployees(searchParams, paging);

        if (employees.isEmpty()) {
            return ResponseEntity
                    .status(404)
                    .body(new PaginationResponse(true, 0,
                            employees.getTotalPages(), page,
                            employees.getContent()));
        }

        Pagination pagination = new Pagination();
        pagination.doesHaveNext(employees, page);


        return ResponseEntity
                .status(200)
                .body(new PaginationResponse(true, employees.getSize(),
                        employees.getTotalElements(), employees.getTotalPages(),
                        page, pagination.getPagination(), employees.getContent()));
    }

    public ResponseEntity<ResponsePayload<EmployeeView>> findEmployee(Long id) {
        EmployeeView employee = this.employeeRepository
                .findEmployee(id);

        if (employee == null) {
            return ResponseEntity
                    .status(404)
                    .body(new ResponsePayload<>(false, "Employee with ID: " + id + " not found"));
        }

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(true, employee));
    }

    public ResponseEntity<ResponsePayload<List<String>>> findEmployeeRoles(Long id) {
        Employee employee = this.employeeRepository
                .findById(id)
                .orElseThrow(() -> new NotFoundExc("Employee not found with the given ID!"));

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(
                        true,
                        this.employeeRepository.findEmployeeRoles(employee.getId())));
    }

    public ResponseEntity<ResponsePayload<Employee>> create(CreateEmployeeDto params) {
        Employee employee = new Employee(params.getFirstName(),
                params.getLastName(),
                params.getEmail(),
                passwordEncoder.encode(params.getPassword()));

        employee.addRole(this.roleRepository.findByName("ROLE_EMPLOYEE"));

        return ResponseEntity
                .status(201)
                .body(new ResponsePayload<>(
                        true,
                        "Employee created successfully",
                        this.employeeRepository.save(employee)));
    }

    public ResponseEntity<ResponsePayload<Employee>> updateEmployee
            (Long employeeId, UpdateEmployeeDto params) {
        Employee employee = this.employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new NotFoundExc("Employee " + employeeId + " not found"));

        List<String> existingValues = Arrays.asList(employee.getFirstName(), employee.getLastName(), employee.getEmail());
        List<Object> passedValues = Arrays.asList(params.getFirstName(), params.getLastName(), params.getEmail());

        UpdateBodyValidation<String> check = new UpdateBodyValidation<>();
        check.checkRequestBody(existingValues, passedValues);

        employeeMapper.updateEntityFromDto(params, employee);
        this.employeeRepository.save(employee);

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(
                        true,
                        "Employee updated successfully",
                        employee));

    }

    public ResponseEntity<ResponsePayload<ResponseBody>> deleteEmployee(Long employeeId) {
        Employee employee = this.employeeRepository
                .findById(employeeId)
                .orElseThrow(() -> new NotFoundExc("Employee " + employeeId + " not found"));

        this.employeeRepository.delete(employee);

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(
                        true,
                        "Employee " + employeeId + " deleted successfully"));
    }

}
