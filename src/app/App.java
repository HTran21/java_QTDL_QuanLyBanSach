package app;
// import java.util.Scanner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ConnectDB.ConnectDB;

public class App {
    public static void main(String[] args) throws Exception {
        // Scanner sc = new Scanner(System.in);
        // Connection conn = null;
        // conn = ConnectDB.getConnection();

        try (Connection conn = ConnectDB.getConnection()) {
            if (conn != null) {
                System.out.println("Connected to the database");
                // Hiển thị dữ liêu từ bảng khachhang
                displayCustomerData(conn);
            } else {
                System.out.println("Failed to connect to the database");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        // System.out.println("Connection DB: " + conn);
        // System.out.println("Tao ket noi thanh cong");
    }

    public static void displayCustomerData(Connection connection) {
        try (PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM user");
                ResultSet resultSet = preparedStatement.executeQuery()) {

            System.out.println("idUser\tfullName\tphone\tgender\tpassword\taddress\trole");
            while (resultSet.next()) {
                int idUser = resultSet.getInt("idUser");
                String fullName = resultSet.getString("fullName");
                String phone = resultSet.getString("phone");
                String gender = resultSet.getString("gender");
                String password = resultSet.getString("password");
                String address = resultSet.getString("address");
                String role = resultSet.getString("role");

                System.out.println(
                        idUser + "\t" + fullName + "\t" + phone + "\t" + gender + "\t" + password + "\t" + address
                                + "\t" + role);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
