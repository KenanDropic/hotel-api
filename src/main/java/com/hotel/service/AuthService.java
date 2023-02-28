package com.hotel.service;

import com.hotel.entity.Employee;
import com.hotel.exception.exceptions.BadRequestExc;
import com.hotel.repository.EmployeeRepository;
import com.hotel.security.UsernamePasswordAuthentication;
import com.hotel.security.UsernamePwdAuthenticateProvider;
import com.hotel.security.jwt.GenerateT;
import com.hotel.utils.dto.Employee.CreateEmployeeDto;
import com.hotel.utils.dto.Employee.EmployeeDto;
import com.hotel.utils.dto.Auth.LoginDto;
import com.hotel.utils.payload.ResponsePayload;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Objects;

@Service
@Transactional
@Slf4j
public class AuthService {
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;
    private final GenerateT generateToken;
    private final UsernamePwdAuthenticateProvider usernamePwdAuthenticateProvider;

    public AuthService(EmployeeRepository employeeRepository, EmployeeService employeeService,
                       GenerateT generateToken,
                       UsernamePwdAuthenticateProvider usernamePwdAuthenticateProvider) {
        this.employeeRepository = employeeRepository;
        this.employeeService = employeeService;
        this.generateToken = generateToken;
        this.usernamePwdAuthenticateProvider = usernamePwdAuthenticateProvider;
    }

    public ResponseEntity<ResponsePayload<ResponseBody>> signup(CreateEmployeeDto params) {
        if (emailExists(params.getEmail())) throw new BadRequestExc("Employee already exists");

        Employee employee = Objects.requireNonNull(this.employeeService.create(params).getBody()).getData();

        String token = generateToken.generateToken(employee);

        return ResponseEntity
                .status(201)
                .body(new ResponsePayload<>(
                        true,
                        "You've registered  successfully.",
                        token));
    }

    public ResponseEntity<ResponsePayload<ResponseBody>> signin(LoginDto params) {
        Employee employee = this.employeeRepository.findByEmail(params.getEmail());

        if (employee == null) {
            return ResponseEntity
                    .status(404)
                    .body(new ResponsePayload<>(
                            true,
                            "Employee not found with the given email!"));
        }


        //noinspection unused
        Authentication auth = usernamePwdAuthenticateProvider
                .authenticate(new UsernamePasswordAuthentication(params.getEmail(), params.getPassword()));

        String token = generateToken.generateToken(employee);

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(true, "You've logged in successfully", token));
    }

    public ResponseEntity<ResponsePayload<EmployeeDto>> getLoggedUser() {
        Authentication auth = getAuthContext();

        if ((auth instanceof AnonymousAuthenticationToken)) {
            return ResponseEntity
                    .status(401)
                    .body(new ResponsePayload<>(true, "Failed to authenticate user"));
        }

        Employee employee = this.employeeRepository.findByEmail(auth.getPrincipal().toString());
        List<String> employeeRoles = Objects.requireNonNull(this.employeeService.findEmployeeRoles(employee.getId()).getBody()).getData();
        EmployeeDto employeeDto = new EmployeeDto();
        employeeDto.setId(employee.getId());
        employeeDto.setFirstName(employee.getFirstName());
        employeeDto.setLastName(employee.getLastName());
        employeeDto.setEmail(employee.getEmail());
        employeeDto.setRoles(employeeRoles);

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(true, employeeDto));
    }

    public ResponseEntity<ResponsePayload<ResponseBody>> logoutUser() {
        Authentication auth = getAuthContext();

        if ((auth instanceof AnonymousAuthenticationToken))
            return ResponseEntity
                    .status(401)
                    .body(new ResponsePayload<>(true, "Authentication failed!"));
        ClearAuth();

        return ResponseEntity
                .status(200)
                .body(new ResponsePayload<>(true, "Logout is successful"));
    }

    /*------------------------------- NON-API -----------------------------------*/

    @NotNull
    public Authentication getAuthContext() {
        SecurityContext context = SecurityContextHolder.getContext();
        return context.getAuthentication();
    }

    private boolean emailExists(final String email) {
        return employeeRepository.findByEmail(email) != null;
    }

    public static void ClearAuth() {
        SecurityContextHolder.clearContext();
    }
}
