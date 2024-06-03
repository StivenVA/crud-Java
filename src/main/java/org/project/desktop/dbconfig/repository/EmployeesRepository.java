package org.project.desktop.dbconfig.repository;

import org.project.desktop.dbconfig.DatabaseConnectionFactory;
import org.project.desktop.entity.Employee;
import org.project.desktop.interfaces.repository.EmployeeRepository;
import org.project.util.InputStreamConverter;
import org.project.util.ManagerStorage;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.sql.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class EmployeesRepository implements EmployeeRepository {

    private static final String OUTPUT_FILE_PATH = System.getProperty("user.home") + "\\Documents\\videos";

    Connection connection;
    PreparedStatement preparedStatement;

    public EmployeesRepository(String motor){
        try {
            connection = DatabaseConnectionFactory.getInstance().createConnection(motor);
        } catch (SQLException ignored) {
        }
    }

    public File saveVideoLocal(File video){
        ManagerStorage managerStorage = new ManagerStorage(OUTPUT_FILE_PATH);
        return managerStorage.saveFile(video);
    }

    @Override
    public void save(Employee employee) throws Exception{
        String query = "INSERT INTO employee VALUES (?,?,?,?,?,?,?,?,?,?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, Integer.parseInt(employee.getId()));
        preparedStatement.setString(2, employee.getName());
        preparedStatement.setString(3, employee.getLastName());
        preparedStatement.setString(4, employee.getEmail());
        preparedStatement.setString(5, employee.getDirection());
        preparedStatement.setString(6, employee.getPhone());
        preparedStatement.setDate(7, (Date) employee.getBirthdate());
        preparedStatement.setTimestamp(10, Timestamp.valueOf(LocalDate.now().atTime(LocalTime.now())));

        try {
            System.out.println(employee.getImage() +"desde repository");
            FileInputStream fis = new FileInputStream(employee.getImage());
            preparedStatement.setBinaryStream(8, fis, (int) employee.getImage().length());

        }catch (FileNotFoundException e){
            System.out.println("No se encontro el archivo");
            preparedStatement.setBinaryStream(8, null, 0);
        }

        try{
            FileInputStream fis;
            System.out.println(employee.getVideo() +"desde repository");
            fis = employee.getVideo() == null? null: new FileInputStream(employee.getVideo());
            preparedStatement.setBinaryStream(9, fis, employee.getVideo() == null? 0: (int) employee.getVideo().length());

        }
        catch (FileNotFoundException e){
            System.out.println("No se encontro el archivo");
            preparedStatement.setBinaryStream(9, null, 0);
        }

        preparedStatement.executeUpdate();
    }

    @Override
    public Employee findById(String identification) throws SQLException {
        String query = "SELECT * FROM employee WHERE id = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, Integer.parseInt(identification));
        ResultSet resultSet = preparedStatement.executeQuery();
        resultSet.next();
        return createEmployee(resultSet);

    }

    @Override
    public boolean update(Employee employee) throws SQLException {

        FileInputStream fis;
        String query = "update EMPLOYEE set" +
                " name='"+employee.getName()+"'," +
                "last_name='"+employee.getLastName()+"'," +
                "email='"+employee.getEmail()+"'," +
                "direction='"+employee.getDirection()+"'," +
                "phone='"+employee.getPhone()+"'";

        if (employee.getImage() !=null) {
            query += ",image=?,created_at=?  where id ="+employee.getId();
            preparedStatement = connection.prepareStatement(query);
            fis = InputStreamConverter.convertFileToInputStream(employee.getImage());

            preparedStatement.setBinaryStream(1, fis, (int) employee.getImage().length());
            preparedStatement.setTimestamp(2, Timestamp.valueOf(LocalDate.now().atTime(LocalTime.now())));

        }
        else{
            query +=" where id ="+employee.getId();
            preparedStatement = connection.prepareStatement(query);
        }

        try {
             preparedStatement.executeUpdate();
        }catch (SQLException e){
            e.printStackTrace();
            return false;
        }

        return true;
    }

    @Override
    public void deleteById(String id) throws SQLException {
        String query = "DELETE FROM employee where id = " + id;

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
    }

    @Override
    public List<Employee> findAll() {
        List<Employee> employees = new ArrayList<>();
        ResultSet resultSet = null;
        PreparedStatement preparedStatement = null;
        try {
            String query = "SELECT * FROM employee";
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setFetchSize(100);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Employee employee = createEmployee(resultSet);
                employees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try {
                if (resultSet != null) resultSet.close();
                if (preparedStatement != null) preparedStatement.close();

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return employees;
    }


    private Employee createEmployee(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();
        employee.setId(resultSet.getString("id"));
        employee.setName(resultSet.getString("name"));
        employee.setLastName(resultSet.getString("last_name"));
        employee.setEmail(resultSet.getString("email"));
        employee.setDirection(resultSet.getString("direction"));
        employee.setPhone(resultSet.getString("phone"));
        employee.setBirthdate(resultSet.getDate("birthdate"));

        InputStream inputStream = resultSet.getBinaryStream("image");
        if (inputStream!=null) {
            File image = InputStreamConverter.inputStreamToFile(inputStream);
            employee.setImage(image);
        }
        inputStream = resultSet.getBinaryStream("video");
        if (inputStream!=null) {
            File video = InputStreamConverter.inputStreamToFile(inputStream);
            employee.setVideo(video);
        }

        return employee;
    }
}
