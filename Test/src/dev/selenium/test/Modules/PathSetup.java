package dev.selenium.test.Modules;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static dev.selenium.test.Customizations.screenshot.screenshot;
public class PathSetup {
    public static void Test_PathSetup(WebDriver driver, WebDriverWait wait,
                                      Map<String, String> pathSetup,
                                      Logger logger, String savePath, SoftAssert softAssert) throws InterruptedException, FileNotFoundException {

        //Calling Modules
        OpenModule(driver, wait, pathSetup, logger, savePath, softAssert); //Opening Module

        List< WebElement> x;
    }


    private static void OpenModule (WebDriver driver, WebDriverWait wait,
                                    Map<String, String> pathSetup,
                                    Logger logger, String savePath, SoftAssert softAssert) throws InterruptedException, FileNotFoundException {



        logger.info("Opening User Details Module");

        //Clicking Menu Dropdown of User Management
        driver.findElement(By.xpath(pathSetup.get("btn_NavigationMenu_Configurations"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(pathSetup.get("btn_NavigationMenu_SubMenu_Setup"))))); // Waiting for "Path" module to be present
        driver.findElement(By.xpath(pathSetup.get("btn_NavigationMenu_SubMenu_Setup"))).click(); // Clicking Setup menu

        //Asserting Page Title
        softAssert.assertEquals(driver.getTitle(), "Setup", "Page Title Values Does Not Match");
        screenshot(driver, savePath, "Path Setup Module.png");

        //logging results
        String activeTab = String.valueOf(driver.findElement(By.xpath(pathSetup.get("btn_Setup_PathSetupTab"))).getAttribute("class"));
        if(driver.getTitle().equals("Setup") && activeTab.equals("active")){
            logger.info("[PASS] Opened Path Setup Module");
        }else{
            logger.info("[FAIL] Opened Path Setup Module");
        }
    }





} // End of Main Class