/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package hotel_reservation;

/**
 *
 * @author manis
 */
import java.sql.Connection;
import java.sql.Statement;
import java.sql.SQLException;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.util.Scanner;

public class Hotel_Reservation {

    /**
     * @param args the command line arguments
     */
    private static final String url = "jdbc:mysql://localhost:3306/example";
    private static final String user = "root";
    private static final String password = "";

    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        // TODO code application logic here
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.out.println(e.getMessage());
        }
        try {
            Connection conn = DriverManager.getConnection(url, user, password);
            Statement stmt = conn.createStatement();
            while (true) {
                System.out.println();
                System.out.println("Hotel Reservation System");
                Scanner sc = new Scanner(System.in);
                System.out.println("1. Reserve a room");
                System.out.println("2. View Reservations");
                System.out.println("3. Get Room Number");
                System.out.println("4. Update Reservation");
                System.out.println("5. Delete Reservation");
                System.out.println("6. Exit");
                System.out.println("Choose an valid option from above");
                int choice = sc.nextInt();
                sc.nextLine();
                switch (choice) {
                    case 1:
                        reserveRoom(conn, sc, stmt);
                        break;
                    case 2:
                        viewReserv(conn, stmt);
                        break;
                    case 3:
                        getRoom(conn, sc, stmt);
                        break;
                    case 4:
                        updateReserv(conn, sc, stmt);
                        break;
                    case 5:
                        deleteReserv(conn, sc, stmt);
                        break;
                    case 6:
                        exit();
                        sc.close();
                        return;
                    default:
                        System.out.println("Invalid input selected, try again");
                }
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private static void reserveRoom(Connection conn, Scanner sc, Statement stmt) {
        try {
            System.out.println("Enter guest name : ");
            String name = sc.nextLine();
            System.out.println("Enter room no : ");
            int rNo = sc.nextInt();
            System.out.println("Enter contact number : ");
            int number = sc.nextInt();
            sc.nextLine();
            String query = "INSERT INTO hotel_res (guest_name, room_no, contact_no) "
                    + "VALUES ('" + name + "', " + rNo + ", '" + number + "')";

            int rows = stmt.executeUpdate(query);
            if (rows > 0) {
                System.out.println(rows + " Affected");
            } else {
                System.out.println("Insertion failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void viewReserv(Connection conn, Statement stmt) {
        String query = "SELECT * from hotel_res;";
        try {
            ResultSet data = stmt.executeQuery(query);
            System.out.println("Hotel Reservation");
            System.out.println("+----------+-------------------+------------+----------------+-------------+");
            System.out.println("| R_ID     | Guest_Name        | Room_No    | Contact_No     | Time & Date |");
            System.out.println("+----------+-------------------+------------+----------------+-------------+");
            while (data.next()) {
                int id = data.getInt("R_ID");
                int rNo = data.getInt("room_no");
                String pNum = data.getString("contact_no");
                String gName = data.getString("guest_name");
                String time = data.getTimestamp("reserv_date").toString();
                System.out.printf("| %-14d | %-15s | %-13d | %-20s | %-19s |\n",
                        id, gName, rNo, pNum, time);
            }
            System.out.println("+----------+-------------------+------------+----------------+-------------+");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void getRoom(Connection conn, Scanner sc, Statement stmt) {
        try {
            System.out.println("Enter reservation id");
            int id = sc.nextInt();
            String query = "SELECT room_no from hotel_res WHERE r_id = " + id;
            ResultSet data = stmt.executeQuery(query);
            if (data.next()) {
                System.out.println("Room number for reservation id is " + data.getInt("room_no"));
            } else {
                System.out.println("Reservation not found for the given id");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void updateReserv(Connection conn, Scanner sc, Statement stmt) {
        try {
            System.out.println("Enter id number to update reservation");
            int id = sc.nextInt();
            sc.nextLine();
            if (!reservCheck(conn, id, stmt)) {
                System.out.println("Reservation not found for the entered id");
                return;
            }
            System.out.println("Enter guest name");
            String name = sc.nextLine();
            System.out.println("Enter new room number");
            int roomNo = sc.nextInt();
            sc.nextLine();
            System.out.println("Enter new Contact number");
            String pNo = sc.nextLine();
            String query = "UPDATE hotel_res SET guest_name='" + name + "', "
                    + "room_no=" + roomNo + ", "
                    + "contact_no='" + pNo + "' "
                    + "WHERE r_id=" + id;

            int rows = stmt.executeUpdate(query);
            if (rows > 0) {
                System.out.println(rows + " rows affected");
            } else {
                System.out.println(rows + " rows affected");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static void deleteReserv(Connection conn, Scanner sc, Statement stmt) {
        try {
            System.out.println("Enter reserve id to delete");
            int id = sc.nextInt();
            if (!reservCheck(conn, id, stmt)) {
                System.out.println("No reservation found for the enterd id");
                return;
            }
            String query = "DELETE FROM hotel_res WHERE R_ID = " + id;
            int row = stmt.executeUpdate(query);
            if (row > 0) {
                System.out.println(row + " rows deleted successfully");
            } else {
                System.out.println(row + "Deletion failed");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static boolean reservCheck(Connection conn, int id, Statement stmt) {
        try {
            String query = "SELECT r_id FROM hotel_res WHERE r_ID=" + id;
            ResultSet data = stmt.executeQuery(query);
            return data.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private static void exit() throws InterruptedException {
        System.out.print("Exiting Database");
        int i = 5;
        while (i != 0) {
            System.out.print(".");
            Thread.sleep(500);
            i--;
        }
        System.out.println("");
        System.out.println("Thank you for using hotel reservation system :)");
    }
}
