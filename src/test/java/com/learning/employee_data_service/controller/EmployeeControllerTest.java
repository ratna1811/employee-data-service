package com.learning.employee_data_service.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.learning.employee_data_service.exception.EmployeeNotFoundException;
import com.learning.employee_data_service.model.Employee;
import com.learning.employee_data_service.service.EmployeeService;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import org.springframework.http.MediaType;

@WebMvcTest(EmployeeController.class)
public class EmployeeControllerTest {

    @MockBean
    private EmployeeService employeeService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    Employee employee;

    @BeforeEach
    void setUp() {
        employee = new Employee(1L, "rk", "developer");

    }

    @Test
    void testGetAllEmployee() throws Exception {
        List<Employee> employees = Arrays.asList(employee, new Employee(2L, "rp", "tester"));
        when(employeeService.findAll()).thenReturn(employees);

        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$.[0].name").value("rk"))
                .andExpect(jsonPath("$.[1].role").value("tester"));

        verify(employeeService, times(1)).findAll();

    }

    @Test
    void testGetEmployeeById() throws Exception {

        when(employeeService.findById(1L)).thenReturn(employee);

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("rk"))
                .andExpect(jsonPath("$.role").value("developer"));

        verify(employeeService, times(1)).findById(1L);

    }

    @Test
    void getEmployeeById_nonExistingId() throws Exception {
        when(employeeService.findById(1L)).thenThrow(new EmployeeNotFoundException("Employee not found with id: 1"));

        mockMvc.perform(get("/employees/1"))
                .andExpect(status().isNotFound());
        verify(employeeService, times(1)).findById(1L);

    }

    @Test
    void createEmployee() throws Exception {
        when(employeeService.save(any(Employee.class))).thenReturn(employee);

        mockMvc.perform(post("/employees")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(employee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("rk"))
                .andExpect(jsonPath("$.role").value("developer"));
        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    void updateEmployee() throws Exception {
        Employee updatedEmployee = new Employee(1L, "John", "Architect");
        when(employeeService.findById(1L)).thenReturn(employee);
        when(employeeService.save(any(Employee.class))).thenReturn(updatedEmployee);

        mockMvc.perform(put("/employees/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedEmployee)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John"))
                .andExpect(jsonPath("$.role").value("Architect"));
        verify(employeeService, times(1)).findById(1L);
        verify(employeeService, times(1)).save(any(Employee.class));
    }

    @Test
    void deleteEmployee_existingId() throws Exception {
        doNothing().when(employeeService).deleteById(1L);
        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isNoContent());
        verify(employeeService, times(1)).deleteById(1L);
    }

    @Test
    void deleteEmployee_nonExistingId() throws Exception {
        doThrow(new EmployeeNotFoundException("Employee not found with id: 1")).when(employeeService).deleteById(1L);
        mockMvc.perform(delete("/employees/1"))
                .andExpect(status().isNotFound());

        verify(employeeService, times(1)).deleteById(1L);
    }
}
