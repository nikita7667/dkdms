package myproject;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class SeleniumWeb {
	
	

    public static List<String[]> fetchDataFromWeb(String username, String password) {
    	
        // List to store product information
        List<String[]> webDataList = new ArrayList<>();
        
        
     

        System.setProperty("webdriver.chrome.driver", "E:\\Testing\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();

        try {
            // Navigate to the login page
            driver.get("https://dkdms.dorfketal.com/login");

            // Wait for login elements and log in
            WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));  // 10 seconds wait time
            
            WebElement usernameField = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='root']/div[2]/div/div/div/div/form/div[1]/input")));
            WebElement passwordField = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/div/div/form/div[2]/input"));

            // Enter the username and password dynamically
            usernameField.sendKeys(username);
            passwordField.sendKeys(password);

            // Submit the form by clicking the login button
            WebElement loginButton = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/div/div/form/div[3]/button"));
            loginButton.click();

            // Wait for navigation after login
            wait.until(ExpectedConditions.urlContains("/dashboard"));

            // Navigate to the stock report page
            WebElement order = driver.findElement(By.xpath("//*[@id='accordionSidebar']/li/div[4]/a"));
            order.click();

            WebElement stockReport = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id='accordionSidebar']/li/div[4]/div/a[8]")));
            stockReport.click();

            // Wait for the stock report data to load
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@id='root']/div[2]/div/div/div/div/div/div[4]/div/div/div[1]/div[2]/div[3]/div[2]/div/div/div[1]/div[4]")));

            List<WebElement> productRows = driver.findElements(By.xpath("//*[@id='root']/div[2]/div/div/div/div/div/div[4]/div/div/div[1]/div[2]/div[3]/div[2]/div/div/div"));

            // Loop through all the located product rows
            for (int i = 1; i <= productRows.size(); i++) {  // Dynamically adjust based on the number of rows found
                String[] webData = new String[4]; // Array to store product info

                WebElement productCode = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/div/div/div/div[4]/div/div/div[1]/div[2]/div[3]/div[2]/div/div/div[" + i + "]/div[4]"));
                webData[0] = productCode.getText().trim();

                WebElement p_rate = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/div/div/div/div[4]/div/div/div[1]/div[2]/div[3]/div[2]/div/div/div[" + i + "]/div[7]"));
                webData[1] = p_rate.getText().trim();

                WebElement s_rate = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/div/div/div/div[4]/div/div/div[1]/div[2]/div[3]/div[2]/div/div/div[" + i + "]/div[6]"));
                webData[2] = s_rate.getText().trim();

                WebElement mrp = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/div/div/div/div[4]/div/div/div[1]/div[2]/div[3]/div[2]/div/div/div[" + i + "]/div[8]"));
                webData[3] = mrp.getText().trim();

                // Add the product data to the list
                webDataList.add(webData);
            }

        } catch (Exception e) {
            e.printStackTrace();	
        } finally {
            // Close the driver after execution
            driver.quit();
        }

        return webDataList; // Return the list of fetched web data
    }
}
