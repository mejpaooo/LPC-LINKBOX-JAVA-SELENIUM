package dev.selenium.test.Modules;

import dev.selenium.test.Customizations.JsonDatasetArray;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import javax.annotation.concurrent.ThreadSafe;
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
            String pathCode = "Path_"+nameVar+"_"+namesArray.get(random.nextInt(151));
            String localPath = "D:"+nameVar+"TEST";
            String backupPath = "D:/"+nameVar+"/TEST/BACKUP";
            String errorPath = "D:/"+nameVar+"/TEST/ERROR";
            String remotePath = "C:/"+nameVar+"/TEST";
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
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_PathCode"))).sendKeys(pathCode, Keys.TAB);
                    Thread.sleep(1000);
                    x = driver.findElements(By.xpath(pathSetup.get("btn_AddPathSetup_Confirm_ErrorNotification")));
                    if (!x.isEmpty()) {
                        i--;
                        driver.navigate().refresh();
                        break;
                    }
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


                    //Calling View Path
                    //Only passing values that the path has inputted
                    ViewPath(driver, wait, pathSetup,logger, savePath, softAssert, pathCode,
                            localPath, "", "", "", "",
                            "", "", "", "", "Adding with optional fields - ");
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
                    Thread.sleep(1000);
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_LocalPath"))).click();
                    //Submitting Form
//                    driver.findElement(By.id(pathSetup.get("btn_AddPathSetup_SaveNewPathSetup"))).click();

                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(pathSetup.get("btn_AddPathSetup_Confirm_ErrorNotification"))));
                    x = driver.findElements(By.xpath(pathSetup.get("btn_AddPathSetup_Confirm_ErrorNotification")));
                    softAssert.assertEquals(x.size(), 1, "[MISSING] Add Path Setup - Path Code is Already Taken Error");
                    screenshot(driver, savePath, "Add Path Setup - Adding path setup with existing path code.png");

                    logMessage = x.isEmpty()  ? "FAIL" : "PASS";
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
                    driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_PathCode"))).sendKeys(pathCode, Keys.TAB);
                    //If Path Code Taken is taken, repetition of Case 3 will be made
                        Thread.sleep(1000);
                        x = driver.findElements(By.xpath(pathSetup.get("btn_AddPathSetup_Confirm_ErrorNotification")));
                        if (!x.isEmpty()) {
                            i--;
                            driver.navigate().refresh();
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


                    //Checking if view password button is functional
                    for(int j=0; j<2; j++){
                        String type1 = driver.findElement(By.id(pathSetup.get("inp_AddPathSetup_RemoteUserPassword"))).getAttribute("type");

                        if(!type1.equals("password")){
                            logger.info("[PASS] Add Path Setup - Able to DECRYPT Password");
                            softAssert.assertNotEquals("password", type1, "[ERROR] Add Path Setup - Able to DECRYPT Password");

                        }else{
                            logger.info("[PASS] Add Path Setup - Able to ENCRYPT password");
                            softAssert.assertEquals("password", type1, "[ERROR] Add Path Setup - Able to ENCRYPT Password");
                        }
                        driver.findElement(By.id(pathSetup.get("btn_AddPathSetup_RemoteUserPassword_ViewPassword"))).click();
                    }


                    //Submitting form to save new path setup
                    screenshot(driver, savePath, "Add Path Setup - Complete Details");
                    driver.findElement(By.id(pathSetup.get("btn_AddPathSetup_SaveNewPathSetup"))).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(pathSetup.get("btn_AddPathSetup_Confirm_SaveNewPathSetup"))));
                    driver.findElement(By.xpath(pathSetup.get("btn_AddPathSetup_Confirm_SaveNewPathSetup"))).click();

                    x = driver.findElements(By.xpath(pathSetup.get("btn_AddPathSetup_Confirm_SaveNewPathSetup")));
                    logMessage =x.isEmpty()  ? "PASS" : "FAIL";
                    logger.info("["+logMessage+"] Saved New Path Setup - Complete Fields");


                    //Calling View Path
                    ViewPath(driver, wait, pathSetup,logger, savePath, softAssert, pathCode,
                            localPath, backupPath, errorPath, remotePath, remoteServer,
                            remoteIPAddress, remotePort, remoteUserID, remoteUserPassword, "Adding with complete fields - ");

                    break;


            }
        }


    }

    private static void ViewPath(WebDriver driver, WebDriverWait wait,
                                  Map<String, String> pathSetup,
                                  Logger logger, String savePath, SoftAssert softAssert,
                                  String pathCode, String localPath,
                                  String backupPath, String errorPath,
                                  String remotePath, String remoteServer,
                                  String remoteIPAddress, String remotePort,
                                  String remoteUserID, String remoteUserPassword, String TestCaseDescription) throws InterruptedException, FileNotFoundException {

        logger.info("[START] Validating Path Details for "+TestCaseDescription);
        List< WebElement> x;

        //Searching for Path Code in Table
        Thread.sleep(800);
        driver.findElement(By.xpath(pathSetup.get("inp_TablePathSetup_SearchPath"))).clear();
        driver.findElement(By.xpath(pathSetup.get("inp_TablePathSetup_SearchPath"))).sendKeys(pathCode); //To ensure that code will search path


        //Asserting Table Values
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(pathSetup.get("lbl_TablePathSetup_PathCode")+"[contains(text(),"+pathCode+")]")));
        softAssert.assertEquals(driver.findElement(By.xpath(pathSetup.get("lbl_TablePathSetup_PathCode"))).getText(), pathCode, "[ERROR] Path Setup Table - Different Path Code Value - Test Case: "+TestCaseDescription);
        softAssert.assertEquals(driver.findElement(By.xpath(pathSetup.get("lbl_TablePathSetup_LocalPath"))).getText(), localPath, "[ERROR] Path Setup Table - Different Local Path Value - Test Case: "+TestCaseDescription);
        softAssert.assertEquals(driver.findElement(By.xpath(pathSetup.get("lbl_TablePathSetup_BackupPath"))).getText(), backupPath, "[ERROR] Path Setup Table - Different Back up Path Value - Test Case: "+TestCaseDescription);
        softAssert.assertEquals(driver.findElement(By.xpath(pathSetup.get("lbl_TablePathSetup_ErrorPath"))).getText(), errorPath, "[ERROR] Path Setup Table - Different Error Path Value - Test Case: "+TestCaseDescription);
        softAssert.assertEquals(driver.findElement(By.xpath(pathSetup.get("lbl_TablePathSetup_ActiveStatus"))).getText(), "Active", "[ERROR] Path Setup Table - Different Error Path Value - Test Case: "+TestCaseDescription);


        //Opening Path Code
        driver.findElement(By.xpath(pathSetup.get("btn_TablePathSetup_ViewPath_1"))).click();

        //Asserting Complete Path Setup Details
        wait.until(ExpectedConditions.elementToBeClickable(By.id(pathSetup.get("lbl_EditPathSetup_PathCode"))));

        Thread.sleep(800);
        softAssert.assertEquals(driver.findElement(By.id(pathSetup.get("lbl_EditPathSetup_PathCode"))).getAttribute("value"), pathCode, "[ERROR] View Path Setup - Path Code Values Do Not Match - Test Case: "+TestCaseDescription);
        softAssert.assertEquals(driver.findElement(By.id(pathSetup.get("lbl_EditPathSetup_LocalPath"))).getAttribute("value"), localPath, "[ERROR] View Path Setup - Local Path Values Do Not Match - Test Case: "+TestCaseDescription);
        softAssert.assertEquals(driver.findElement(By.id(pathSetup.get("lbl_EditPathSetup_BackupPath"))).getAttribute("value"), backupPath, "[ERROR] View Path Setup - Backup Path Values Do Not Match - Test Case: "+TestCaseDescription);
        softAssert.assertEquals(driver.findElement(By.id(pathSetup.get("lbl_EditPathSetup_ErrorPath"))).getAttribute("value"), errorPath, "[ERROR] View Path Setup - Error Path Values Do Not Match - Test Case: "+TestCaseDescription);
        softAssert.assertEquals(driver.findElement(By.id(pathSetup.get("lbl_EditPathSetup_RemotePath"))).getAttribute("value"), remotePath, "[ERROR] View Path Setup - Remote Path Values Do Not Match - Test Case: "+TestCaseDescription);
        softAssert.assertEquals(driver.findElement(By.id(pathSetup.get("lbl_EditPathSetup_RemoteServer"))).getAttribute("value"), remoteServer, "[ERROR] View Path Setup - Remote Server Values Do Not Match - Test Case: "+TestCaseDescription);
        softAssert.assertEquals(driver.findElement(By.id(pathSetup.get("lbl_EditPathSetup_RemoteIPAddress"))).getAttribute("value"), remoteIPAddress, "[ERROR] View Path Setup - Remote IP Address Values Do Not Match - Test Case: "+TestCaseDescription);
        softAssert.assertEquals(driver.findElement(By.id(pathSetup.get("lbl_EditPathSetup_RemotePort"))).getAttribute("value"), remotePort, "[ERROR] View Path Setup - Remote Port Values Do Not Match - Test Case: "+TestCaseDescription);
        softAssert.assertEquals(driver.findElement(By.id(pathSetup.get("lbl_EditPathSetup_RemoteUserID"))).getAttribute("value"), remoteUserID, "[ERROR] View Path Setup - Remote User ID Values Do Not Match - Test Case: "+TestCaseDescription);
        softAssert.assertEquals(driver.findElement(By.id(pathSetup.get("lbl_EditPathSetup_RemotePassword"))).getAttribute("value"), remoteUserPassword, "[ERROR] View Path Setup - Remote User Password Values Do Not Match - Test Case: "+TestCaseDescription);
        softAssert.assertEquals(driver.findElement(By.xpath(pathSetup.get("btn_EditPathSetup_ActiveStatus"))).getAttribute("aria-checked"), "true", "[ERROR] View Path Setup - Active Status Not Checked - Test Case: "+TestCaseDescription);

        logger.info("[END] Validating Path Details for"+TestCaseDescription);
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