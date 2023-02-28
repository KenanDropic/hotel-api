package com.hotel.controller;

import com.hotel.service.AuthService;
import com.hotel.utils.dto.Employee.CreateEmployeeDto;
import com.hotel.utils.dto.Auth.LoginDto;
import com.hotel.utils.dto.Employee.EmployeeDto;
import com.hotel.utils.payload.ResponsePayload;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<ResponsePayload<ResponseBody>> register(@RequestBody @Valid CreateEmployeeDto params) {
        return this.authService.signup(params);
    }

    @PostMapping("/login")
    public ResponseEntity<ResponsePayload<ResponseBody>> login(@RequestBody @Valid LoginDto params) {
        return this.authService.signin(params);
    }

    @GetMapping("/me")
    public ResponseEntity<ResponsePayload<EmployeeDto>> getCurrentUser() {
        return this.authService.getLoggedUser();
    }

    @PostMapping("/logout")
    public ResponseEntity<ResponsePayload<ResponseBody>> logout() {
        return this.authService.logoutUser();
    }

}
