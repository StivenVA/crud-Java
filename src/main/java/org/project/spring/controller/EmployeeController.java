package org.project.spring.controller;

import lombok.RequiredArgsConstructor;
import org.project.spring.entity.Employee;
import org.project.spring.service.EmployeeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/employees")
@RequiredArgsConstructor
@RestController
public class EmployeeController {
    private final EmployeeService employeeService;

    @GetMapping
    public ResponseEntity<?> findAll(){

        return ResponseEntity.ok(employeeService.findAll());
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody Employee employee){
        return ResponseEntity.ok(employeeService.save(employee));
    }

}
