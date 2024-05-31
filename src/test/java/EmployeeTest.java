import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.project.desktop.dto.EmployeeDTO;
import org.project.desktop.entity.Employee;
import org.project.desktop.fachada.ApplicationFachada;
import org.project.util.EmployeeMapper;

import java.io.File;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertSame;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class EmployeeTest {

    private Employee employee;
    private ApplicationFachada applicationFachada;

    @BeforeEach
    void setUp() {
        employee = mock(Employee.class);
        applicationFachada = mock(ApplicationFachada.class);
        // Set up employee mock with necessary data
        when(employee.getId()).thenReturn("1");
        when(employee.getName()).thenReturn("John");
        when(employee.getLastName()).thenReturn("Doe");
        when(employee.getEmail()).thenReturn("email@email.com");
        when(employee.getDirection()).thenReturn("123 Main St");
        when(employee.getPhone()).thenReturn("1234567890");
        when(employee.getBirthdate()).thenReturn(new Date());
        when(employee.getImage()).thenReturn(new File("image.jpg"));
        when(employee.getVideo()).thenReturn(new File("video.mp4"));

        EmployeeDTO employeeDTO = EmployeeMapper.toEmployeeDTO(employee);

        when(applicationFachada.findById("1")).thenReturn(employeeDTO);
        when(applicationFachada.findAll()).thenReturn(List.of(employeeDTO));
    }

    @Test
    void testEmployeeToEmployeeDTO(){
        EmployeeDTO employeeDTO = EmployeeMapper.toEmployeeDTO(employee);

        assertEquals(employee.getId(), employeeDTO.getId());
        assertEquals(employee.getName(), employeeDTO.getName());
        assertEquals(employee.getLastName(), employeeDTO.getLastName());

        assertSame(employeeDTO.getId(),applicationFachada.findById("1").getId());
        assertEquals(employeeDTO.getClass(),applicationFachada.findById("1").getClass());
    }

    @Test
    void testListOfEmployees() {
        EmployeeDTO employeeDTO = applicationFachada.findById("1");

        List<EmployeeDTO> employees = applicationFachada.findAll();

        employees.forEach(employeeDTO1 -> {
            assertEquals(employeeDTO1.getId(), employeeDTO.getId());
            assertEquals(employeeDTO1.getName(), employeeDTO.getName());
            assertEquals(employeeDTO1.getLastName(), employeeDTO.getLastName());
            assertEquals(employeeDTO1.getEmail(), employeeDTO.getEmail());
            assertEquals(employeeDTO1.getDirection(), employeeDTO.getDirection());
            assertEquals(employeeDTO1.getPhone(), employeeDTO.getPhone());
            assertEquals(employeeDTO1.getBirthdate(), employeeDTO.getBirthdate());
            assertEquals(employeeDTO1.getImage(), employeeDTO.getImage());
            assertEquals(employeeDTO1.getVideo(), employeeDTO.getVideo());
        });

    }

    @Test
    void testEmployeeDTOtoEmployee() {
        EmployeeDTO employeeDTO = applicationFachada.findById("1");
        Employee employee = EmployeeMapper.toEmployee(employeeDTO);

        assertEquals(employee.getId(), employeeDTO.getId());
        assertEquals(employee.getName(), employeeDTO.getName());
        assertEquals(employee.getLastName(), employeeDTO.getLastName());
        assertEquals(employee.getEmail(), employeeDTO.getEmail());
        assertEquals(employee.getDirection(), employeeDTO.getDirection());
        assertEquals(employee.getPhone(), employeeDTO.getPhone());
        assertEquals(employee.getBirthdate(), employeeDTO.getBirthdate());
        assertEquals(employee.getImage(), employeeDTO.getImage());
        assertEquals(employee.getVideo(), employeeDTO.getVideo());

        assertNotEquals(employee.getClass(), employeeDTO.getClass());

    }

}
