package app;

import java.util.Scanner;

import java.sql.Connection;
import java.sql.PreparedStatement;
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
                    System.out.println("Xem danh sach san pham");
                    showMenuAdmin();
                    break;
                case 2:
                    showMenuAdmin();
                    System.out.println("Them san pham");
                    showMenuAdmin();

                    break;

                case 3:
                    showMenuAdmin();
                    System.out.println("Xoa san pham");
                    showMenuAdmin();

                    break;
                case 4:
                    showMenuAdmin();
                    System.out.println("Xem danh sach order");
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
        System.out.println("2. Them san pham");
        System.out.println("3. Xoa san pham");
        System.out.println("4. Xem danh sach order");

        System.out.println("0. Dang xuat");
    }

    
}
