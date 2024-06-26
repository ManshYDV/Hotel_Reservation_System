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

public class Accounts {

    private Connection conn;
    private Scanner sc;

    public Accounts(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    public long openAccount(String email) {
        if (!accountExist(email)) {
            String query = "INSERT INTO accounts(account_number,full_name, email, balance, security_pin)VALUES(?,?,?,?,?)";
            System.out.println();
            sc.nextLine();
            System.out.println("Enter Full name");
            String fullName = sc.nextLine();
            System.out.println("Enter initial amount");
            double balance = sc.nextDouble();
            sc.nextLine();
            System.out.println("Enter Security pin");
            String pin = sc.nextLine();
            try {
                long accNumber = generateAccNumber();
                PreparedStatement pstmt = conn.prepareStatement(query);
                pstmt.setLong(1, accNumber);
                pstmt.setString(2, fullName);
                pstmt.setString(3, email);
                pstmt.setDouble(4, balance);
                pstmt.setString(5, pin);
                int rows = pstmt.executeUpdate();
                if (rows != 0) {
                    return accNumber;
                } else {
                    throw new RuntimeException("Account creation failed");
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        throw new RuntimeException("Account already exist");
    }

    public long getAccNumber(String email) {
        String query = "SELECT account_number FROM accounts WHERE email=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet data = pstmt.executeQuery();
            if (data.next()) {
                return data.getLong("account_number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        throw new RuntimeException("Account number doesn't exist");
    }

    public long generateAccNumber() {
        try {
            Statement stmt = conn.createStatement();
            ResultSet data = stmt.executeQuery("SELECT account_number FROM accounts ORDER BY account_number DESC LIMIT 1");
            if (data.next()) {
                long lastNumber = data.getLong("account_number");
                return lastNumber + 1;
            } else {
                return 10000100;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 10000100;
    }

    public boolean accountExist(String email) {
        String query = "SELECT * from accounts WHERE email=?";
        try {
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, email);
            ResultSet data = pstmt.executeQuery();
            if (data.next()) {
                return true;
            } else {
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
