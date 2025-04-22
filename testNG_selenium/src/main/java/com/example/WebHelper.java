package com.example;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class WebHelper {
	WebDriver driver;
    public static String[] fetchDataFromWeb() throws InterruptedException {
    	
         String[] webData = new String[2]; // Array to store item_name and net values
        
        // Set up the ChromeDriver (make sure you have the proper driver installed)
 		System.setProperty("webdriver.chrome.driver","E:\\Testing\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        // Open the webpage
        driver.get("https://dkdms.dorfketal.com/login");
        
	    WebElement usernameField = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/form/div[1]/input"));
	    WebElement passwordField = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/form/div[2]/input"));
	    WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/form/div[3]/button"));

      // Enter credentials
        usernameField.sendKeys("mansiadmin");
        passwordField.sendKeys("mansiadmin");
      
      // Click on the login button
        loginButton.click();
        Thread.sleep(2000);
        
        
        WebElement entries = driver.findElement(By.xpath("//*[@id=\"accordionSidebar\"]/li/div[3]/a"));
        entries.click();
        Thread.sleep(2000);
      
        WebElement addBills = driver.findElement(By.xpath("//*[@id=\"accordionSidebar\"]/li/div[3]/div/a[2]"));
	    addBills.click();
	    Thread.sleep(2000);
	    
	    WebElement partyclickBox = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[1]/form/div[1]/div[1]/div[2]/div[2]/div[1]/div/div[1]/input[1]"));
		  partyclickBox.click();
	    Thread.sleep(2000);
	    
	    WebElement partyselect = driver.findElement(By.xpath("//*[@id=\"PartyNameTypehead-item-0\"]/div/div/small[1]"));
	    partyselect.click();
	    Thread.sleep(2000);
	    
	    
	    WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
      
	    WebElement iclickBox = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[1]/form/div[2]/div/div[1]/div/div/table/tbody/tr/td[2]/div/div/div/div[1]/input[1]"));
	    iclickBox.click();
	    Thread.sleep(2000);
	      
	    // Wait for the item element to be clickable
	    WebElement itemElement = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"selectedproducttypeahead-item-1\"]/div/div/small[1]")));
	    itemElement.click();  // Click the item
	    Thread.sleep(2000);
      
	    WebElement clickbatchBox = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[1]/form/div[2]/div/div[1]/div/div/table/tbody/tr/td[3]/div/div[1]/div/div[1]/input[1]"));
	    clickbatchBox.click();
	    Thread.sleep(2000);
	
	    WebElement selectbatch = driver.findElement(By.xpath("//*[@id=\"VendorBatchTypehead-item-0\"]/div/small[1]"));
	    selectbatch.click();
	    Thread.sleep(2000);
	
	    WebElement clickQty = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[1]/form/div[2]/div/div[1]/div/div/table/tbody/tr/td[5]/input"));
	    clickQty.click();
	    clickQty.sendKeys("1");
	    Thread.sleep(2000);    
	     
	    WebElement add = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[1]/form/div[2]/div/div[1]/div/div/section/div/div[1]/button"));
	    add.click();
	    Thread.sleep(2000);
	    
	    WebElement itemNameElement = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[1]/form/div[2]/div/div[1]/div/div/table/tbody/tr/td[2]/div/div/div/div[1]/input[1]"));
	    webData[0] = itemNameElement.getAttribute("value").trim();
        
	    WebElement netElement = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[1]/form/div[2]/div/div[1]/div/div/table/tbody/tr/td[15]"));
	    webData[1] = netElement.getText().trim(); // net
        
        driver.quit();
        
        return webData;
    }
}
