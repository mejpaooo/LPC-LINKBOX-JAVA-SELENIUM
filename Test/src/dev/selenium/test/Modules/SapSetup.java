package dev.selenium.test.Modules;

import dev.selenium.test.Customizations.JsonDatasetArray;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import static dev.selenium.test.Customizations.screenshot.screenshot;
public class SapSetup {

    public static void Test_SapSetup(WebDriver driver, WebDriverWait wait, Map<String, String> test, Logger logger, String savePath) throws InterruptedException {
        logger.info("[START] of Test Module 1");
        screenshot(driver, savePath, "Test Module 1.png");

        logger.info("[START] of SapSetup Test Automation");
        screenshot(driver, savePath, "Test Sapsetup.png");

        //Execute modules
        OpenModule(driver, softAssert, SapSetup, logger, wait, savePath);
        AddSAP(driver, wait, savePath, SapSetup, logger); //Opening Module
        UpdateSAP(driver, wait, savePath, SapSetup, logger); //Opening Module
        SearchSAP(driver, wait, savePath, SapSetup, logger); //Opening Module
        ASCandDESSAP(driver, savePath, SapSetup, logger); //Opening Module

        logger.info("[END] of User Details Automation");

        //Close the website
        driver.close();
        driver.quit();

    private static void OpenModule(WebDriver driver, SoftAssert softAssert,
                                   Map<String, String> SapSetup, Logger logger,
                                   WebDriverWait wait, String savePath) throws InterruptedException {

        logger.info("Opening SAP Setup Module");

        Thread.sleep(2000);
        wait.until(ExpectedConditions.titleIs("Setup"));
        wait.until(ExpectedConditions.presenceOfElementLocated(By.className("active")));
        driver.findElement(By.xpath(SapSetup.get("btn_Setup_SAPSetupTab"))).click();

        //Asserting Page Title
        softAssert.assertEquals(driver.getTitle(), "Setup", "Page Title Values Does Not Match");
        screenshot(driver, savePath, "Path Setup Module.png");

    }
    private static void AddSAP(WebDriver driver, WebDriverWait wait,
                               String savePath, Map<String, String> sapSetup,
                               Logger logger) throws FileNotFoundException, InterruptedException {
            Map<String, Object> Names = JsonDatasetArray.parser("SapSetup");
            List<String> namesArray = (List<String>) Names.get("Random_username");
            Random random = new Random();
            List<WebElement> x;

            String RemoteSapUserID = "admin";
            String RemoteSapPassword = "password";
            String logMessage, dupPathCode = "";


            for (int i = 0; i < 3; i++) {

                String nameVar = namesArray.get(random.nextInt(100));
                String IncSapCode = "TEST_" + random.nextInt(200) + "10";
                String SapCode = "SAP_" + nameVar + random.nextInt(200);
                String DbPort = "1" + random.nextInt(200) + "10";
                String SldServer = "TEST SLD SERVER";
                String ServerName = "TEST SERVER NAME " + nameVar + "." + random.nextInt(200);
                String Ipaddress = "192.168." + random.nextInt(200) + "." + random.nextInt(200);
                String Version = random.nextInt(200) + "";
                String LicPort = "1" + random.nextInt(200);
                String DBName = "TEST DATABASE " + nameVar;
                String DBUserID = "TEST DATABASE " + random.nextInt(200);
                String DBpassword = RemoteSapPassword;
                String SapUserID = RemoteSapUserID;
                String SapPassword = RemoteSapPassword;

                switch (i) {

                    //Adding with all complete fields
                    case 0:
                        //Opening Modal to create new SAP setup
                        Thread.sleep(2000);
                        driver.findElement(By.xpath(sapSetup.get("btn_NewSAPetup"))).click();
                        wait.until(ExpectedConditions.elementToBeClickable(By.id(sapSetup.get("modal_AddSAPSetup"))));
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SAPCode"))).sendKeys(SapCode);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_DBPort"))).sendKeys(DbPort);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SLDServer"))).sendKeys(SldServer);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SerName"))).sendKeys(ServerName);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_IpAdd"))).sendKeys(Ipaddress);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_Version"))).sendKeys(Version);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_LicPort"))).sendKeys(LicPort);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_DBName"))).sendKeys(DBName);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_DBuserID"))).sendKeys(DBUserID);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_DBPassword"))).sendKeys(SapUserID);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SAPuserID"))).sendKeys(DBpassword);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SAPpassword"))).sendKeys(SapPassword);
                        screenshot(driver, savePath, "SAP Setup Module Complete Field.png");
                        Thread.sleep(2000);
                        driver.findElement(By.xpath(sapSetup.get("btn_SaveNewSAPSetup"))).click();
                        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(sapSetup.get("modal_ConfirmSAPSave"))));
                        driver.findElement(By.xpath(sapSetup.get("btn_Confirm_NEWandUPDATE_SAPSetup"))).click();
                        break;

                    //Adding without required fields
                    case 1:
                        //Opening Modal to create new SAP setup
                        Thread.sleep(2000);
                        driver.findElement(By.xpath(sapSetup.get("btn_NewSAPetup"))).click();
                        wait.until(ExpectedConditions.elementToBeClickable(By.id(sapSetup.get("modal_AddSAPSetup"))));
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SAPCode"))).sendKeys(IncSapCode);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SAPCode"))).click();
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_DBPort"))).click();
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SLDServer"))).click();
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SerName"))).click();
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_IpAdd"))).click();
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_Version"))).click();
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_LicPort"))).click();
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_DBName"))).click();
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_DBuserID"))).click();
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_DBPassword"))).click();
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SAPuserID"))).click();
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SAPpassword"))).click();
                        screenshot(driver, savePath, "SAP Setup Module Required Field.png");
                        Thread.sleep(2000);
                        driver.findElement(By.xpath(sapSetup.get("btn_SaveNewSAPSetup"))).click();
                        driver.findElement(By.xpath(sapSetup.get("btn_CloseNewSAPSetup"))).click();
                        break;

                    //Adding without optional fields
                    case 2:
                        //Opening Modal to create new SAP setup
                        Thread.sleep(2000);
                        driver.findElement(By.xpath(sapSetup.get("btn_NewSAPetup"))).click();
                        wait.until(ExpectedConditions.elementToBeClickable(By.id(sapSetup.get("modal_AddSAPSetup"))));
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SAPCode"))).sendKeys(SapCode);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_DBPort"))).sendKeys(DbPort);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SLDServer"))).sendKeys(SldServer);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SerName"))).sendKeys(ServerName);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_IpAdd"))).sendKeys(Ipaddress);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_Version"))).click();
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_LicPort"))).sendKeys(LicPort);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_DBName"))).sendKeys(DBName);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_DBuserID"))).sendKeys(DBUserID);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_DBPassword"))).sendKeys(SapUserID);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SAPuserID"))).sendKeys(DBpassword);
                        driver.findElement(By.xpath(sapSetup.get("inp_AddUserDetails_SAPpassword"))).sendKeys(SapPassword);
                        screenshot(driver, savePath, "SAP Setup Module Optional Field.png");
                        Thread.sleep(2000);
                        driver.findElement(By.xpath(sapSetup.get("btn_SaveNewSAPSetup"))).click();
                        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(sapSetup.get("modal_ConfirmSAPSave"))));
                        driver.findElement(By.xpath(sapSetup.get("btn_Confirm_NEWandUPDATE_SAPSetup"))).click();
                        break;
                }
            }
        }
        private static void UpdateSAP(WebDriver driver, WebDriverWait wait, String savePath,
                                  Map<String, String> sapSetup, Logger logger) throws InterruptedException, FileNotFoundException {

        Map<String, Object> Namess = JsonDatasetArray.parser("SapSetup");
        List<String> namessArray = (List<String>) Namess.get("Random_username");
        Random randoms = new Random();
        List<WebElement> x;

        String RemoteUpdateSapUserID = "admin";
        String RemoteUpdateSapPassword = "password";
        String logMessage, dupPathCode = "";

        for (int a = 0; a < 3; a++) {

            String updnameVar = namessArray.get(randoms.nextInt(100));
            String updDbPort = "1" + randoms.nextInt(200) + "10";
            String updSldServer = "TEST SLD SERVER";
            String updServerName = "TEST SERVER NAME " + updnameVar + "." + randoms.nextInt(200);
            String updIpaddress = "192.168." + randoms.nextInt(200) + "." + randoms.nextInt(200);
            String updVersion = randoms.nextInt(200) + "";
            String updLicPort = "1" + randoms.nextInt(200);
            String updDBName = "TEST DATABASE " + updnameVar;
            String updDBUserID = "TEST DATABASE " + randoms.nextInt(200);
            String updDBpassword = RemoteUpdateSapPassword;
            String updSapUserID = RemoteUpdateSapUserID;
            String updSapPassword = RemoteUpdateSapPassword;

            switch (a) {
                //Update with all complete fields
                case 0:
                    //Opening Modal to update existing SAP setup
                    Thread.sleep(2000);
                    driver.findElement(By.xpath(sapSetup.get("btn_EditSAPSetup"))).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.id(sapSetup.get("modal_EditSAPSetup"))));
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_DBPort"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_DBPort"))).sendKeys(updDbPort);
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_SLDServer"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_SLDServer"))).sendKeys(updSldServer);
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_SerName"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_SerName"))).sendKeys(updServerName);
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_IpAdd"))).click();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_Version"))).click();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_LicPort"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_LicPort"))).sendKeys(updLicPort);
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_DBName"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_DBName"))).sendKeys(updDBName);
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_DBuserID"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_DBuserID"))).sendKeys(updDBUserID);
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_DBPassword"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_DBPassword"))).sendKeys(updSapUserID);
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_SAPuserID"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_SAPuserID"))).sendKeys(updDBpassword);
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_SAPpassword"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_SAPpassword"))).sendKeys(updSapPassword);
                    screenshot(driver, savePath, "SAP Setup Module update complete Field.png");
                    Thread.sleep(2000);
                    driver.findElement(By.xpath(sapSetup.get("btn_SaveUpdateSAPSetup"))).click();
                    driver.findElement(By.xpath(sapSetup.get("btn_Confirm_NEWandUPDATE_SAPSetup"))).click();
                    break;

                //Update with all incomplete fields
                case 1:
                    //Opening Modal to update existing SAP setup
                    Thread.sleep(2000);
                    driver.findElement(By.xpath(sapSetup.get("btn_EditSAPSetup2"))).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.id(sapSetup.get("modal_EditSAPSetup"))));
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_DBPort"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_SLDServer"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_SLDServer"))).sendKeys(updSldServer);
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_SerName"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_SerName"))).sendKeys(updServerName);
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_IpAdd"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_IpAdd"))).sendKeys(updIpaddress);
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_Version"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_DBName"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_DBName"))).sendKeys(updDBName);
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_DBuserID"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_DBuserID"))).sendKeys(updDBUserID);
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_DBPassword"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_DBPassword"))).sendKeys(updSapUserID);
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_SAPuserID"))).clear();
                    driver.findElement(By.xpath(sapSetup.get("inp_UpdUserDetails_SAPpassword"))).clear();
                    screenshot(driver, savePath, "SAP Setup Module update incomplete Field.png");
                    Thread.sleep(2000);
                    driver.findElement(By.xpath(sapSetup.get("btn_SaveUpdateSAPSetup"))).click();
                    driver.findElement(By.xpath(sapSetup.get("btn_CloseUpdateSAPSetup"))).click();
                    break;
        }
    }

    private static void SearchSAP(WebDriver driver, WebDriverWait wait, String savePath,
                                  Map<String, String> sapSetup, Logger logger) throws InterruptedException {
        driver.findElement(By.xpath(sapSetup.get("Search_ClickkeywordsSAP"))).click();
        driver.findElement(By.xpath(sapSetup.get("Search_ClickkeywordsSAP"))).sendKeys(sapSetup.get("Search_InputkeywordsSAP"));
        screenshot(driver, savePath, "SAP Search keywords.png");
        logger.info("Search Keywords");
        Thread.sleep(2000);
    }

    private static void ASCandDESSAP(WebDriver driver,
                                     String savePath, Map<String, String> sapSetup, Logger logger) throws InterruptedException{
        driver.findElement(By.xpath(sapSetup.get("SAPAscending"))).click();
        screenshot(driver, savePath, "SAP Ascending.png");
        logger.info("Ascending Order Test Automation");
        Thread.sleep(2000);

        driver.findElement(By.xpath(sapSetup.get("SAPDescending"))).click();
        screenshot(driver, savePath, "SAP Descending.png");
        logger.info("Descending Order Test Automation");
        Thread.sleep(2000);

    }
}

