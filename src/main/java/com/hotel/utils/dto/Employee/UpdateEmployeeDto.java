package com.hotel.utils.dto.Employee;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.Nullable;


@Data
@AllArgsConstructor
public class UpdateEmployeeDto {
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;
    @Nullable
    private String email;
}
