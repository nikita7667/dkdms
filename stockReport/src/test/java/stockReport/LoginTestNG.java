package stockReport;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class LoginTestNG {

    @Test
    public void testLoginAndCompareData() throws InterruptedException {
        // Initialize WebDriver
        WebDriver driver = new ChromeDriver();

        // Fetch data from the website (after login)
        String[] webData = LoginTest.loginAndFetchData(driver);
        String webProductCode = webData[0];
        String webPurchaseRate = webData[1];
        String webSalesRate = webData[2];
        String webMrp = webData[3];

        // Fetch data from the database
        List<String[]> dbRecords = DbConnection.fetchDataFromDB();

        // Comparison logic - comparing website data with the database data
        boolean comparisonSuccess = false;
        for (String[] dbRecord : dbRecords) {
            String dbSku = dbRecord[1]; // SKU from DB
            String dbSuperStockiestPrice = dbRecord[2]; // Super Stockiest Purchase Rate
            String dbDistributorPrice = dbRecord[3]; // Distributor Purchase Rate
            String dbRetailerPrice = dbRecord[4]; // Retailer Sales Rate
            String dbMrp = dbRecord[4]; // MRP

            // Compare SKU (product code)
            if (webProductCode.equals(dbSku)) {
                // Assuming user is Super Stockiest for this example (logic to determine user type can be added)
                Assert.assertEquals(webPurchaseRate, dbSuperStockiestPrice, "Purchase rate mismatch");
                Assert.assertEquals(webSalesRate, dbDistributorPrice, "Sales rate mismatch");
                Assert.assertEquals(webMrp, dbMrp, "MRP mismatch");

                // Mark the comparison as successful if everything matches
                comparisonSuccess = true;
                break;
            }
        }

        // Assert that comparison was successful
        Assert.assertTrue(comparisonSuccess, "No matching product found or data mismatch.");

        // Quit the driver
        driver.quit();
    }
}
