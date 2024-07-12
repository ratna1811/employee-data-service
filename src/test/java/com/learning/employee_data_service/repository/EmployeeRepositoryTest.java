package com.learning.employee_data_service.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.ActiveProfiles;

import com.learning.employee_data_service.model.Employee;

@DataJpaTest
@ActiveProfiles("test")
@ComponentScan(basePackages = "com.learning.employee_data_service")
public class EmployeeRepositoryTest {
    @Autowired
    private EmployeeRepository employeeRepository;

    private Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee();
        employee.setName("ratna");
        employee.setRole("developer");
        employeeRepository.save(employee);

    }

    @Test
    void testFindById() {
        Optional<Employee> foundEmployee = employeeRepository.findById(employee.getId());
        assertThat(foundEmployee).isPresent();
        assertThat(foundEmployee.get().getName()).isEqualTo(employee.getName());

    }

    @Test
    void testFindAll() {
        Employee emp1 = new Employee();
        emp1.setName("rp");
        emp1.setRole("tester");
        employeeRepository.save(emp1);
        List<Employee> employees = Arrays.asList(employee, emp1);
        List<Employee> foundEmployees = employeeRepository.findAll();
        assertThat(employees.size()).isEqualTo(foundEmployees.size());
    }

    @Test
    void testFindAll_OtherWay() {
        Iterable<Employee> employees = employeeRepository.findAll();
        assertThat(employees).hasSize(1).contains(employee);
    }

    @Test
    void testSave() {
        Employee newEmployee = new Employee();
        newEmployee.setName("John");
        newEmployee.setRole("manager");
        Employee savedEmployee = employeeRepository.save(newEmployee);
        assertThat(savedEmployee).isNotNull();
        assertThat(savedEmployee.getName()).isEqualTo(newEmployee.getName());
        assertThat(savedEmployee.getRole()).isEqualTo(newEmployee.getRole());
    }

    // @Test
    // void testUpdate() {
    // employee.setName("karthi");
    // employee.setRole("TL");
    // Employee updatedEmployee = employeeRepository.save(employee);
    // assertThat(updatedEmployee).isNotNull();
    // assertThat(updatedEmployee.getName()).isEqualTo(employee.getName());
    // // assertThat(updatedEmployee.getName()).isEqualTo("karthi");
    // assertThat(updatedEmployee.getRole()).isEqualTo(employee.getRole());
    // }
    @Test
    void testDeleteById() {
        // Optional<Employee> existingEmployee =
        // employeeRepository.findById(employee.getId());
        // employeeRepository.deleteById(existingEmployee.get().getId());
        // assertThat(existingEmployee.isEmpty());
        employeeRepository.deleteById(employee.getId());
        Optional<Employee> deletedEmployee = employeeRepository.findById(employee.getId());
        assertThat(deletedEmployee).isNotPresent();
    }

    @Test
    void testExistsById() {
        boolean exists = employeeRepository.existsById(employee.getId());
        assertThat(exists).isTrue();
    }

}
