package org.project.util;

import org.project.desktop.dto.EmployeeDTO;
import org.project.desktop.entity.Employee;

public class EmployeeMapper {
    public static EmployeeDTO toEmployeeDTO(Employee employee) {

        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setId(employee.getId());
        employeeDTO.setName(employee.getName());
        employeeDTO.setLastName(employee.getLastName());
        employeeDTO.setEmail(employee.getEmail());
        employeeDTO.setDirection(employee.getDirection());
        employeeDTO.setPhone(employee.getPhone());
        employeeDTO.setBirthdate(employee.getBirthdate());
        employeeDTO.setImage(employee.getImage());
        employeeDTO.setVideo(employee.getVideo());

        return employeeDTO;
    }

    public static Employee toEmployee(EmployeeDTO employeeDTO) {

        Employee employee = new Employee();
        employee.setId(employeeDTO.getId());
        employee.setName(employeeDTO.getName());
        employee.setLastName(employeeDTO.getLastName());
        employee.setEmail(employeeDTO.getEmail());
        employee.setDirection(employeeDTO.getDirection());
        employee.setPhone(employeeDTO.getPhone());
        employee.setBirthdate(employeeDTO.getBirthdate());
        employee.setVideo(employeeDTO.getVideo());

        if (employeeDTO.getUpdateImage() !=null) {
            employee.setImage(InputStreamConverter.inputStreamToFile(employeeDTO.getUpdateImage()));
        }
        else{
            employee.setImage(employeeDTO.getImage());
        }

        return employee;
    }
}
