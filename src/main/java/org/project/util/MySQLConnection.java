package org.project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {

    private static Connection connection;

    private MySQLConnection(){}

    public static Connection getConnection() throws SQLException {

        if (connection == null)
            connection = DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/employee", "root", "root");

        return connection;
    }
}
