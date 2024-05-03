package org.project.fachada;

import org.project.dto.EmployeeDTO;
import org.project.entity.Employee;
import org.project.util.EmployeeMapper;
import org.project.util.InputStreamConverter;

import java.io.File;
import java.util.List;


public class ApplicationFachada {

    private static ApplicationFachada applicationFachada;

    private ApplicationFachada (){
    }

    public static ApplicationFachada getInstance(){
        if (applicationFachada == null){
            applicationFachada = new ApplicationFachada();
        }

        return applicationFachada;
    }

    public void saveEmployee(EmployeeDTO employeeDTO){
        Employee.save(EmployeeMapper.toEmployee(employeeDTO));
    }

    public void deleteEmployee(String id){
        Employee.delete(id);
    }

    public void updateEmployee(EmployeeDTO employeeDTO){
        Employee.update(EmployeeMapper.toEmployee(employeeDTO));
    }

    public EmployeeDTO findById(String id){
        return EmployeeMapper.toEmployeeDTO(Employee.findById(id));
    }

    public List<EmployeeDTO> findAll(){
        return Employee.findAll().stream().map(EmployeeMapper::toEmployeeDTO).toList();
    }

    public void saveVideoLocal(File video){
        Employee.saveVideoLocal(video);
    }
}
