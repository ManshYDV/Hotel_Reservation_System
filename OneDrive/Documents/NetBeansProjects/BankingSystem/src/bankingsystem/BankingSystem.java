/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
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

public class BankingSystem {

    /**
     * @param args the command line arguments
     */
    private static final String url = "jdbc:mysql://localhost:3306/banking_system";
    private static final String id = "root";
    private static final String password = "@DMINmSaQnL123";

    public static void main(String[] args) throws ClassNotFoundException, SQLException, InterruptedException {
        // TODO code application logic here
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            System.out.println("Drivers Loaded Successfully");

        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage()+"error is here");
        }
        try {
            Connection conn = DriverManager.getConnection(url, id, password);
            System.out.println("Connection Established");
            Scanner sc = new Scanner(System.in);
            User user = new User(conn, sc);
            Accounts ac = new Accounts(conn, sc);
            AccountManager am = new AccountManager(conn, sc);
            String email;
            long accNumber;
            while (true) {
                System.out.println("****Welcome to Banking System****");
                System.out.println();
                System.out.println("1. Register");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                System.out.println("Select any desired option");
                int choice1 = sc.nextInt();
                switch (choice1) {
                    case 1:
                        user.register();
                        System.out.println("\033[H\033[2J");
                        System.out.flush();
                        break;
                    case 2:
                        email = user.login();
                        if (email != null) {
                            System.out.println();
                            System.out.println("User logged in");
                            if (!ac.accountExist(email)) {
                                System.out.println();
                                System.out.println("1. Open a new bank account");
                                System.out.println("2 Exit");
                                if (sc.nextInt() == 1) {
                                    accNumber = ac.openAccount(email);
                                    System.out.println("Account created successfully");
                                    System.out.println("Your acc number is " + accNumber);
                                } else {
                                    break;
                                }
                            }
                            accNumber = ac.getAccNumber(email);
                            int choice2 = 0;

                            while (choice2 != 5) {
                                System.out.println();
                                System.out.println("1. Debit Money");
                                System.out.println("2. Credit Money");
                                System.out.println("3. Transfer Money");
                                System.out.println("4. Check Balance");
                                System.out.println("5. Log Out");
                                choice2 = sc.nextInt();
                                switch (choice2) {
                                    case 1:
                                        am.debit(accNumber);
                                        break;
                                    case 2:
                                        am.credit(accNumber);
                                        break;
                                    case 3:
                                        am.transfer(accNumber);
                                        break;
                                    case 4:
                                        am.checkBalance(accNumber);
                                        break;
                                    case 5:
                                        break;
                                    default:
                                        System.out.println("Select valid option only");
                                        break;
                                }
                            }
                        } else {
                            System.out.println("Incorrect email or password");
                        }
                    case 3:
                        System.out.println("Thank you for using Banking System");
                        Thread td = new Thread();
                        int i = 5;
                        while (i != 0) {
                            td.sleep(500);
                            System.out.print("*");
                            i--;
                        }
                        System.out.println("Logged out");
                        break;
                    default:
                        System.out.println("Please select vlaid option");
                        break;
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();

        }
    }

}
