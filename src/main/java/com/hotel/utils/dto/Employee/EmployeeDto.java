package com.hotel.utils.dto.Employee;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@EqualsAndHashCode
public class EmployeeDto {
    public EmployeeDto(Long id, String firstName, String lastName,
                       String email, List<String> roles) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.roles = roles;
    }

    private Long id;
    private String firstName;
    private String lastName;

    private String email;
    private List<String> roles;
    //private ArrayList<String> roles;
}
