package com.hotel.utils.dto.Employee;

import com.hotel.utils.dto.Auth.LoginDto;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@NoArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class CreateEmployeeDto extends LoginDto {
    @NotNull(message = "Field cannot be null")
    @NotEmpty(message = "Field cannot be empty")
    @Length(max = 12,min = 3)
    private String firstName;

    @NotNull(message = "Field cannot be null")
    @NotEmpty(message = "Field cannot be empty")
    @Length(max = 14,min = 4)
    private String lastName;

    @Pattern(regexp =  "^[+][0-9]{3}[\s-.]?6[1-5][\s-.]?[0-9]{3}[\s-.]?[0-9]{3,4}$",
            message = "Allowed number format examples: 1) +38761653789 2) +386 62 653 789 3) +384.64.653.7891 4) +387-63-653-789")
    private String phone;
}
