package org.project.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgresQLConnection {
    private static Connection connection;

    private PostgresQLConnection(){}

    public static Connection getConnection() throws SQLException {
        if (connection == null)
            connection = DriverManager.getConnection("jdbc:postgresql://127.0.0.1:5432/employee", "postgres", "root");

        return connection;
    }
}
