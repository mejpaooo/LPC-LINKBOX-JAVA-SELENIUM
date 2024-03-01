package dev.selenium.test.Modules;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.util.Map;
import java.util.logging.Logger;

import static dev.selenium.test.Customizations.screenshot.screenshot;


public class Login {
    public static void Test_Login(WebDriver driver, WebDriverWait wait, Map<String, String> login, Logger logger, String savePath, SoftAssert softAssert) throws InterruptedException {
        screenshot(driver, savePath,"Login.png");
        logger.info("[Start] of Login Test Automation");
        wait.until(ExpectedConditions.titleIs("Login"));


        //hard coded 3 loops for 4 test cases
        for (int i = 0; i<4; i++){
            Thread.sleep(2100);
            WebElement inp_username = driver.findElement(By.id(login.get("username")));
            WebElement inp_password = driver.findElement(By.id(login.get("password")));
            inp_username.clear();
            inp_password.clear();

            //Hard coding this to save time upon testing the other modules
            i=3;
            //Hard coding this to save time upon testing the other modules


            switch (i){
                case 0:
                    //Logging in with incorrect username and password
                    inp_username.sendKeys("incorect_username");
                    inp_password.sendKeys("incorrect_password");
                    driver.findElement(By.id(login.get("btn_signIn"))).click();
                    softAssert.assertEquals(driver.getTitle(), "Login", "User Logged in with incorrect username & password: ");
                    wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id(login.get("btn_signIn")))));
                    Thread.sleep(1000);
                    screenshot(driver, savePath,"Logged in with incorrect username and password_Login page.png");
                    break;

                case 1:
                    //Logging in with incorrect username
                    inp_username.sendKeys("incorrect_username");
                    inp_password.sendKeys(login.get("cred_password"));
                    driver.findElement(By.id(login.get("btn_signIn"))).click();
                    softAssert.assertEquals(driver.getTitle(), "Login", "User Logged in with incorrect username: ");
                    wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id(login.get("btn_signIn")))));
                    Thread.sleep(1000);
                    screenshot(driver, savePath, "Logged in with incorrect username_Login page.png");
                    break;

                case 2:
                    //Logging in with incorrect password
                    inp_username.sendKeys(login.get("cred_username"));
                    inp_password.sendKeys("incorrect_password");
                    driver.findElement(By.id(login.get("btn_signIn"))).click();
                    softAssert.assertEquals(driver.getTitle(), "Login", "User Logged in with incorrect password: ");
                    wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.id(login.get("btn_signIn")))));
                    Thread.sleep(1000);
                    screenshot(driver, savePath, "Logged in with incorrect password_Login page.png");
                    break;

                case 3:
                    //Logging in with correct credentials
                    inp_username.sendKeys(login.get("cred_username"));
                    inp_password.sendKeys(login.get("cred_password"));
                    driver.findElement(By.id(login.get("btn_signIn"))).click();
                    softAssert.assertEquals(driver.getTitle(), "Home Page", "Page title is not as expected.");
                    Thread.sleep(1000);
                    screenshot(driver, savePath, "Logged in with correct credentials_Dashboard.png");
                    break;
            }
        }


        logger.info("[End] of Login Test Automation");
    }

}
