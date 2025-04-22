package myproject;

import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.List;

public class TestngHelper {

    @Test
    public void compareWebDataToDatabaseForBatchedLogins() throws InterruptedException {
        // Fetch last processed login ID from the login_counter table
        int lastLoginId = DBConnection.fetchLastLoginId();
        int limit = 2; // Set the limit to process logins per batch

        // Fetch the next batch of logins
        List<String[]> loginsList = DBConnection.fetchLoginsWithLimit(lastLoginId, limit);

        if (loginsList == null || loginsList.isEmpty()) {
            // Reset the login counter when no new logins are found
            System.out.println("No new logins found. Resetting login counter.");
            DBConnection.clearLoginCounter();  // Call the method to reset the login_counter
            Assert.fail("No new logins found in the logins table. Resetting the login counter.");
        }

        boolean isTestFailed = false;

        for (String[] login : loginsList) {
            String username = login[0];
            String password = login[1];
            String userType = login[2];

            System.out.println("\nChecking user: " + username + " (Type: " + userType + ")");

            // Fetch product data from the database
            List<String[]> dbRecords = DBConnection.fetchDataFromDB();

            if (dbRecords == null || dbRecords.isEmpty()) {
                System.out.println("No records found in the database.");
                continue;
            }

            // Fetch product data from the web
            List<String[]> webDataList = SeleniumWeb.fetchDataFromWeb(username, password);

            if (webDataList == null || webDataList.isEmpty()) {
                System.out.println("No records found on the website.");
                continue;
            }

            // Compare web and database records for this user
            for (int i = 0; i < webDataList.size(); i++) {
                String[] webData = webDataList.get(i);
                String webProductCode = webData[0];
                String webPurchaseRate = webData[1];
                String webSalesRate = webData[2];
                String webMRP = webData[3];

                boolean isRecordMatched = false;

                for (String[] dbRecord : dbRecords) {
                    String dbProductCode = dbRecord[1];
                    String dbPurchaseRate, dbSalesRate, dbMRP;

                    // Compare based on user type
                    if (userType.equals("super_stockiest")) {
                        dbPurchaseRate = dbRecord[2];  // Super Stockiest purchase rate
                        dbSalesRate = dbRecord[3];     // Distributor sales rate
                    } else if (userType.equals("distributor")) {
                        dbPurchaseRate = dbRecord[3];  // Distributor purchase rate
                        dbSalesRate = dbRecord[4];     // Retailer sales rate
                    } else {
                        System.out.println("Error: Unknown user type '" + userType + "' for username: " + username);
                        return;  // Exit the comparison process for this user since the type is invalid
                    }

                    dbMRP = dbRecord[5];  // MRP

                    // Compare web and db product records
                    if (dbProductCode.equalsIgnoreCase(webProductCode) &&
                        dbPurchaseRate.trim().equalsIgnoreCase(webPurchaseRate.trim()) &&
                        dbSalesRate.trim().equalsIgnoreCase(webSalesRate.trim()) &&
                        dbMRP.trim().equalsIgnoreCase(webMRP.trim())) {

                        isRecordMatched = true;
                        break;  // Exit inner loop on successful match
                    }
                }

                // Log comparison results for this user
                String productCodeResult = isRecordMatched ? "Pass" : "Fail";
                String purchaseRateResult = webPurchaseRate.equals(webData[1]) ? "Pass" : "Fail";
                String salesRateResult = webSalesRate.equals(webData[2]) ? "Pass" : "Fail";
                String mrpResult = webMRP.equals(webData[3]) ? "Pass" : "Fail";

                DBConnection.insertComparisonLog(username, password, userType, webProductCode, webProductCode, productCodeResult,
                                               webPurchaseRate, webPurchaseRate, purchaseRateResult, webSalesRate,
                                               webSalesRate, salesRateResult, webMRP, webMRP, mrpResult,
                                               productCodeResult.equals("Pass") && purchaseRateResult.equals("Pass") &&
                                               salesRateResult.equals("Pass") && mrpResult.equals("Pass") ? "Pass" : "Fail");

                // Flag if there was a mismatch for this record
                if (!isRecordMatched) {
                    System.out.println("No complete match found for Web Product Code: " + webProductCode);
                    isTestFailed = true;
                }
            }
        }

        int newLastLoginId = lastLoginId + loginsList.size();
        DBConnection.updateLastLoginId(newLastLoginId);

        // Reset the login counter if this is the last batch
        if (loginsList.size() < limit) {
            System.out.println("Last batch processed. Resetting login counter.");
            DBConnection.clearLoginCounter();  // Call the method to reset the login_counter
        }

        // Assert test failure if any mismatch occurred
        if (isTestFailed) {
            Assert.fail("Test failed: Some web records did not match any database records.");
        } else {
            System.out.println("All records successfully matched for all users.");
        }
    }
}
