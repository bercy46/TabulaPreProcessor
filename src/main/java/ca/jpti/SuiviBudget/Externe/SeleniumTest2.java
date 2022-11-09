package ca.jpti.SuiviBudget.Externe;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

public class SeleniumTest2 {


    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        WebDriver driver = new ChromeDriver();

        String url = "https://easyweb.td.com//";

        driver.get(url);
//        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);


//        WebElement login = driver.findElement(By.id("codeUtilisateur"));
//        System.out.println("Clicking on the login element in the main page");
//        login.click();
//
//        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

        WebElement identifiant = driver.findElement(By.id("username"));
        WebElement motDePasse = driver.findElement(By.id("uapPassword"));
        WebElement btnLogin = driver.findElement(By.xpath("/html/body/app-root/main/core-login-template/div/section[1]/div/div[2]/div/div/div/core-login-form/form/div[3]/div/div/button"));

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        identifiant.clear();
        System.out.println("Saisie de l'identifiant");
        identifiant.sendKeys("4724090153491913");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        motDePasse.clear();
        System.out.println("Saisie du mot de passe");
        motDePasse.sendKeys("TDJPt05032");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Click du bouton login");
        btnLogin.click();

        String title = "Welcome - LambdaTest";

        String actualTitle = driver.getTitle();

        System.out.println("Verifying the page title has started");
        Assert.assertEquals(actualTitle, title, "Page title doesnt match");

        System.out.println("The page title has been successfully verified");

        System.out.println("User logged in successfully");

        driver.quit();
    }
}
