package com.learning.employee_data_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import org.mockito.MockitoAnnotations;

import org.mockito.junit.jupiter.MockitoExtension;

import com.learning.employee_data_service.exception.EmployeeNotFoundException;
import com.learning.employee_data_service.model.Employee;
import com.learning.employee_data_service.repository.EmployeeRepository;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {
    @InjectMocks
    // private EmployeeService employeeService = new EmployeeServiceImpl();
    private EmployeeServiceImpl employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    public EmployeeServiceTest() {
        MockitoAnnotations.openMocks(this);
    }

    private Employee employee;

    @BeforeEach
    void init() {
        employee = new Employee();
        employee.setId(1L);
        employee.setName("rkvs");
        employee.setRole("Data Analyst");
    }

    @Test
    @DisplayName("Should save the employee object to database")
    void save() {

        when(employeeRepository.save(any(Employee.class))).thenReturn(employee);

        Employee newEmployee = employeeService.save(employee);

        assertNotNull(newEmployee);
        assertThat(newEmployee.getName()).isEqualTo("rkvs");

    }

    @Test
    @DisplayName("Should return all the employees")
    void getAllEmployees() {

        Employee employee2 = new Employee();
        employee2.setId(2L);
        employee2.setName("rk");
        employee2.setRole("Developer");
        List<Employee> employeeList = new ArrayList<>();
        employeeList.add(employee);
        employeeList.add(employee2);

        when(employeeRepository.findAll()).thenReturn(employeeList);
        List<Employee> newEmployeeList = employeeService.findAll();
        assertNotNull(newEmployeeList);
        assertEquals(2, newEmployeeList.size());

    }

    @Test
    @DisplayName("Should return employee with a particular id")
    void getEmployeeById() {

        when(employeeRepository.findById(anyLong())).thenReturn(Optional.of(employee));

        Employee existingEmployee = employeeService.findById(1L);

        assertNotNull(existingEmployee);
        assertThat(existingEmployee.getId()).isEqualTo(1L);

    }

    @Test
    @DisplayName("Should throw exception when id not found")
    void getEmployeeByIdException() {

        // Mock the repository to return an empty Optional when findById is called with
        // 2L
        when(employeeRepository.findById(2L)).thenReturn(Optional.empty());

        assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.findById(2L);
        });

    }

    @Test
    @DisplayName("Should delete the employee")
    void deleteEmployee() {

        when(employeeRepository.existsById(1L)).thenReturn(true);
        doNothing().when(employeeRepository).deleteById(1L);

        employeeService.deleteById(1L);

        verify(employeeRepository, times(1)).deleteById(1L);

    }

}
