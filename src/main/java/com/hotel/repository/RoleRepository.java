package com.hotel.repository;

import com.hotel.entity.Employee;
import com.hotel.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import javax.validation.constraints.NotNull;
import java.util.List;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    Role findByName(String name);

    @SuppressWarnings("SpringDataRepositoryMethodParametersInspection")
    List<Role> findByEmployees(Employee employee);

    @Override
    void delete(@SuppressWarnings("NullableProblems") @NotNull Role role);
}
