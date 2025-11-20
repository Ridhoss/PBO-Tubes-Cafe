/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package database;

/**
 *
 * @author RIDHO
 */
public class DBConnection {

    private static final String URL = "jdbc:postgresql://localhost:5432/dbtubespbo";
    private static final String USER = "postgres";
    private static final String PASS = "1234";

    public static java.sql.Connection getConnection() throws Exception {
        return java.sql.DriverManager.getConnection(URL, USER, PASS);
    }
}
