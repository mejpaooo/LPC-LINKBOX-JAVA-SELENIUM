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
public class PathSetup {
    public static void Test_PathSetup(WebDriver driver, WebDriverWait wait,
                                      Map<String, String> pathSetup,
                                      Map<String, String> userDetails,
                                      Logger logger, String savePath, SoftAssert softAssert) throws InterruptedException, FileNotFoundException {

        //Calling Modules
        OpenModule(driver, wait, pathSetup, logger, savePath, softAssert); //Opening Module
        AddPath(driver, wait, pathSetup, userDetails,logger, savePath, softAssert); //Opening Module


    }


    private static void OpenModule (WebDriver driver, WebDriverWait wait,
                                    Map<String, String> pathSetup,
                                    Logger logger, String savePath, SoftAssert softAssert) throws InterruptedException, FileNotFoundException {


        logger.info("Opening User Details Module");

        //Clicking Menu Dropdown of Configuration
        Thread.sleep(2000);
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

    private static void AddPath (WebDriver driver, WebDriverWait wait,
                                 Map<String, String> pathSetup,
                                 Map<String, String> userDetails,
                                 Logger logger, String savePath, SoftAssert softAssert) throws InterruptedException, FileNotFoundException {


        Map<String, Object> Names = JsonDatasetArray.parser("UserDetails");
        List<String> namesArray = (List<String>) Names.get("names");
        Random random = new Random();
        List< WebElement> x;

        String remoteUserID = "admin";
        String remoteUserPassword = "password";
        String logMessage, dupPathCode = "";


        for(int i=0; i<4; i++){
            String nameVar = namesArray.get(random.nextInt(151));
            String pathCode = "Path_"+nameVar;
            String localPath = "D:/"+nameVar+"/TEST";
            String backupPath = "D:/"+nameVar+"/TEST/BACKUP";
            String errorPath = "D:/"+nameVar+"/TEST/ERROR";
            String remotePath = ":C/"+nameVar+"/TEST";
            String remoteServer = "192.168."+random.nextInt(200)+"."+random.nextInt(200);
            String remoteIPAddress = remoteServer;
            Integer port = random.nextInt(9000)+1111;
            String remotePort = Integer.toString(port);

            switch (i){

                //Adding without required fields
                case 0:
                    //Opening Modal to create new path
                    driver.findElement(By.xpath(pathSetup.get("btn_New_PathSetup"))).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.id(pathSetup.get("btn_AddPathSetup_SaveNewPathSetup"))));
                    driver.findElement(By.id(pathSetup.get("btn_AddPathSetup_SaveNewPathSetup"))).click();

                    List <WebElement> t1, t2;
                    t1 = driver.findElements(By.xpath(pathSetup.get("lbl_AddPathSetup_PathCode_RequiredNotif")));
                    t2 = driver.findElements(By.xpath(pathSetup.get("lbl_AddPathSetup_LocalPath_RequiredNotif")));
                    softAssert.assertEquals(t1.size(), 1, "[MISSING] Add Path Setup_Path Code Required Notification");
                    softAssert.assertEquals(t2.size(), 1, "[MISSING] Add Path Setup_Local Path Required Notification");
                    screenshot(driver, savePath, "Path Setup_Adding New Path without required Fields.png");

                    logMessage = t1.size() == 1 && t2.size() == 1 ? "PASS": "FAIL";
                    logger.info("["+logMessage+"] Unable to add path setup without required fields");

                    //Closing Modal
                    driver.findElement(By.xpath(pathSetup.get("btn_AddPathSetup_CloseModal"))).click();
                    break;


                //Adding without optional fields
                case 1:
                    //For Case 2 Preparation of same path code
                    dupPathCode = pathCode;

                    //Opening Modal to create new path
                    driver.findElement(By.xpath(pathSetup.get("btn_New_PathSetup"))).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.id(pathSetup.get("btn_AddPathSetup_SaveNewPathSetup"))));


                    //Inputting Path Code and Local Path
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_PathCode"))).sendKeys(pathCode);
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_LocalPath"))).sendKeys(localPath);

                    //Submitting Form
                    driver.findElement(By.id(pathSetup.get("btn_AddPathSetup_SaveNewPathSetup"))).click();

                    //Checking if cancel button works
                    driver.findElement(By.xpath(pathSetup.get("btn_AddPathSetup_Cancel_SaveNewPathSetup"))).click();
                    Thread.sleep(500);
                    x = driver.findElements(By.xpath(pathSetup.get("btn_AddPathSetup_Cancel_SaveNewPathSetup")));
                    softAssert.assertEquals(x.size(),0,"Add Path Setup - Error: Cancel Button on Confirming Saving of New Path Setup");

                    logMessage = x.isEmpty() ? "PASS" : "FAIL";
                    logger.info("["+logMessage+"] Cancel Button on Confirming Saving of New Path Setup");


                    //Submitting form to save new path setup
                    driver.findElement(By.id(pathSetup.get("btn_AddPathSetup_SaveNewPathSetup"))).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(pathSetup.get("btn_AddPathSetup_Confirm_SaveNewPathSetup"))));
                    driver.findElement(By.xpath(pathSetup.get("btn_AddPathSetup_Confirm_SaveNewPathSetup"))).click();

                    Thread.sleep(500);
                    x = driver.findElements(By.xpath(pathSetup.get("btn_AddPathSetup_Confirm_SaveNewPathSetup")));
                    logMessage =x.isEmpty()  ? "PASS" : "FAIL";
                    logger.info("["+logMessage+"] Saved New Path Setup - Required Field Only");


                    //Calling View User
                    /*-----------------------     VIEW USER FUNCTION    ----------------------- */
                    break;


                //Adding with existing PathCode
                case 2:
                    //Opening Modal to create new path
                    driver.navigate().refresh();
                    driver.findElement(By.xpath(pathSetup.get("btn_New_PathSetup"))).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.id(pathSetup.get("btn_AddPathSetup_SaveNewPathSetup"))));

                    //Inputting Path Code and Local Path
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_PathCode"))).sendKeys(dupPathCode);

                    //Notif is diplayed immediately upon out of focus
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_LocalPath"))).click();
                    //Submitting Form
//                    driver.findElement(By.id(pathSetup.get("btn_AddPathSetup_SaveNewPathSetup"))).click();

                    x = driver.findElements(By.xpath(pathSetup.get("btn_AddPathSetup_Confirm_ErrorNotification")));
                    softAssert.assertEquals(x.size(), 1, "[MISSING] Add Path Setup - Path Code is Already Taken Error");
                    screenshot(driver, savePath, "Add Path Setup - Adding path setup with existing path code.png");

                    logMessage = x.isEmpty()  ? "PASS" : "FAIL";
                    logger.info("["+logMessage+"] Add Path Setup - Unable to add with existing Path Code");

                    //Closing Modal
                    driver.navigate().refresh(); //refreshing due to notif error inconsistency... maybe

                    break;


                //Adding with complete fields
                case 3:
                    //Opening Modal to create new path
                    driver.findElement(By.xpath(pathSetup.get("btn_New_PathSetup"))).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.id(pathSetup.get("btn_AddPathSetup_SaveNewPathSetup"))));


                    //Inputting Path Code and Local Path
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_PathCode"))).sendKeys(pathCode);
                        //If Path Code Taken is taken, repetition of Case 3 will be made
                        x = driver.findElements(By.xpath(pathSetup.get("btn_AddPathSetup_Confirm_ErrorNotification")));
                        if (x.isEmpty()) {
                            i--;
                            break;
                        }
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_LocalPath"))).sendKeys(localPath);
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_BackupPath"))).sendKeys(backupPath);
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_ErrorPath"))).sendKeys(errorPath);
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_RemotePath"))).sendKeys(remotePath);
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_RemoteServer"))).sendKeys(remoteServer);
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_RemoteIPAddress"))).sendKeys(remoteIPAddress);
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_RemotePort"))).sendKeys(remotePort);
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_RemoteUserID"))).sendKeys(remoteUserID);
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_RemoteUserPassword"))).sendKeys(remoteUserPassword);


                    //Submitting form to save new path setup
                    driver.findElement(By.id(pathSetup.get("btn_AddPathSetup_SaveNewPathSetup"))).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(pathSetup.get("btn_AddPathSetup_Confirm_SaveNewPathSetup"))));
                    driver.findElement(By.xpath(pathSetup.get("btn_AddPathSetup_Confirm_SaveNewPathSetup"))).click();

                    x = driver.findElements(By.xpath(pathSetup.get("btn_AddPathSetup_Confirm_SaveNewPathSetup")));
                    logMessage =x.isEmpty()  ? "PASS" : "FAIL";
                    logger.info("["+logMessage+"] Saved New Path Setup - Complete Fields");


                    //Calling View User
                    /*-----------------------     VIEW USER FUNCTION    ----------------------- */

                    break;


            }
        }


    }

    private static void ViewPath (WebDriver driver, WebDriverWait wait,
                                  Map<String, String> pathSetup,
                                  Logger logger, String savePath, SoftAssert softAssert) throws InterruptedException, FileNotFoundException {

        List< WebElement> x;

    }


    private static void EditPath (WebDriver driver, WebDriverWait wait,
                                 Map<String, String> pathSetup,
                                 Logger logger, String savePath, SoftAssert softAssert) throws InterruptedException, FileNotFoundException {

        List< WebElement> x;

        //Searching Path Code
        driver.findElement(By.xpath(pathSetup.get("inp_TablePathSetup_SearchPath"))).sendKeys();

        //Selecting path setup
        x = driver.findElements(By.xpath(pathSetup.get("btn_TablePathSetup_ViewPath_1")));
        if(x.size() == 1){
            driver.findElement(By.xpath(pathSetup.get("btn_TablePathSetup_ViewPath_1"))).click(); //if there is only one path setup displayed on table
        }else{
            driver.findElement(By.xpath(pathSetup.get("btn_TablePathSetup_ViewPath_2"))).click(); //if there are multiple path setup displayed on table
        }
    }



} // End of Main Class