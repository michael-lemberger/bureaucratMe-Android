package com.example.bureaucratme;

import java.io.IOException;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class FormFilling {
    private Users user;
   private Document doc;



    public  void fill(String docPath) throws InterruptedException, IOException {

        System.setProperty("webdriver.chrome.driver", "res\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        driver.get("https://account.gov.il/sspr/public/newuser?forwardURL=https%3A%2F%2Fmy.gov.il%2Fsec&locale=iw&locale=he");
        driver.manage().window().maximize();

        Thread.sleep(2000);

        driver.findElement(By.id("cn")).sendKeys("203668116");
        driver.findElement(By.id("givenName")).sendKeys("לירון");
        driver.findElement(By.id("sn")).sendKeys("ארד");
        driver.findElement(By.id("mail")).sendKeys("lironarad1@gmail.com");
        driver.findElement(By.id("mail_confirm")).sendKeys("lironarad1@gmail.com");
        driver.findElement(By.id("mobile")).sendKeys("0534596473");
        driver.findElement(By.id("mobile_confirm")).sendKeys("0534596473");
        driver.findElement(By.id("radioSMS")).click();
        driver.findElement(By.id("Identification1")).click();
        driver.findElement(By.id("Identification3")).click();
        driver.findElement(By.id("Identification2")).click();
        driver.findElement(By.id("idIssueDateYear")).sendKeys("1992");
        driver.findElement(By.id("idIssueDateMonth")).sendKeys("10");
        driver.findElement(By.id("idIssueDateDay")).sendKeys("01");
        driver.findElement(By.id("password1")).sendKeys("1234!Abcd");
        driver.findElement(By.id("password2")).sendKeys("1234!Abcd");
        driver.findElement(By.id("radioDisagree")).click();
        driver.findElement(By.id("agreementConsentDiv")).click();

        Thread.sleep(2000);

        driver.close();
    }
}
