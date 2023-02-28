package com.hotel.utils.mapper;

import com.hotel.entity.Employee;
import com.hotel.utils.dto.Employee.UpdateEmployeeDto;
import org.mapstruct.*;

import static org.mapstruct.NullValuePropertyMappingStrategy.IGNORE;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    @BeanMapping(nullValuePropertyMappingStrategy = IGNORE)
    void updateEntityFromDto(UpdateEmployeeDto dto, @MappingTarget Employee employee);
}
