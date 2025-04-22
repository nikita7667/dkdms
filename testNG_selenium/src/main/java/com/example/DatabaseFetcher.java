
package com.example;

import java.sql.*;

public class DatabaseFetcher {

    public static String[] fetchDataFromDB() {
        String[] data = new String[2]; // Array to store item_name and net values
        Connection connection = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            // Database connection parameters
            String url = "jdbc:mysql://localhost:3306/testingdb?useSSL=false"; // useSSL=false to avoid SSL errors (optional)
            String username = "root";
            String password = "root";

            // Establish the connection
            connection = DriverManager.getConnection(url, username, password);
            stmt = connection.createStatement();

            // SQL query to get item_name and net
            String query = "SELECT item_name, net FROM bills WHERE id = 101";
            rs = stmt.executeQuery(query);

            // Extract data from result set
            if (rs.next()) {
                data[0] = rs.getString("item_name"); // Correct column name casing
                data[1] = rs.getString("net");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            // Close resources in the finally block to ensure they are closed even if an error occurs
            try {
                if (rs != null) rs.close();
                if (stmt != null) stmt.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return data;
    }

    // Main method to execute the program
    public static void main(String[] args) {
        // Example of calling the method to fetch data from the database
        String[] dbData = fetchDataFromDB();
        
        // Print the fetched data to the console
        System.out.println("Item Name: " + dbData[0]);
        System.out.println("Net: " + dbData[1]);
    }
}




