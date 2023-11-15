package app;

import java.util.Scanner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.sql.CallableStatement;

import ConnectDB.ConnectDB;

public class App {
    static int userIdLogin;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        Connection conn = null;
        conn = ConnectDB.getConnection();

        try {
            System.out.println();
            System.out.println("------------------------ CHAO MUNG BAN DEN NHA SACH ONLINE  ------------------------");
            int chosse = 0;
            do {
                switch (chosse) {
                    case 1:
                        handleLogin(conn);
                        showMenu();
                        break;
                    case 2:
                        handleRegister(conn);
                        showMenu();
                        break;
                    default:
                        showMenu();
                        break;
                }
                chosse = sc.nextInt();
            } while (chosse != 0);
        } catch (Exception e) {
            System.out.println(e.getMessage());
        } finally {
            sc.close();
        }

    }

    public static void showMenu() {
        System.out.println("1. Dang nhap");
        System.out.println("2. Dang ky");
        System.out.println("0. Thoat");
    }

    public static void handleLogin(Connection conn) {
        CallableStatement cStmt = null;
        Scanner sc = new Scanner(System.in);
        // kiem tra
        System.out.println("---------------------- Dang nhap ----------------");
        System.out.print("Nhap so dien thoai: ");
        String phone = sc.nextLine();

        System.out.print("Mat khau: ");
        String password = sc.nextLine();

        try {

            cStmt = conn.prepareCall("{? = call LoginUser(?, ?)}");
            cStmt.setString(2, phone);
            cStmt.setString(3, password);
            cStmt.registerOutParameter(1, Types.INTEGER);
            cStmt.execute();
            int balance = cStmt.getInt(1);
            if (balance != -1) {
                if (balance == 1) {
                    System.out.println();
                    System.out.println("Xin chao admin id = " + balance);
                    homeAdminPage(conn);
                } else {
                    System.out.println();
                    userIdLogin = balance;
                    System.out.println("Thanh cong. Ban da dang nhap id nguoi dung " + balance);
                    // homePage(conn);
                }
                // giao dien trang chu
            } else {
                System.out.println("So dien thoai hoac mat khau khong chinh xac!");
            }

        } catch (Exception e) {
            System.out.println(e);
        }

    }

    public static void handleRegister(Connection conn) {

        Scanner sc = new Scanner(System.in);
        System.out.println("------------------- Dang ky tai khoan --------------------");

        System.out.print("Ho va ten: ");
        String name = sc.nextLine();

        System.out.print("Mat khau: ");
        String password = sc.nextLine();

        System.out.print("So dien thoai: ");
        String phone = sc.nextLine();

        System.out.print("Dia chi: ");
        String address = sc.nextLine();

        try (CallableStatement cStmt = conn.prepareCall("{? =call RegisterUser(?, ?, ?, ?)}")) {
            cStmt.setString(2, name);
            cStmt.setString(3, phone);
            cStmt.setString(4, password);
            cStmt.setString(5, address);

            cStmt.registerOutParameter(1, Types.INTEGER);

            cStmt.execute();
            int balance = cStmt.getInt(1);

            if (balance != -1) {
                System.out.println("Dang ky tai khoan thanh cong voi id = " + balance);

            } else {
                System.out.println("Tai khoan da ton tai !!!!");
            }

        } catch (Exception e) {
            System.out.println("Dang ky that bai !!!! > Loi : " + e.getMessage());
        }
    }

    public static void homeAdminPage(Connection conn) {
        System.out.println("----------------------Trang admin--------------------");
        Scanner sc = new Scanner(System.in);
        int chosse = 0;
        do {
            switch (chosse) {
                case 1:
                    showMenuAdmin();
                    showListBook(conn);
                    showMenuAdmin();
                    break;
                case 2:
                    showMenuAdmin();
                    showBookDetail(conn);
                    showMenuAdmin();

                    break;

                case 3:
                    showMenuAdmin();
                    createBook(conn);
                    showMenuAdmin();

                    break;
                case 4:
                    showMenuAdmin();
                    deleteBook(conn);
                    showMenuAdmin();
                    break;
                case 5:
                    showMenuAdmin();
                    showListUser(conn);
                    showMenuAdmin();
                    break;
                default:
                    showMenuAdmin();
                    break;
            }
            System.out.print("Nhap lua chon cua ban: ");
            chosse = sc.nextInt();
        } while (chosse != 0);
    }

    public static void showMenuAdmin() {
        System.out.println("1. Xem danh sach san pham");
        System.out.println("2. Xem chi tiet san pham");
        System.out.println("3. Them san pham");
        System.out.println("4. Xoa san pham");
        System.out.println("5. Xem danh sach khach hang");

        System.out.println("0. Dang xuat");
    }

    public static void showListBook(Connection conn) {
        try (CallableStatement statement = conn.prepareCall("{call ShowBookList()}")) {
            boolean hasResults = statement.execute();

            if (hasResults) {
                try (ResultSet resultSet = statement.getResultSet()) {
                    System.out.println(
                            "+-------+--------------------------------+---------------+-------------------------+----------------------+----------+----------+");
                    System.out.printf("| %-5s| %-30s | %-14s| %-24s| %-20s | %-8s | %-8s |%n",
                            "idBook", "name", "type", "author", "description", "price", "quantity");
                    System.out.println(
                            "+-------+--------------------------------+---------------+-------------------------+----------------------+----------+----------+");

                    while (resultSet.next()) {
                        int idBook = resultSet.getInt("idBook");
                        String name = resultSet.getString("name");
                        String type = resultSet.getString("type");
                        String author = resultSet.getString("author");
                        String description = resultSet.getString("description");
                        int price = resultSet.getInt("price");
                        int quantity = resultSet.getInt("quantity");

                        System.out.printf("| %-5d | %-30s | %-14s| %-24s| %-20.20s | %-8d | %-8d |%n",
                                idBook, name, type, author, description, price, quantity);
                    }
                    System.out.println(
                            "+-------+--------------------------------+---------------+-------------------------+----------------------+----------+----------+");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void showBookDetail(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("------------------- Xem chi tiet sach --------------------");

        System.out.print("\nNhap ID sach: ");
        int bookID = scanner.nextInt();

        try {
            CallableStatement statement = conn.prepareCall("{call ShowDetailBook(?)}");
            statement.setInt(1, bookID);
            boolean hasResults = statement.execute();

            if (hasResults) {
                try (ResultSet resultSet = statement.getResultSet()) {
                    System.out.println(
                            "+-------+--------------------------------+---------------+-------------------------+----------------------+----------+----------+");
                    // System.out.printf("| %-5s| %-30s | %-14s| %-24s| %-20.20s | %-8s | %-8s |%n",
                    // "idBook", "name", "type", "author", "description", "price", "quantity");
                    // System.out.println(
                    // "+-------+--------------------------------+---------------+-------------------------+----------------------+----------+----------+");

                    while (resultSet.next()) {
                        int idBook = resultSet.getInt("idBook");
                        String name = resultSet.getString("name");
                        String type = resultSet.getString("type");
                        String author = resultSet.getString("author");
                        String description = resultSet.getString("description");
                        int price = resultSet.getInt("price");
                        int quantity = resultSet.getInt("quantity");

                        // System.out.printf("| %-5d | %-30s | %-14s| %-24s| %-20.20s | %-8d | %-8d
                        // |%n",
                        // idBook, name, type, author, description, price, quantity);
                        System.out.println("ID Book: " + idBook);
                        System.out.println("Ten sach: " + name);
                        System.out.println("The loai: " + type);
                        System.out.println("Tac gia: " + author);
                        System.out.println("Mo ta: " + description);
                        System.out.println("Gia: " + price);
                        System.out.println("So Luong: " + quantity);

                    }
                    System.out.println(
                            "+-------+--------------------------------+---------------+-------------------------+----------------------+----------+----------+\n");
                }
            }

        } catch (SQLException e) {
            System.out.println("\nSach khong ton tai. \n");
            // e.printStackTrace();
        }
    }

    public static void createBook(Connection conn) {
        Scanner sc = new Scanner(System.in);
        System.out.println("------------------- Them sach --------------------");

        System.out.print("Nhap ten sach: ");
        String name = sc.nextLine();

        System.out.print("Nhap the loai: ");
        String type = sc.nextLine();

        System.out.print("Nhap tac gia: ");
        String author = sc.nextLine();

        System.out.print("Nhap mo ta: ");
        String description = sc.nextLine();

        System.out.print("Nhap gia: ");
        int price = sc.nextInt();

        System.out.print("Nhap so luong: ");
        int quantity = sc.nextInt();

        try (CallableStatement cStmt = conn.prepareCall("{? =call createBook(?, ?, ?, ?, ?, ?)}")) {
            cStmt.setString(2, name);
            cStmt.setString(3, type);
            cStmt.setString(4, author);
            cStmt.setString(5, description);
            cStmt.setInt(6, price);
            cStmt.setInt(7, quantity);

            cStmt.registerOutParameter(1, Types.INTEGER);

            cStmt.execute();
            int balance = cStmt.getInt(1);

            if (balance != -1) {
                System.out.println("Them sach thanh cong. Voi Id sach = " + balance);

            } else {
                System.out.println("Sach da ton tai !!!!");
            }

        } catch (SQLException e) {
            // System.out.println("Them sach that bai !!!! > Loi : " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void deleteBook(Connection conn) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("------------------- Xoa sach --------------------");

        System.out.print("\nNhap ID sach: ");
        int bookID = scanner.nextInt();
        try {
            CallableStatement cStmt = conn.prepareCall("{? =call DeleteBook(?)}");
            cStmt.registerOutParameter(1, java.sql.Types.INTEGER); // Giá trị trả về
            cStmt.setInt(2, bookID);

            // Thực hiện gọi hàm
            cStmt.execute();

            // Lấy giá trị trả về từ hàm
            int result = cStmt.getInt(1);
            if (result == 1) {
                System.out.println("Xóa sách thành công. \n");
                System.out.println("-------------------------------------------------\n");
            } else if (result == -1) {
                System.out.println("Sách không tồn tại. \n");
            } else {
                System.out.println("Xóa sách thất bại hoặc có lỗi xảy ra. \n");
            }

        } catch (SQLException e) {
            // System.out.println("Xoa sach that bai !!!! > Loi : " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static void showListUser(Connection conn) {
        try (CallableStatement statement = conn.prepareCall("{call ShowUserList()}")) {
            boolean hasResults = statement.execute();

            if (hasResults) {
                try (ResultSet resultSet = statement.getResultSet()) {
                    System.out.println(
                            "+-------+--------------------------------+---------------+-------------------------+----------------------+");
                    System.out.printf("| %-5s| %-30s | %-14s| %-24s| %-20s |%n",
                            "idUser", "name", "phone", "password", "addresss");
                    System.out.println(
                            "+-------+--------------------------------+---------------+-------------------------+----------------------+");

                    while (resultSet.next()) {
                        int idUser = resultSet.getInt("idUser");
                        String name = resultSet.getString("name");
                        String phone = resultSet.getString("phone");
                        String password = resultSet.getString("password");
                        String address = resultSet.getString("address");

                        System.out.printf("| %-5d | %-30s | %-14s| %-24s| %-20.20s |%n",
                                idUser, name, phone, password, address);
                    }
                    System.out.println(
                            "+-------+--------------------------------+---------------+-------------------------+----------------------+");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
