package ConnectDB;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {
    private static String url = "jdbc:mysql://localhost:3306/quanlybansach";
    private static String username = "root";
    private static String password = "";
    private static Connection con;

    public static Connection getConnection() {
        try {
            con = DriverManager.getConnection(url, username, password);
            System.out.println("Connection successfully!!!");
        } catch (SQLException ex) {
            // log an exception. fro example:
            ex.printStackTrace();
            System.out.println("Failed to create the database connection.");
        }
        return con;
    }
}
