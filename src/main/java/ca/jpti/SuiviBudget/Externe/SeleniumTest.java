package ca.jpti.SuiviBudget.Externe;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;

import java.util.concurrent.TimeUnit;

public class SeleniumTest {


    public static void main(String[] args) {

        System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");
        WebDriver driver = new ChromeDriver();

        String url = "https://accweb.mouv.desjardins.com/";

        driver.get(url);
//        driver.manage().window().maximize();
        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);


//        WebElement login = driver.findElement(By.id("codeUtilisateur"));
//        System.out.println("Clicking on the login element in the main page");
//        login.click();
//
//        driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

        WebElement identifiant = driver.findElement(By.id("codeUtilisateur"));
        WebElement motDePasse = driver.findElement(By.id("motDePasse"));
        WebElement btnLogin = driver.findElement(By.xpath("//*[@id=\"form-identifiant\"]/div[3]/button"));

        identifiant.clear();
        System.out.println("Saisie de l'identifiant");
        identifiant.sendKeys(System.getenv("desjardinsuser"));

        motDePasse.clear();
        System.out.println("Saisie du mot de passe");
        motDePasse.sendKeys(System.getenv("desjardinspassword"));

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
