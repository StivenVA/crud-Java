package org.project.interfaces.repository;

import org.project.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends Repository<Employee, String>{
    List<Employee> findAll();
}
