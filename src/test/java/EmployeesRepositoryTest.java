import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.project.config.dbconfig.repository.EmployeesRepository;
import org.project.entity.Employee;

import java.io.File;
import java.sql.SQLException;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.*;

public class EmployeesRepositoryTest {

    private EmployeesRepository employeesRepository;
    private Employee employee;

    @BeforeEach
    void setUp() throws SQLException {
        employeesRepository = mock(EmployeesRepository.class);

        employee = mock(Employee.class);
        // Set up employee mock with necessary data
        when(employee.getId()).thenReturn("9632");

        when(employee.getImage()).thenReturn(new File("image.jpg"));
        when(employee.getVideo()).thenReturn(new File("video.mp4"));
        when(employee.getBirthdate()).thenReturn(new Date());
        when(employee.getName()).thenReturn("John");
        when(employee.getLastName()).thenReturn("Doe");
        when(employee.getEmail()).thenReturn("email@email.com");
        when(employee.getBirthdate()).thenReturn(java.sql.Date.valueOf("2020-05-02"));

        when(employeesRepository.findById("9632")).thenReturn(employee);
        when(employeesRepository.findAll()).thenReturn(List.of(employee));

    }

    @Test
    void saveEmployeeTest() throws Exception {

        employeesRepository.save(employee);

        verify(employeesRepository).save(employee);
    }

    @Test
    void deleteEmployeeTest() throws Exception {

        employeesRepository.deleteById(employee.getId());

        verify(employeesRepository).deleteById(employee.getId());
    }

    @Test
    void updateEmployeeTest() throws Exception {

        employeesRepository.update(employee);

        verify(employeesRepository).update(employee);
    }

    @Test
    void findByIdEmployeeTest() throws SQLException {

        Employee employeeFounded = employeesRepository.findById("9632");

        assertEquals("9632", employeeFounded.getId());

        verify(employeesRepository).findById(employee.getId());
    }

    @Test
    void findAllEmployeesTest(){

        List<Employee> employees = employeesRepository.findAll();

        assertEquals(1, employees.size());
        assertSame(employee, employees.get(0));

        verify(employeesRepository).findAll();
    }


}
