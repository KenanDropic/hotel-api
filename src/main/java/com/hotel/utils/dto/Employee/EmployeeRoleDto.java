package com.hotel.utils.dto.Employee;

import com.hotel.utils.annotations.ValidateRole;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@EqualsAndHashCode
@NoArgsConstructor
public class EmployeeRoleDto {
    @ValidateRole
    private String role;
}
