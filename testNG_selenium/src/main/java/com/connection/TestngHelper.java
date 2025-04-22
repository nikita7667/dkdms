package com.connection;

import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

public class TestngHelper {

    @Test
    public void compareWebDataToDatabase() throws InterruptedException {
        // Fetch all records from the database (10 records)
        List<String[]> dbRecords = Connection.fetchDataFromDB();
        boolean isTestFailed = false; // Flag to track if any mismatch occurs

        // Fetch all records from the web (5 records)
        List<String[]> webDataList = Seleniumweb.fetchDataFromWeb();

        // Iterate over the web records (5 records)
        for (int i = 0; i < webDataList.size(); i++) {
            String[] webData = webDataList.get(i);
            String webItemName = webData[0];  // item_name from the web
            String webNet = webData[1];       // net value from the web

            boolean isItemMatchFound = false; // Track if an item_name match is found
            boolean isNetMatchFound = false;  // Track if both item_name and net match

            // Iterate over all 10 database records
            for (int j = 0; j < dbRecords.size(); j++) {
                String[] dbRecord = dbRecords.get(j);
                String dbItemName = dbRecord[1];  // item_name from the DB
                String dbNet = dbRecord[2];       // net value from the DB

                // Compare both item_name and net values
                if (dbItemName.equalsIgnoreCase(webItemName)) {
                    isItemMatchFound = true;  // We found a matching item_name

                    if (dbNet.trim().equalsIgnoreCase(webNet.trim())) {
                        // If both item_name and net match, we found a full match
                        isNetMatchFound = true;
                        Connection.insertLog("TestCase_" + (i + 1), webItemName, dbItemName, webNet, dbNet, "PASS");
                    } else {
                        // If item_name matches but net value does not match
                        isTestFailed = true;  // Mark the test as failed due to the net value mismatch
                        Connection.insertLog("TestCase_" + (i + 1), webItemName, dbItemName, webNet, dbNet, "FAIL");
                    }
                }
            }

            // Check if the web item was not found in the database at all
            if (!isItemMatchFound) {
                isTestFailed = true;  // Mark the test as failed if no match was found
                Connection.insertLog("TestCase_" + (i + 1), webItemName, "N/A", webNet, "N/A", "FAIL");
            } else if (!isNetMatchFound) {
                // Item was found but no matching net value was found
                isTestFailed = true;  // Mark the test as failed due to the net mismatch
                Connection.insertLog("TestCase_" + (i + 1), webItemName, "N/A", webNet, "N/A", "FAIL");
            }
        }

        // After checking all web records, assert failure if any mismatch was found
        if (isTestFailed) {
            Assert.fail("Test failed: Some records did not match between the web data and database records.");
        } else {
            System.out.println("All records matched between the web data and database records.");
        }
    }
}
