/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package bankingsystem;

/**
 *
 * @author manis
 */
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.util.Scanner;

public class User {

    private Connection conn;
    private Scanner sc;

    public User(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    public void register() {
        sc.nextLine();
        System.out.println("Enter Full Name:");
        String fullName = sc.nextLine();
        System.out.println("Enter Email");
        String email = sc.nextLine();
        System.out.println("Enter Password");
        String password = sc.nextLine();
        if (user_exist(email)) {
            System.out.println("User already exits for this email address, please enter new emil");
            return;
        }
        String reg_query = "INSERT INTO user(full_name, email, password) VALUES(?,?,?)";
        try {
            PreparedStatement pstmt = conn.prepareStatement(reg_query);
            pstmt.setString(1, fullName);
            pstmt.setString(2, email);
            pstmt.setString(3, password);
            int rows = pstmt.executeUpdate();
            if (rows != 0) {
                System.out.println("Registration successful");
            } else {
                System.out.println("Registration failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public String login() {
        sc.nextLine();
        System.out.println("Email :");
        String email = sc.nextLine();
        System.out.println("Password :");
        String password = sc.nextLine();
        String login_query = "SELECT * from user WHERE email=? and password=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(login_query);
            pstmt.setString(1, email);
            pstmt.setString(2, password);
            ResultSet data = pstmt.executeQuery();
            if (data.next()) {
                return email;
            } else {
                return null;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean user_exist(String email) {
        String query = "SELECT * from user WHERE email=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet data = pstmt.executeQuery();
            if (data.next()) {
                return true;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
