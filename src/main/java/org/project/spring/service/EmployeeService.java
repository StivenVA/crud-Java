package org.project.spring.service;

import lombok.RequiredArgsConstructor;
import org.project.spring.entity.Employee;
import org.project.spring.repository.EmployeeRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmployeeService {
    private final EmployeeRepository employeeRepository;

    public List<Employee> findAll(){
        return employeeRepository.findAll();
    }

    public Employee findById(Integer id){
        return employeeRepository.findById(id).orElse(null);
    }
}
