package stockReport;

import java.sql.*;

public class DatabaseUtils {

    // Method to check the database for user credentials and return username, password, and login type
    public static String[] getDatabaseCredentials(String username, String password) {
        Connection connection = null;
        PreparedStatement stmt1 = null;
        PreparedStatement stmt2 = null;
        ResultSet rs1 = null;
        ResultSet rs2 = null;
        String[] dbData = new String[3]; // Array to store username, password, and login type

        try {
            // Connect to the database
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/testingdb?useSSL=false", "root", "root");

            // Prepare a query for the super_stockiest table
            String query1 = "SELECT * FROM super_stockiest WHERE user_name = ? AND user_password = ?";
            stmt1 = connection.prepareStatement(query1);
            stmt1.setString(1, username);
            stmt1.setString(2, password);
            rs1 = stmt1.executeQuery();

            // Check if the user is found in the super_stockiest table
            if (rs1.next()) {
                dbData[0] = rs1.getString("user_name"); // Store username
                dbData[1] = rs1.getString("user_password"); // Store password
                dbData[2] = "super_stockiest"; // Login type from super_stockiest
            } else {
                // If not found in super_stockiest, check the distributor table
                String query2 = "SELECT * FROM distributor WHERE user_dis = ? AND password_dis = ?";
                stmt2 = connection.prepareStatement(query2);
                stmt2.setString(1, username);
                stmt2.setString(2, password);
                rs2 = stmt2.executeQuery();

                // Check if the user is found in the distributor table
                if (rs2.next()) {
                    dbData[0] = rs2.getString("user_dis"); // Store username
                    dbData[1] = rs2.getString("password_dis"); // Store password
                    dbData[2] = "distributor"; // Login type from distributor
                } else {
                    System.out.println("User not found in either table");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // Close resources
            try {
                if (rs1 != null) rs1.close();
                if (rs2 != null) rs2.close();
                if (stmt1 != null) stmt1.close();
                if (stmt2 != null) stmt2.close();
                if (connection != null) connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return dbData; // Return username, password, and login type
    }
}
