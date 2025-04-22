package stockReport;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class LoginTest {

    public static String[] loginAndFetchData(WebDriver driver) throws InterruptedException {
        String[] webData = new String[4]; // Array to store product info

        // Navigate to the login page
        driver.get("https://dkdms.dorfketal.com/login");

        // Find the username and password fields
        WebElement usernameField = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/div/div/form/div[1]/input"));
        WebElement passwordField = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/div/div/form/div[2]/input"));

        // Set username and password values (which will be entered into the login form)
        String username = "mansiadmin";
        String password = "mansiadmin";

        // Enter the username and password using sendKeys
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        // Submit the form by clicking the login button
        WebElement loginButton = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/div/div/form/div[3]/button"));
        loginButton.click();
        Thread.sleep(2000);

        // Navigate to the stock report page (example navigation)
        WebElement order = driver.findElement(By.xpath("//*[@id='accordionSidebar']/li/div[4]/a"));
        order.click();
        Thread.sleep(2000);

        WebElement stockReport = driver.findElement(By.xpath("//*[@id='accordionSidebar']/li/div[4]/div/a[8]"));
        stockReport.click();
        Thread.sleep(2000);

        // Extract some web data
        WebElement productCode = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/div/div/div/div[4]/div/div/div[1]/div[2]/div[3]/div[2]/div/div/div[1]/div[4]"));
        webData[0] = productCode.getText().trim();
        Thread.sleep(1000);

        WebElement p_rate = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/div/div/div/div[4]/div/div/div[1]/div[2]/div[3]/div[2]/div/div/div[1]/div[7]"));
        webData[1] = p_rate.getText().trim();
        Thread.sleep(1000);

        WebElement s_rate = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/div/div/div/div[4]/div/div/div[1]/div[2]/div[3]/div[2]/div/div/div[1]/div[6]"));
        webData[2] = s_rate.getText().trim();
        Thread.sleep(1000);

        WebElement mrp = driver.findElement(By.xpath("//*[@id='root']/div[2]/div/div/div/div/div/div[4]/div/div/div[1]/div[2]/div[3]/div[2]/div/div/div[1]/div[8]"));
        webData[3] = mrp.getText().trim();
        Thread.sleep(1000);

        return webData; // Return fetched web data
    }
}
