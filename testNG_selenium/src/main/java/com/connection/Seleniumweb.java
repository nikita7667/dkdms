package com.connection;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.openqa.selenium.JavascriptExecutor;

public class Seleniumweb {
    WebDriver driver;

    public static List<String[]> fetchDataFromWeb() throws InterruptedException {
        // List to store item_name and net values for each record
        List<String[]> webDataList = new ArrayList<>();

		System.setProperty("webdriver.chrome.driver","E:\\Testing\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        driver.get("https://dkdms.dorfketal.com/login");

        // Wait for login elements and login
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(20));  // Increased wait time

        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/form/div[1]/input")));
        WebElement passwordField = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/form/div[2]/input"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/form/div[3]/button"));
        
        usernameField.sendKeys("mansiadmin");
        passwordField.sendKeys("mansiadmin");
        loginButton.click();

        // Wait for the menu to load and navigate
        WebElement entries = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"accordionSidebar\"]/li/div[3]/a")));
        entries.click();
        
        WebElement addBills = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"accordionSidebar\"]/li/div[3]/div/a[2]")));
        addBills.click();

        // Selecting the party and product
        WebElement partyclickBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[1]/form/div[1]/div[1]/div[2]/div[2]/div[1]/div/div[1]/input[1]")));
        partyclickBox.click();
        
        WebElement partyselect = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"PartyNameTypehead-item-0\"]/div/div/small[1]")));
        partyselect.click();

        // Wait for items to be clickable and interact with them
        for (int i = 1; i <= 5; i++) {  // Assuming 5 rows, adjust if needed
            String[] webData = new String[2];

            // Dynamic XPath for rows
            WebElement iclickBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[1]/form/div[2]/div/div[1]/div/div/table/tbody/tr[" + i + "]/td[2]/div/div/div/div[1]/input[1]")));
            
            // Scroll to the 5th item (if needed) before interacting with it
            if (i == 5) {
                WebElement fifthItem = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[1]/form/div[2]/div/div[1]/div/div/table/tbody/tr[5]"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fifthItem); // Scroll the 5th item into view
                Thread.sleep(500);  // Wait for the page to stabilize after scroll
            }

            iclickBox.click();

            // Wait for the item element to be clickable
            WebElement itemElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"selectedproducttypeahead-item-" + (i - 1) + "\"]/div/div/small[1]")));
            itemElement.click();

            // Select batch and enter quantity
            WebElement clickbatchBox = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[1]/form/div[2]/div/div[1]/div/div/table/tbody/tr[" + i + "]/td[3]/div/div[1]/div/div[1]/input[1]")));
            clickbatchBox.click();
            
            try {
                WebElement selectbatch = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"VendorBatchTypehead-item-0\"]/div/small[1]")));

                // If batch is found, use JavaScript click
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", selectbatch);
            } catch (Exception e) {
                // If no batch is found, skip this row and continue with the next iteration
                System.out.println("No batch found for row " + i + ", skipping to next.");
                continue;  // Skip to next iteration
            }

            // Continue with the rest of the process
            WebElement clickQty = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[1]/form/div[2]/div/div[1]/div/div/table/tbody/tr[" + i + "]/td[5]/input")));
            clickQty.sendKeys("1");

            // Fetch item name and net value
            WebElement itemNameElement = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[1]/form/div[2]/div/div[1]/div/div/table/tbody/tr[" + i + "]/td[2]/div/div/div/div[1]/input[1]"));
            webData[0] = itemNameElement.getAttribute("value").trim();

            WebElement netElement = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[1]/form/div[2]/div/div[1]/div/div/table/tbody/tr[" + i + "]/td[15]"));
            webData[1] = netElement.getText().trim(); // net value

            // Add to the list
            webDataList.add(webData);

            // Click Add button (if needed)
            WebElement addButton = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[1]/form/div[2]/div/div[1]/div/div/section/div/div[1]/button"));
            addButton.click();
        }

        driver.quit();  // Close the browser
        return webDataList;  // Return all fetched data
    }
}
