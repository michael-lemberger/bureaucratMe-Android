package com.example.bureaucratme;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.seleniumhq.jetty9.server.Authentication;

import java.io.IOException;

public class Document {
    private String docId;
    private String date;
    private String entityId;
    private String docUrl;

    public Document(String docUrl){
        this.docUrl = docUrl;
    }

    /*
    This method gets user reference, extract his details and fill the form document using web driver.
    Currently work only with chrome driver.
     */
    public void fill(Users user) throws InterruptedException, IOException {

        System.setProperty("webdriver.chrome.driver", "res\\chromedriver.exe");
        WebDriver driver = new ChromeDriver();

        driver.get(docUrl);
        driver.manage().window().maximize();

        Thread.sleep(2000);
        driver.findElement(By.id("cn")).sendKeys(user.getCn());
        driver.findElement(By.id("givenName")).sendKeys(user.getGivenName());
        driver.findElement(By.id("sn")).sendKeys(user.getSn());
        driver.findElement(By.id("mail")).sendKeys(user.getMail());
        driver.findElement(By.id("mail_confirm")).sendKeys(user.getMail_confirm());
        driver.findElement(By.id("mobile")).sendKeys(user.getMobile());
        driver.findElement(By.id("mobile_confirm")).sendKeys(user.getMobile_confirm());
        driver.findElement(By.id("radioSMS")).click();
        driver.findElement(By.id("Identification1")).click();
        driver.findElement(By.id("Identification3")).click();
        driver.findElement(By.id("Identification2")).click();
        driver.findElement(By.id("idIssueDateYear")).sendKeys(user.getIdIssueDateYear());
        driver.findElement(By.id("idIssueDateMonth")).sendKeys(user.getIdIssueDateMonth());
        driver.findElement(By.id("idIssueDateDay")).sendKeys(user.getIdIssueDateDay());
        driver.findElement(By.id("password1")).sendKeys(user.getPassword1());
        driver.findElement(By.id("password2")).sendKeys(user.getPassword2());
        driver.findElement(By.id("radioDisagree")).click();
        driver.findElement(By.id("agreementConsentDiv")).click();
        Thread.sleep(2000);

        driver.close();
    }

    public String getDocId() {
        return docId;
    }

    public String getDate() {
        return date;
    }

    public String getEntityId() {
        return entityId;
    }

    public String getDocUrl() {
        return docUrl;
    }

    public void setDocId(String docId) {
        this.docId = docId;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setEntityId(String entityId) {
        this.entityId = entityId;
    }

    public void setDocUrl(String docUrl) {
        this.docUrl = docUrl;
    }

}
