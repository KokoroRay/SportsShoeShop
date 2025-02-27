/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBContext {
    private final String url = "jdbc:sqlserver://localhost:1433;databaseName=group5";
    private final String user = "sa";
    private final String password = "0123456789";

    public Connection getConnection() throws SQLException {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            return DriverManager.getConnection(url, user, password);
        } catch (ClassNotFoundException | SQLException e) {
            throw new SQLException("Error connecting to database: " + e.getMessage());
        }
    }
   
    public static void main(String[] args) {
        System.out.println("Java Version: " + System.getProperty("java.version"));
    }

}

