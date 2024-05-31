package org.project.desktop.interfaces.observer;

import org.project.desktop.dto.EmployeeDTO;

public interface Observer {
    void notify(EmployeeDTO employeeDTO);
}
