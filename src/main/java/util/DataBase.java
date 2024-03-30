package util;

import entity.Employee;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.sql.*;

public class DataBase {

    Connection connection;
    PreparedStatement preparedStatement;


    public DataBase(){
        try {

            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/empleados", "root", "root");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    public void insert(Employee employee) throws Exception{
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

    public ResultSet searchInformation(String identification) throws SQLException {
        String query = "SELECT * FROM employee WHERE id = ?";
        preparedStatement = connection.prepareStatement(query);
        preparedStatement.setString(1, String.valueOf(Integer.parseInt(identification)));
        return preparedStatement.executeQuery();

    }

    public boolean updateInformation(Employee employee) throws SQLException {
        FileInputStream fis = null;
        String query = "update employee set" +
                " name=?," +
                "last_name=?," +
                "email=?," +
                "direction=?," +
                "phone=?" +
                "image=? where id ="+employee.getId();

        preparedStatement = connection.prepareStatement(query);

        preparedStatement.setString(1, employee.getName());
        preparedStatement.setString(2, employee.getLastName());
        preparedStatement.setString(3, employee.getEmail());
        preparedStatement.setString(4, employee.getDirection());
        preparedStatement.setString(5, employee.getPhone());

        if (employee.getImage() !=null) {
            try {
                fis = new FileInputStream(employee.getImage());
            } catch (FileNotFoundException e) {
                throw new RuntimeException(e);
            }
            preparedStatement.setBinaryStream(6, fis, (int) employee.getImage().length());

        }
        else {
            preparedStatement.setBlob(6,employee.getUpdateImage());
        }

        preparedStatement.executeUpdate();

        try {
            preparedStatement = connection.prepareStatement(query);
            preparedStatement.executeUpdate();
        }catch (SQLException e){
            return false;
        }

        return true;
    }

    public void deleteInformation(String id) throws SQLException {
        String query = "DELETE FROM employee where id = " + id;

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
    }

}
