package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;
    private Connection connection;

    private static final String URL = "jdbc:postgresql://localhost:5432/dbtubespbo";
    private static final String USER = "postgres";
    private static final String PASS = "12345";

    private DBConnection() throws Exception {
        connection = DriverManager.getConnection(URL, USER, PASS);
    }

    public static DBConnection getInstance() throws Exception {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;

    }
}
