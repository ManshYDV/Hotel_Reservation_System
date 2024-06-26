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

public class AccountManager {

    private Connection conn;
    private Scanner sc;

    public AccountManager(Connection conn, Scanner sc) {
        this.conn = conn;
        this.sc = sc;
    }

    public void credit(long currAcc) throws SQLException {
        sc.nextLine();
        System.out.println("Enter amount");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter Security pin");
        String pin = sc.nextLine();
        try {
            conn.setAutoCommit(false);
            if (currAcc != 0) {
                PreparedStatement pstmt = conn.prepareStatement("SELECT * from accounts WHERE account_number=? and security_pin=?");
                pstmt.setLong(1, currAcc);
                pstmt.setString(2, pin);
                ResultSet data = pstmt.executeQuery();
                if (data.next()) {
                    String creditQuery = "UPDATE accounts SET balance=balance + ? WHERE account_number=?";
                    pstmt = conn.prepareStatement(creditQuery);
                    pstmt.setDouble(1, amount);
                    pstmt.setLong(2, currAcc);
                    int rows = pstmt.executeUpdate();
                    if (rows != 0) {
                        System.out.println("Money credited successfully");
                        conn.commit();
                        conn.setAutoCommit(true);
                        return;
                    } else {
                        System.out.println("Transaction failed");
                        conn.rollback();
                        conn.setAutoCommit(true);
                    }
                } else {
                    System.out.println("Enter correct security pin");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.setAutoCommit(true);
    }

    public void debit(long currAcc) throws SQLException {
        sc.nextLine();
        System.out.println("Enter debit amount");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter security pin");
        String pin = sc.nextLine();
        try {
            conn.setAutoCommit(false);
            if (currAcc != 0) {
                PreparedStatement pstmt = conn.prepareStatement("SELECT * from accounts WHERE account_number=? and security_pin=?");
                pstmt.setLong(1, currAcc);
                pstmt.setString(2, pin);
                ResultSet data = pstmt.executeQuery();
                if (data.next()) {
                    double currBalance = data.getDouble("balance");
                    if (amount <= currBalance) {
                        pstmt = conn.prepareStatement("UPDATE accounts SET balance=balance-? WHERE account_number=? ");
                        pstmt.setDouble(1, amount);
                        pstmt.setLong(2, currAcc);
                        int rows = pstmt.executeUpdate();
                        if (rows != 0) {
                            System.out.println("Amount debited successfullt");
                            conn.commit();
                            conn.setAutoCommit(true);
                        } else {
                            System.out.println("Transaction failed");
                            conn.rollback();
                            conn.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient balance");
                    }
                } else {
                    System.out.println("Please enter valid pin");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.setAutoCommit(true);
    }

    public void transfer(long currAcc) throws SQLException {
        sc.nextLine();
        System.out.println("Enter receiver account number");
        long recAcc = sc.nextLong();
        System.out.println("Enter amount to transfer");
        double amount = sc.nextDouble();
        sc.nextLine();
        System.out.println("Enter security pin");
        String pin = sc.nextLine();
        try {
            conn.setAutoCommit(false);
            if (currAcc != 0 && recAcc != 0) {
                PreparedStatement pstmt = conn.prepareStatement("SELECT * from accounts WHERE account_number=? and WHERE security_pin=?");
                pstmt.setLong(1, currAcc);
                pstmt.setString(2, pin);
                ResultSet data = pstmt.executeQuery();
                if (data.next()) {
                    double balance = data.getDouble("balance");
                    if (amount <= balance) {
                        String dQuery = "UPDATE accounts SET balance=balance-? WHERE account_number=?";
                        String cQuery = "UPDATE accounts SET balance=balance+amount WHERE account_number=?";
                        pstmt = conn.prepareStatement(dQuery);
                        pstmt.setDouble(1, amount);
                        pstmt.setLong(2, currAcc);
                        int dRows = pstmt.executeUpdate();
                        pstmt = conn.prepareStatement(cQuery);
                        pstmt.setDouble(1, amount);
                        pstmt.setLong(2, recAcc);
                        int cRows = pstmt.executeUpdate();
                        if (cRows != 0 && dRows != 0) {
                            System.out.println("Money Transferred successfuly.");
                            conn.commit();
                            conn.setAutoCommit(true);
                        } else {
                            System.out.println("Transaction failed");
                            conn.rollback();
                            conn.setAutoCommit(true);
                        }
                    } else {
                        System.out.println("Insufficient balance");
                    }
                } else {
                    System.out.println("Invalid security pin");
                }
            } else {
                System.out.println("Invalid account number");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        conn.setAutoCommit(true);
    }

    public void checkBalance(long currAcc) throws SQLException {
        sc.nextLine();
        System.out.println("Enter security pin");
        String pin = sc.nextLine();
        try {
            PreparedStatement pstmt = conn.prepareStatement("SELECT *  from accounts WHERE account_number=? and security_pin=?");
            pstmt.setLong(1, currAcc);
            pstmt.setString(2, pin);
            ResultSet data = pstmt.executeQuery();
            if (data.next()) {
                double balance = data.getDouble("balance");
                System.out.println("Available balance is : " + balance);
            } else {
                System.out.println("Invalid pin");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
