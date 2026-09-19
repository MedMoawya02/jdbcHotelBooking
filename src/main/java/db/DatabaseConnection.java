package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static volatile DatabaseConnection instance;
    private Connection connection;

    private final String url = "jdbc:postgresql://localhost:5433/hotel_db";
    private final String user = "postgres";
    private final String password = "mouawya1234";
    private DatabaseConnection() {
        try {
            connection = DriverManager.getConnection(
                    url,
                    user,
                    password
            );

        } catch (SQLException e) {
            throw new RuntimeException(
                    "Erreur de connexion JDBC : "
                            + e.getMessage(),
                    e
            );
        }
    }

    public static DatabaseConnection getInstance() {

        if (instance == null) {

            synchronized (DatabaseConnection.class) {

                if (instance == null) {
                    instance = new DatabaseConnection();
                }
            }
        }

        return instance;
    }

    public Connection getConnection() {
        return connection;
    }
}