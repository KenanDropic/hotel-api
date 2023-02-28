package com.hotel.controller;

import com.hotel.entity.Employee;
import com.hotel.entity.Role;
import com.hotel.service.EmployeeService;
import com.hotel.service.RoleService;
import com.hotel.utils.dto.Employee.CreateEmployeeDto;
import com.hotel.utils.dto.Employee.EmployeeRoleDto;
import com.hotel.utils.dto.Employee.SearchEmployeeDto;
import com.hotel.utils.dto.Employee.UpdateEmployeeDto;
import com.hotel.utils.payload.PaginationResponse;
import com.hotel.utils.payload.ResponsePayload;
import com.hotel.utils.projections.EmployeeView;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/v1/employees")
public class EmployeeController {
    private final EmployeeService employeeService;
    private final RoleService roleService;

    public EmployeeController(EmployeeService employeeService,
                              RoleService roleService) {
        this.employeeService = employeeService;
        this.roleService = roleService;
    }

    @GetMapping
    public ResponseEntity<PaginationResponse> getEmployees
            (@Valid SearchEmployeeDto searchParams,
             @RequestParam(required = false, defaultValue = "1") Integer page,
             @RequestParam(required = false, defaultValue = "5") Integer pageSize) {
        return this.employeeService.findAllEmployeesWithPaginationAndSorting(searchParams, page, pageSize);
    }

    @GetMapping(path = "/{id}")
    public ResponseEntity<ResponsePayload<EmployeeView>> getEmployee
            (@PathVariable("id") final Long userId) {
        return this.employeeService.findEmployee(userId);
    }

    @PostMapping
    public ResponseEntity<ResponsePayload<Employee>> createEmployee
            (@RequestBody @Valid CreateEmployeeDto params) {
        return this.employeeService.create(params);
    }

    @PutMapping(path = "/{id}")
    public ResponseEntity<ResponsePayload<Employee>> updateEmployee
            (@PathVariable("id") final Long employeeId,
             @RequestBody @Valid UpdateEmployeeDto params) {
        return this.employeeService.updateEmployee(employeeId, params);
    }

    @DeleteMapping(path = "/{id}")
    public ResponseEntity<ResponsePayload<ResponseBody>> deleteEmployee
            (@PathVariable("id") final Long employeeId) {
        return this.employeeService.deleteEmployee(employeeId);
    }

    @PutMapping("/{id}/roles")
    public ResponseEntity<ResponsePayload<List<Role>>> updateEmployeeRoles
            (@PathVariable("id") Long userId,
             @RequestBody @Valid EmployeeRoleDto role,
             String updateType) {
        return this.roleService.updateEmployeeRoles(userId, role, "update");
    }

    @GetMapping("/{id}/roles")
    public ResponseEntity<ResponsePayload<List<String>>> getEmployeesRoles
            (@PathVariable("id") Long userId) {
        return this.employeeService.findEmployeeRoles(userId);
    }
}
