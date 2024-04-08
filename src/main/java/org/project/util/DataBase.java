package org.project.util;

import org.project.entity.Employee;

import java.io.*;
import java.sql.*;
import org.project.Main;

public class DataBase {

    Connection connection;
    PreparedStatement preparedStatement;


    public DataBase(){
        try {
           connection = DatabaseConnectionFactory.createConnection("postgresql");
        }catch (SQLException e){
            e.printStackTrace();
        }
    }

    private void crearTablas() throws IOException, SQLException {

        InputStream inputStream = Main.class.getResourceAsStream("/crear_tablas.sql");
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
        StringBuilder sqlBuilder = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sqlBuilder.append(line);
            sqlBuilder.append("\n");
        }
        reader.close();

       preparedStatement = connection.prepareStatement(sqlBuilder.toString());
       preparedStatement.executeUpdate();
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
        preparedStatement.setInt(1, Integer.parseInt(identification));
        return preparedStatement.executeQuery();

    }

    private FileInputStream convertImageToInputStream(File image){
        try {
            return new FileInputStream(image);
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean updateInformation(Employee employee) throws SQLException {

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
            fis = convertImageToInputStream(employee.getImage());

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

    public void deleteInformation(String id) throws SQLException {
        String query = "DELETE FROM employee where id = " + id;

        preparedStatement = connection.prepareStatement(query);
        preparedStatement.executeUpdate();
    }

}
