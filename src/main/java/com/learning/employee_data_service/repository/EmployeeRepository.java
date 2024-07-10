package com.learning.employee_data_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.learning.employee_data_service.model.Employee;

public interface EmployeeRepository extends JpaRepository<Employee, Long> {

}
