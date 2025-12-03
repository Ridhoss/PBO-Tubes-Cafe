package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {

    private static DBConnection instance;
    private Connection connection;

    // Sesuaikan konfigurasi database Anda di sini
    private static final String URL = "jdbc:postgresql://localhost:5432/dbtubespbo";
    private static final String USER = "postgres";
    private static final String PASS = "12345"; // Pastikan password ini BENAR

    private DBConnection() {
        // Constructor kosong, inisialisasi dilakukan di getConnection()
    }

    public static DBConnection getInstance() {
        if (instance == null) {
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        try {
            // Cek apakah koneksi null atau sudah tertutup
            if (connection == null || connection.isClosed()) {
                try {
                    // Coba load driver (opsional untuk versi JDBC baru, tapi aman)
                    Class.forName("org.postgresql.Driver");
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
                // Buka koneksi baru
                connection = DriverManager.getConnection(URL, USER, PASS);
            }
        } catch (SQLException e) {
            System.err.println("Gagal membuka koneksi database: " + e.getMessage());
            e.printStackTrace();
        }
        return connection;
    }
}