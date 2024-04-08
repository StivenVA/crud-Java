package org.project.util;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionFactory {

    private DatabaseConnectionFactory(){}

    public static Connection createConnection(String motor) throws SQLException {
        return switch (motor){
            case "postgresql"-> PostgresQLConnection.getConnection();
            case "mysql" -> MySQLConnection.getConnection();
            default -> throw new IllegalStateException("Unexpected value: " + motor);
        };
    }
}
