package com.learning.employee_data_service.service;

import java.util.List;

import com.learning.employee_data_service.model.Employee;

public interface EmployeeService {

    List<Employee> findAll();

    Employee findById(Long id);

    Employee save(Employee employee);

    void deleteById(Long id);

}
