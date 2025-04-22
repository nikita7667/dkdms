package com.connection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class Connection {  // Renamed class to avoid conflict with java.sql.Connection

    public static List<String[]> fetchDataFromDB() {
        List<String[]> records = new ArrayList<>(); // List to hold records

        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/testingdb?useSSL=false";
        String username = "root";
        String password = "root";

        // Use try-with-resources to automatically close resources
        try (java.sql.Connection connection = DriverManager.getConnection(url, username, password);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, item_name, net FROM bills LIMIT 10")) {

            // Extract data from result set
            while (rs.next()) {
                String[] record = new String[3]; // Array to store id, item_name, net
                record[0] = rs.getString("id"); // Store id
                record[1] = rs.getString("item_name"); // Store item_name
                record[2] = rs.getString("net"); // Store net
                records.add(record); // Add record to the list
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace in case of errors
        }

        return records;
    }

    // Method to insert test log into the database
    public static void insertLog(String testCaseId, String webItemName, String dbItemName, String webNet, String dbNet, String result) {
        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/testingdb?useSSL=false";
        String username = "root";
        String password = "root";

        String insertSQL = "INSERT INTO test_logs (test_case_id, web_item_name, db_item_name, web_net, db_net, result) VALUES (?, ?, ?, ?, ?, ?)";

        try (java.sql.Connection connection = DriverManager.getConnection(url, username, password);
             PreparedStatement pstmt = connection.prepareStatement(insertSQL)) {

            // Set the parameters for the prepared statement
            pstmt.setString(1, testCaseId);
            pstmt.setString(2, webItemName);
            pstmt.setString(3, dbItemName);
            pstmt.setString(4, webNet);
            pstmt.setString(5, dbNet);
            pstmt.setString(6, result);

            // Execute the insert statement
            pstmt.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace in case of errors
        }
    }

    public static void main(String[] args) {
        // Fetch records from the database
        List<String[]> dbRecords = fetchDataFromDB();
        
        // Print the fetched data
        System.out.println("Fetched Records from Database:");
        for (String[] record : dbRecords) {
            System.out.println("ID: " + record[0] + ", Item Name: " + record[1] + ", Net: " + record[2]);
        }
    }
}
