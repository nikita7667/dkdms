package com.example;

import org.apache.log4j.BasicConfigurator;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.NoSuchElementException;
import java.util.concurrent.TimeUnit;

public class NewTest {

    WebDriver driver;

    @BeforeClass
    public void setup() throws InterruptedException {
        // Setup WebDriver
		System.setProperty("webdriver.chrome.driver","E:\\Testing\\chromedriver-win64\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        driver.manage().window().maximize();

        // Navigate to the webpage
        driver.get("https://dkdms.dorfketal.com/");
        Thread.sleep(2000);

    }

    @Test(priority = 1)
    public void testLogin() throws InterruptedException {
    	
    	driver.findElement(By.xpath("//*[@id=\"sidebarToggle\"]")).click();
        Thread.sleep(2000);

    	driver.findElement(By.xpath("//*[@id=\"accordionSidebar\"]/div[1]/a/span")).click();
        Thread.sleep(2000);


    	
        WebElement usernameField = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/form/div[1]/input"));
        WebElement passwordField = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/form/div[2]/input"));
        WebElement loginButton = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/form/div[3]/button"));

        // Enter credentials
        usernameField.sendKeys("mansiadmin");
        passwordField.sendKeys("mansiadmin");
        
        

        // Click on the login button
        loginButton.click();
        System.out.println("login successfully");
        Thread.sleep(2000);
    }

    @Test(priority = 2)
    public void testNavigateToEntries() throws InterruptedException {
        // Click on entries
        WebElement entries = driver.findElement(By.xpath("//*[@id=\"accordionSidebar\"]/li/div[3]/a"));
        entries.click();
        Thread.sleep(2000);
    }

    @Test(priority = 3)
    public void testNavigateToBilling() throws InterruptedException {
        // Click on billing
        WebElement billing = driver.findElement(By.xpath("//*[@id=\"accordionSidebar\"]/li/div[3]/div/a[1]"));
        billing.click();
        Thread.sleep(2000);
    }
    
    @Test(priority = 4)
    public void datePicker() throws InterruptedException {
    	WebElement fromDateClick = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[2]/div/div[1]/div/div[1]/div/input"));  // Change this to the correct locator
  		fromDateClick.click();
  		fromDateClick.clear();
  		fromDateClick.sendKeys("01-02-2025");  
  		Thread.sleep(2000);
    }
    

//    @Test(priority = 5)
//    public void testLoadBills() throws InterruptedException {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement loadBill = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[2]/button[1]")));
//        loadBill.click();
//        Thread.sleep(4000);
//    }
//    @Test(priority = 6)
//    public void testSelectAllBills() throws InterruptedException {
//        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
//        WebElement select = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[3]/div/button")));
//        select.click();
//        Thread.sleep(4000);
//    }


    @Test(priority = 5)
    public void testTotalBillValues() throws InterruptedException {
        WebElement totalBillValue = driver.findElement(By.xpath("//*[@id=\"root\"]/div[2]/div/div/div/div/div/div[2]/button[8]"));
        totalBillValue.click();
        Thread.sleep(4000);
    }
    
    @Test(priority = 6)
    public void testTotalAmt() {
        try {
            // Find the elements that contain the total amount
            WebElement AmtElement1 = driver.findElement(By.xpath("//*[@id=\"cell-vineoAddSaleBillBottomAmt-6\"]"));
            WebElement AmtElement2 = driver.findElement(By.xpath("//*[@id=\"cell-vineoAddSaleBillBottomAmt-14\"]"));
            WebElement AmtElement3 = driver.findElement(By.xpath("//*[@id=\"cell-vineoAddSaleBillBottomAmt-22\"]"));

            // Get the text from each element
            String AmtText1 = AmtElement1.getText().trim();
            String AmtText2 = AmtElement2.getText().trim();
            String AmtText3 = AmtElement3.getText().trim();

            // Ensure that the text is numeric before parsing
            if (AmtText1.matches("\\d+") && AmtText2.matches("\\d+") && AmtText3.matches("\\d+")) {
                // Convert the text to integers
                int amt1 = Integer.parseInt(AmtText1);
                int amt2 = Integer.parseInt(AmtText2);
                int amt3 = Integer.parseInt(AmtText3);

                // Calculate the total amount
                int totalAmt = amt1 + amt2 + amt3;

                // Compare the total amount with 90702
                if (totalAmt == 90702) {
                    // Perform actions if the total amount matches 90702
                    System.out.println("The total amount matches 90702.");
                } else {
                    // Handle case when the total amount doesn't match
                    System.out.println("The total amount does not match 90702. Total: " + totalAmt);
                }
            } else {
                System.err.println("One or more amounts are not valid numbers.");
            }
        } catch (NoSuchElementException e) {
            System.err.println("One or more elements not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    
//    @Test(priority = 7)
//    public void testTotalAmt() {
//        try {
//            // Find the elements that contain the total amounts
//        	 WebElement AmtElement1 = driver.findElement(By.xpath("//*[@id=\"cell-vineoAddSaleBillBottomAmt-6\"]"));
//             WebElement AmtElement2 = driver.findElement(By.xpath("//*[@id=\"cell-vineoAddSaleBillBottomAmt-18\"]"));
//             WebElement AmtElement3 = driver.findElement(By.xpath("//*[@id=\"cell-vineoAddSaleBillBottomAmt-30\"]"));
//
//            WebElement AmtElement4 = driver.findElement(By.xpath("//*[@id=\"cell-vineoAddSaleBillBottomAmt-42\"]"));
//            WebElement AmtElement5 = driver.findElement(By.xpath("//*[@id=\"cell-vineoAddSaleBillBottomAmt-54\"]"));
//            WebElement AmtElement6 = driver.findElement(By.xpath("//*[@id=\"cell-vineoAddSaleBillBottomAmt-66\"]"));
//            WebElement AmtElement7 = driver.findElement(By.xpath("//*[@id=\"cell-vineoAddSaleBillBottomAmt-78\"]"));
//            WebElement AmtElement8 = driver.findElement(By.xpath("//*[@id=\"cell-vineoAddSaleBillBottomAmt-90\"]"));
//            WebElement AmtElement9 = driver.findElement(By.xpath("//*[@id=\"cell-vineoAddSaleBillBottomAmt-102\"]"));
//            WebElement AmtElement10 = driver.findElement(By.xpath("//*[@id=\"cell-vineoAddSaleBillBottomAmt-114\"]"));
//            WebElement AmtElement11 = driver.findElement(By.xpath("//*[@id=\"cell-vineoAddSaleBillBottomAmt-126\"]"));
//
//            // Get the text from each element
//            String AmtText1 = AmtElement1.getText().trim();
//            String AmtText2 = AmtElement2.getText().trim();
//            String AmtText3 = AmtElement3.getText().trim();
//            String AmtText4 = AmtElement4.getText().trim();
//            String AmtText5 = AmtElement5.getText().trim();
//            String AmtText6 = AmtElement6.getText().trim();
//            String AmtText7 = AmtElement7.getText().trim();
//            String AmtText8 = AmtElement8.getText().trim();
//            String AmtText9 = AmtElement9.getText().trim();
//            String AmtText10 = AmtElement10.getText().trim();
//            String AmtText11 = AmtElement11.getText().trim();
//
//            // Ensure that all texts are numeric before parsing
//            if (AmtText1.matches("\\d+") && AmtText2.matches("\\d+") && AmtText3.matches("\\d+") &&
//                AmtText4.matches("\\d+") && AmtText5.matches("\\d+") && AmtText6.matches("\\d+") &&
//                AmtText7.matches("\\d+") && AmtText8.matches("\\d+") && AmtText9.matches("\\d+") &&
//                AmtText10.matches("\\d+") && AmtText11.matches("\\d+")) {
//
//                // Convert the text to integers
//                int amt1 = Integer.parseInt(AmtText1);
//                int amt2 = Integer.parseInt(AmtText2);
//                int amt3 = Integer.parseInt(AmtText3);
//                int amt4 = Integer.parseInt(AmtText4);
//                int amt5 = Integer.parseInt(AmtText5);
//                int amt6 = Integer.parseInt(AmtText6);
//                int amt7 = Integer.parseInt(AmtText7);
//                int amt8 = Integer.parseInt(AmtText8);
//                int amt9 = Integer.parseInt(AmtText9);
//                int amt10 = Integer.parseInt(AmtText10);
//                int amt11 = Integer.parseInt(AmtText11);
//
//                // Calculate the total amount by summing all the amounts
//                int totalAmt = amt1 + amt2 + amt3 + amt4 + amt5 + amt6 + amt7 + amt8 + amt9 + amt10 + amt11;
//
//                // Compare the total amount with 90702
//                if (totalAmt == 90702) {
//                    // Perform actions if the total amount matches 90702
//                    System.out.println("The total amount matches 90702.");
//                } else {
//                    // Handle case when the total amount doesn't match
//                    System.out.println("The total amount does not match 90702. Total: " + totalAmt);
//                }
//            } else {
//                System.err.println("One or more amounts are not valid numbers.");
//            }
//        } catch (NoSuchElementException e) {
//            System.err.println("One or more elements not found.");
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }

    
    


    @AfterClass
    public void tearDown() {
        // Close the browser
        if (driver != null) {
            driver.quit();
        }
    }
}
