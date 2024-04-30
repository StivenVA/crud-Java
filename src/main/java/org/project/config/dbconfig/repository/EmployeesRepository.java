package org.project.config.dbconfig.repository;

import org.project.config.dbconfig.DatabaseConnectionFactory;
import org.project.dto.EmployeeDTO;
import org.project.entity.Employee;
import org.project.interfaces.repository.EmployeeRepository;
import org.project.util.InputStreamConverter;

import java.io.*;
import java.sql.*;

public class EmployeesRepository implements EmployeeRepository {

    Connection connection;
    PreparedStatement preparedStatement;

    public EmployeesRepository(String motor){
        try {
            connection = DatabaseConnectionFactory.getInstance().createConnection(motor);
        } catch (SQLException ignored) {
        }
    }

    @Override
    public void save(Employee employee) throws Exception{
        String query = "INSERT INTO employee VALUES (?,?,?,?,?,?,?,?)";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, Integer.parseInt(employee.getId()));
        preparedStatement.setString(2, employee.getName());
        preparedStatement.setString(3, employee.getLastName());
        preparedStatement.setString(4, employee.getEmail());
        preparedStatement.setString(5, employee.getDirection());
        preparedStatement.setString(6, employee.getPhone());
        preparedStatement.setDate(7, (Date) employee.getBirthdate());
        FileInputStream fis = new FileInputStream(employee.getImage());
        preparedStatement.setBinaryStream(8, fis, (int) employee.getImage().length());

        preparedStatement.executeUpdate();
    }

    @Override
    public ResultSet findById(String identification) throws SQLException {
        String query = "SELECT * FROM employee WHERE id = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setInt(1, Integer.parseInt(identification));
        return preparedStatement.executeQuery();

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
            query += ",image=?  where id ="+employee.getId();
            preparedStatement = connection.prepareStatement(query);
            fis = InputStreamConverter.convertFileToInputStream(employee.getImage());

            preparedStatement.setBinaryStream(1, fis, (int) employee.getImage().length());
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

}
