package com.learning.employee_data_service.integration;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.web.client.RestTemplate;

import com.learning.employee_data_service.model.Employee;
import com.learning.employee_data_service.repository.EmployeeRepository;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class EmployeeIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    private EmployeeRepository employeeRepository;

    private String baseUrl = "http://localhost";

    private static RestTemplate restTemplate;
    private Employee employee1;
    private Employee employee2;

    @BeforeAll
    public static void init() {
        restTemplate = new RestTemplate();
    }

    @BeforeEach
    public void beforeSetup() {
        baseUrl = baseUrl + ":" + port + "/employees";
        employee1 = new Employee();
        employee1.setName("test1");
        employee1.setRole("tester1");

        employee2 = new Employee();
        employee2.setName("test2");
        employee2.setRole("tester2");
        employee1 = employeeRepository.save(employee1);
        employee2 = employeeRepository.save(employee2);

    }

    @AfterEach
    public void afterAll() {
        employeeRepository.deleteAll();
    }

    @Test
    void shouldCreateEmployee() {
        Employee employee = new Employee();
        employee.setName("test");
        employee.setRole("tester");

        Employee newEmployee = restTemplate.postForObject(baseUrl, employee, Employee.class);

        assertNotNull(newEmployee);
        // assertNotNull(newEmployee.getId());
        assertThat(newEmployee.getId()).isNotNull();

    }

    @SuppressWarnings("unchecked")
    @Test
    void shouldGetAllEmployees() {
        List<Employee> employeeList = restTemplate.getForObject(baseUrl, List.class);
        assertNotNull(employeeList);
        assertThat(employeeList.size()).isEqualTo(2);
    }

    @Test
    void shouldGetEmployeeById() {
        Employee existingEmployee = restTemplate.getForObject(baseUrl + "/" + employee1.getId(), Employee.class);
        assertNotNull(existingEmployee);
        assertEquals("test1", existingEmployee.getName());
    }

    @Test
    void shouldDeleteEmployeeById() {
        restTemplate.delete(baseUrl + "/" + employee1.getId());

        int count = employeeRepository.findAll().size();
        assertEquals(1, count);
    }

    @Test
    void shouldUpdateEmployee() {
        employee1.setRole("tester3");

        restTemplate.put(baseUrl + "/{id}", employee1, employee1.getId());
        Employee existingEmployee = restTemplate.getForObject(baseUrl + "/" + employee1.getId(), Employee.class);
        assertNotNull(existingEmployee);
        assertEquals("tester3", existingEmployee.getRole());

    }
}
