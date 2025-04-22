

package com.example;
import org.testng.Assert;
import org.testng.annotations.Test;

public class Database {

    @Test
    public void compareDatabaseAndWebData() throws InterruptedException {
        // Fetch data from the database
        String[] dbData = DatabaseFetcher.fetchDataFromDB();
        String dbItemName = dbData[0];
        String dbNet = dbData[1];
        
        // Fetch data from the web page
        String[] webData = WebHelper.fetchDataFromWeb();
        String webItemName = webData[0];
        String webNet = webData[1];

        // Print fetched data for debugging purposes
        System.out.println("Database Item Name: " + dbItemName);
        System.out.println("Web Item Name: " + webItemName);
        System.out.println("Database Net: " + dbNet);
        System.out.println("Web Net: " + webNet);

        // Compare item_name and net from the database and the webpage

        // Compare Item Names
        if (dbItemName.equalsIgnoreCase(webItemName)) {
            System.out.println("Item Name MATCHED.");
        } else {
            System.out.println("Item Name does NOT MATCH!");
        }

        // Compare Net values
        if (dbNet.trim().equalsIgnoreCase(webNet.trim())) {
            System.out.println("Net Value MATCHED.");
        } else {
            System.out.println("Net Value does NOT MATCH!");
        }

    }
}