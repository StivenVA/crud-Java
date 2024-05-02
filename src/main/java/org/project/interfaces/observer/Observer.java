package org.project.interfaces.observer;

import org.project.dto.EmployeeDTO;

import java.io.File;

public interface Observer {
    void notify(EmployeeDTO employeeDTO);
}
