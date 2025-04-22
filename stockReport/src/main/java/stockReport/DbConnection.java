package stockReport;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DbConnection {

    // Fetch product pricing data from the database
    public static List<String[]> fetchDataFromDB() {
        List<String[]> records = new ArrayList<>(); // List to hold records

        // Database connection parameters
        String url = "jdbc:mysql://localhost:3306/testingdb?useSSL=false";
        String username = "root";
        String password = "root";

        // Use try-with-resources to automatically close resources
        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT id, sku, super_stockiest_price_in_gst, distributor_price_in_gst, retailer_price_ex_gst, mrp FROM `productpricing` LIMIT 160")) {

            // Extract data from result set
            while (rs.next()) {
                String[] record = new String[6]; // Array to store id, sku, super_stockiest_price_in_gst, distributor_price_in_gst, and mrp
                record[0] = rs.getString("id"); // Store id
                record[1] = rs.getString("sku"); // Store sku
                record[2] = rs.getString("super_stockiest_price_in_gst"); // Store super_stockiest_price_in_gst
                record[3] = rs.getString("distributor_price_in_gst"); // Store distributor_price_in_gst
                record[4] = rs.getString("retailer_price_ex_gst"); // Store retailer_price_ex_gst
                record[5] = rs.getString("mrp"); // Store mrp
                records.add(record); // Add record to the list
            }

        } catch (SQLException e) {
            e.printStackTrace(); // Print the stack trace in case of errors
        }

        return records;
    }

    // Generalized method to fetch login credentials
    public static List<String[]> fetchCredentials(String tableName, String userColumn, String passwordColumn) {
        List<String[]> credentials = new ArrayList<>();

        String url = "jdbc:mysql://localhost:3306/testingdb?useSSL=false";
        String username = "root";
        String password = "root";

        String query = String.format("SELECT %s, %s FROM `%s`", userColumn, passwordColumn, tableName);

        try (Connection connection = DriverManager.getConnection(url, username, password);
             Statement stmt = connection.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                String[] record = new String[2]; // Array to store username and password
                record[0] = rs.getString(userColumn); // Store username
                record[1] = rs.getString(passwordColumn); // Store password
                credentials.add(record);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return credentials;
    }

    public static void main(String[] args) {
        // Fetch and display product pricing data
        List<String[]> dbRecords = fetchDataFromDB();
        System.out.println("Fetched Product Pricing Records from Database:");
        for (String[] record : dbRecords) {
            System.out.println("ID: " + record[0] + ", SKU: " + record[1] + ", Super Stockiest Price: " + record[2] 
            + ", Distributor Price: " + record[3] + ", Retailer Price: " + record[4] + ", MRP: " + record[5]);
        }

        // Fetch and display Super Stockiest login credentials
        List<String[]> superStockiestRecords = fetchCredentials("super_stockiest", "user_name", "user_password");
        System.out.println("\nSuper Stockiest Login Credentials:");
        for (String[] record : superStockiestRecords) {
            System.out.println("Username: " + record[0] + ", Password: " + record[1]);
        }

        // Fetch and display Distributor login credentials
        List<String[]> distributorRecords = fetchCredentials("distributor", "user_dis", "password_dis");
        System.out.println("\nDistributor Login Credentials:");
        for (String[] record : distributorRecords) {
            System.out.println("Username: " + record[0] + ", Password: " + record[1]);
        }
    }
}
