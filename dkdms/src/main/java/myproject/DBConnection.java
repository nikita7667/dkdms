package myproject;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;


public class DBConnection {

    // Database connection parameters
    public static final String DB_URL = "jdbc:mysql://localhost:3306/testingdb?useSSL=false";
   public static final String DB_USERNAME = "root";
   public static final String DB_PASSWORD = "root"; 
   
    // Method to fetch last processed login ID from login_counter table
    public static int fetchLastLoginId() {
        int lastLoginId = 0; // Default to 0 if no records found
        String query = "SELECT last_login_id FROM login_counter ORDER BY id DESC LIMIT 1";

        try (java.sql.Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            if (rs.next()) {
                lastLoginId = rs.getInt("last_login_id");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return lastLoginId;
    }

    // Method to update the last processed login ID in the login_counter table
    public static void updateLastLoginId(int newLastLoginId) {
        String query = "INSERT INTO login_counter (last_login_id) VALUES (?)";

        try (java.sql.Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, newLastLoginId);
            pstmt.executeUpdate();
            System.out.println("Last login ID updated to: " + newLastLoginId);

        } catch (SQLException e) {
            e.printStackTrace();  // Make sure any errors are logged
            System.err.println("Error updating last login ID in login_counter.");
        }
    }


    
 // Method to clear the login_counter table and reset the auto-increment value
    public static void clearLoginCounter() {
        String deleteQuery = "DELETE FROM login_counter";
        String resetAutoIncrementQuery = "ALTER TABLE login_counter AUTO_INCREMENT = 1";  // Reset auto-increment

        try (java.sql.Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = connection.createStatement()) {

            // First, clear all records from the login_counter
            stmt.executeUpdate(deleteQuery);
            System.out.println("All records cleared from login_counter.");

            // Then, reset the auto-increment value to start from 1
            stmt.executeUpdate(resetAutoIncrementQuery);
            System.out.println("Auto-increment value for login_counter reset to 1.");

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error clearing login_counter or resetting auto-increment.");
        }
    }
    
    // Modify fetchAllLogins method to include pagination
    public static List<String[]> fetchLoginsWithLimit(int startId, int limit) {
        List<String[]> loginsList = new ArrayList<>();
        String query = "SELECT username, password, type FROM logins WHERE id > ? LIMIT ?";

        try (java.sql.Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(query)) {

            pstmt.setInt(1, startId);
            pstmt.setInt(2, limit);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                String[] login = new String[3];
                login[0] = rs.getString("username");
                login[1] = rs.getString("password");
                login[2] = rs.getString("type");
                loginsList.add(login);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return loginsList;
    }
    
    public static String fetchUserType(String username) {
        String userType = null;

        // Create the SQL query to fetch user type
        String query = "SELECT type FROM logins WHERE username = ?"; // Adjust table/column names as necessary

        try (Connection connection = getConnection(); PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                userType = rs.getString("type");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return userType;
    }
    
    // Helper method to establish a connection to the database (you can adjust this as needed)
    private static Connection getConnection() throws SQLException {
        // Return the database connection (you'll need to implement this based on your DB setup)
        return DriverManager.getConnection("jdbc:mysql://localhost:3306/testingdb?useSSL=false", "root", "root");
    }

    
    
    

    // Method to fetch product data from the database
    public static List<String[]> fetchDataFromDB() {
        List<String[]> records = new ArrayList<>();

        // Use try-with-resources to automatically close resources
        try (java.sql.Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, sku, super_stockiest_price_in_gst, distributor_price_in_gst,retailer_price_in_gst, mrp FROM productpricing LIMIT 160")) {

            // Extract data from result set
            while (rs.next()) {
                String[] record = new String[6]; // Array to store id, sku, super_stockiest_price_in_gst, distributor_price_in_gst, mrp
                record[0] = rs.getString("id"); // Store id
                record[1] = rs.getString("sku"); // Store sku (product code)
                record[2] = rs.getString("super_stockiest_price_in_gst"); // Store super_stockiest_price_in_gst
                record[3] = rs.getString("distributor_price_in_gst"); // Store distributor_price_in_gst
                record[4] = rs.getString("retailer_price_in_gst");
                record[5] = rs.getString("mrp"); // Store mrp
                records.add(record); // Add record to the list
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace in case of errors
        }

        return records;
    }

    // Method to insert comparison logs into the comparison_logs table
    public static void insertComparisonLog(String username, String password, String userType, 
                                           String webProductCode, String dbSKU, String productCodeResult, 
                                           String webPurchaseRate, String dbPurchaseRate, String purchaseRateResult, 
                                           String webSalesRate, String dbSalesRate, String salesRateResult, 
                                           String webMRP, String dbMRP, String mrpResult, String finalResult) {
        
        String insertLogSQL = "INSERT INTO comparison_logs (username, password, type, web_purchasecode, db_sku, product_code_result, " +
                              "web_purchaserate, db_purchaserate, purchase_rate_result, web_salesrate, db_salesrate, " +
                              "sales_rate_result, web_mrp, db_mrp, mrp_result, final_result) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (java.sql.Connection connection = DriverManager.getConnection(DB_URL, DB_USERNAME, DB_PASSWORD);
             PreparedStatement pstmt = connection.prepareStatement(insertLogSQL)) {

            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, userType);
            pstmt.setString(4, webProductCode);
            pstmt.setString(5, dbSKU);
            pstmt.setString(6, productCodeResult);
            pstmt.setString(7, webPurchaseRate);
            pstmt.setString(8, dbPurchaseRate);
            pstmt.setString(9, purchaseRateResult);
            pstmt.setString(10, webSalesRate);
            pstmt.setString(11, dbSalesRate);
            pstmt.setString(12, salesRateResult);
            pstmt.setString(13, webMRP);
            pstmt.setString(14, dbMRP);
            pstmt.setString(15, mrpResult);
            pstmt.setString(16, finalResult);

            // Execute the insert query
            pstmt.executeUpdate();
            System.out.println("Log inserted successfully for Web Product Code: " + webProductCode);

        } catch (SQLException e) {
            e.printStackTrace();
            System.err.println("Error inserting log for Web Product Code: " + webProductCode);
        }
    }

    public static void main(String[] args) {
        // Fetch records from the database
        List<String[]> dbRecords = fetchDataFromDB();

        // Print the fetched data
        System.out.println("Fetched Records from Database:");
        for (String[] record : dbRecords) {
            System.out.println("ID: " + record[0] + ", sku: " + record[1] + ", super_stockiest_price_in_gst: " + record[2]
                    + ", distributor_price_in_gst: " + record[3] + ", retailer_price_in_gst:" + record[4] + ", mrp: " + record[5]);
        }
    }
}
