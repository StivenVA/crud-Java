package org.project.desktop.interfaces.repository;

import org.project.desktop.entity.Employee;

import java.util.List;

public interface EmployeeRepository extends Repository<Employee, String>{
    List<Employee> findAll();
}
