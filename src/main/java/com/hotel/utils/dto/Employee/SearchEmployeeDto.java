package com.hotel.utils.dto.Employee;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.lang.Nullable;

@Data
@NoArgsConstructor
public class SearchEmployeeDto  {
    @Nullable
    private String firstName;
    @Nullable
    private String lastName;

    private String field = "employee_id";
    private String direction = "ASC";
}
