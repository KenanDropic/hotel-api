package com.hotel.repository;

import com.hotel.entity.Employee;
import com.hotel.utils.dto.Employee.SearchEmployeeDto;
import com.hotel.utils.projections.EmployeeView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmployeeRepository extends JpaRepository<Employee, Long> {

    Employee findByEmail(String email);

    @Query(value = """
            SELECT e.employee_id as id,first_name as firstName,last_name as lastName,email,array_agg(r.name) as roles
            FROM employee e
            INNER JOIN employee_roles er ON er.employee_id = e.employee_id
            INNER JOIN roles r on er.role_id = r.id
            WHERE er.employee_id = :employeeId
            GROUP BY e.employee_id
            """, nativeQuery = true)
    EmployeeView findEmployee(@Param("employeeId") Long employeeId);

    @Query(value = "select role1_.name as role_name from employee_roles roles0_ " +
            "inner join roles role1_ on roles0_.role_id = role1_.id where roles0_.employee_id = :employeeId",
            nativeQuery = true)
    List<String> findEmployeeRoles(@Param("employeeId") Long userId);

    @Query(value = """
            SELECT e.employee_id as id,first_name as firstName,last_name as lastName,email,array_agg(r.name) as roles
            FROM employee e
            INNER JOIN employee_roles er ON er.employee_id = e.employee_id
            INNER JOIN roles r on er.role_id = r.id
            WHERE :#{#params.firstName} is null or
            first_name = cast(:#{#params.firstName} as character varying)
            AND
            :#{#params.lastName} is null or
            last_name = cast(:#{#params.lastName} as character varying)
            GROUP BY e.employee_id""", nativeQuery = true)
    Page<EmployeeView> findEmployees(SearchEmployeeDto params, Pageable pageable);
}
