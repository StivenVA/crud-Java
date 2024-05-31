package org.project.desktop.dbconfig;

import org.project.desktop.dbconfig.connection.MySQLConnection;
import org.project.desktop.dbconfig.connection.PostgresQLConnection;

import java.sql.Connection;
import java.sql.SQLException;

public class DatabaseConnectionFactory {

    private static DatabaseConnectionFactory databaseConnectionFactory;

    private DatabaseConnectionFactory(){}

    public static DatabaseConnectionFactory getInstance(){
        if (databaseConnectionFactory == null)
            databaseConnectionFactory = new DatabaseConnectionFactory();

        return databaseConnectionFactory;
    }

    public Connection createConnection(String motor) throws SQLException {
        return switch (motor){
            case "postgresql"-> PostgresQLConnection.getConnection();
            case "mysql" -> MySQLConnection.getConnection();
            default -> throw new IllegalStateException("Unexpected value: " + motor);
        };
    }
}
