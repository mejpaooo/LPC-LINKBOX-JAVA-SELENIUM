package dev.selenium.test.Modules;

import dev.selenium.test.Customizations.JsonDatasetArray;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Logger;

import static dev.selenium.test.Customizations.screenshot.screenshot;

public class UserDetails {
    public static void Test_UserDetails(WebDriver driver, WebDriverWait wait,
                                        Map<String, String> userDetails,
                                        Map<String, String> login, Logger logger, String savePath, SoftAssert softAssert) throws InterruptedException, FileNotFoundException {


        logger.info("[START] of User Details");
        screenshot(driver, savePath, "User Details.png");

        //Variables to be used in multiple classes
        String userName = "", firstName = "", middleName = "", lastName = "", password = "1234";

        //Executing modules
        OpenModule(driver, wait, userDetails, logger, savePath, softAssert);    //Opening module
        AddNewUser(driver, wait, userDetails, login, logger, savePath, softAssert, userName, firstName, middleName, lastName, password);    //Adding New User
        //ViewUser is called by "AddUser"
        EditUser(driver, wait, userDetails, login, logger, savePath, softAssert, userName, firstName, middleName, lastName, password);    //Adding New User


        logger.info("[END] of User Details");

    }



    private static void OpenModule(WebDriver driver, WebDriverWait wait,
                                   Map<String, String> userDetails,
                                   Logger logger, String savePath, SoftAssert softAssert) throws InterruptedException, FileNotFoundException{

        logger.info("Opening User Details Module");

        //Clicking Menu Dropdown of User Management
        driver.findElement(By.xpath(userDetails.get("userManagementMenu"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(userDetails.get("userDetailsMenu"))))); // Waiting for "User Details" module to be present
        driver.findElement(By.xpath(userDetails.get("userDetailsMenu"))).click(); // Clicking User Details menu

        //Asserting Page Title
        softAssert.assertEquals(driver.getTitle(), "User Details");

        screenshot(driver, savePath, "User Details Module.png");
        logger.info("[PASS] Openend User Details Module");

    }


    private static void AddNewUser(WebDriver driver, WebDriverWait wait,
                                   Map<String, String> userDetails,
                                   Map<String, String> login, Logger logger, String savePath, SoftAssert softAssert,
                                   String userName, String firstName,
                                   String middleName, String lastName, String password) throws InterruptedException, FileNotFoundException {


        logger.info("Adding New User");

        Map<String, Object> userDetailsNames = JsonDatasetArray.parser("UserDetails");
        List<String> namesArray = (List<String>) userDetailsNames.get("names");
        Random random = new Random();
        //For opening new browser to login
        WebDriver NewDriver;
        String dupliUserName = "";


        //Clicking New Button to create new user
        driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails"))).click();

        //Asserting page title
        softAssert.assertEquals(driver.getTitle(), "New User");
        screenshot(driver, savePath, "User Details | Add New User.png");

        for(int i=0; i<5; i++){
            //Adding New User Info Details
            firstName = namesArray.get(random.nextInt(151));
            middleName = namesArray.get(random.nextInt(151));
            lastName = namesArray.get(random.nextInt(151));
            userName = firstName+lastName;
            List < WebElement> x;
            WebElement presenceOfLogInButton;

            Thread.sleep(2000);
            switch (i){
                //Add user with No Inputs
                case (0):
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_AddNewUser"))).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_AddUserDetails_ConfirmAddNewUser"))));  //Wait until confirm modal opens
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_ConfirmAddNewUser"))).click();

                    //No Inputs
                    x =driver.findElements(By.xpath(userDetails.get("lbl_AddUserDetails_RequiredNotif_UserName"))); //User Name
                    softAssert.assertEquals(x.size(), 1, "Add New User: UserName required notif not displayed");
                    x =driver.findElements(By.xpath(userDetails.get("lbl_AddUserDetails_RequiredNotif_FirstName"))); //First Name
                    softAssert.assertEquals(x.size(), 1, "Add New User: First Name required notif not displayed");
                    x =driver.findElements(By.xpath(userDetails.get("lbl_AddUserDetails_RequiredNotif_LastName"))); //Last Name
                    softAssert.assertEquals(x.size(), 1);
                    x =driver.findElements(By.xpath(userDetails.get("lbl_AddUserDetails_RequiredNotif_Password"))); //Password Name
                    softAssert.assertEquals(x.size(), 1);
                    x =driver.findElements(By.xpath(userDetails.get("lbl_AddUserDetails_RequiredNotif_ReTypePassword"))); //Retype Password
                    softAssert.assertEquals(x.size(), 1);

                    screenshot(driver, savePath, "User Details | Add user with No Inputs.png");
                    logger.info("[PASS] Unable to Add user with incomplete required fields | No input");
                    break;


                //Add user with incomplete optional fields
                case (1):
                    middleName = "";

                    //In preparation for case 3
                    dupliUserName = userName;

                    driver.get(driver.getCurrentUrl());
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("inp_AddUserDetails_UserName"))));  //Wait until confirm modal opens
                    //Inputting User Details
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_UserName"))).click();
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_UserName"))).sendKeys(userName);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_FirstName"))).sendKeys(firstName);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_LastName"))).sendKeys(lastName);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_Password"))).sendKeys(password);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_ConfirmPassword"))).sendKeys(password);
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_AddNewUser"))).click(); //Submitting user
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_AddUserDetails_ConfirmAddNewUser")))); //Waiting for confirm add user button to be clickable
                    screenshot(driver, savePath, "User Details | Add User with incomplete optional fields.png");
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_ConfirmAddNewUser"))).click(); //Submitting user


                    //Make new tab to login
                    NewDriver = new EdgeDriver();
                    NewDriver.get("http://192.168.2.32:8069");

                    //Waiting for login page to load
                    presenceOfLogInButton = NewDriver.findElement(By.id(login.get("btn_signIn")));
                    wait.until(ExpectedConditions.elementToBeClickable(presenceOfLogInButton));

                    NewDriver.findElement(By.id(login.get("username"))).sendKeys(userName);
                    NewDriver.findElement(By.id(login.get("password"))).sendKeys(password);
                    NewDriver.findElement(By.id(login.get("btn_signIn"))).click();
                    softAssert.assertEquals(NewDriver.getTitle(), "Home Page", "Page title is not as expected: ");
                    Thread.sleep(1000);
                    screenshot(driver, savePath, "Logged in with correct credentials_Dashboard.png");
                    NewDriver.quit();

                    logger.info("[PASS] Able to Add user with incomplete optional fields");

                    //Checking if Users Table list contains added user.
                    ViewUser(driver, wait, userDetails, login, logger, savePath, softAssert, userName, firstName, middleName, lastName, password);    //Adding New User
                    break;


                //Creating user with different "Password" and "Retype Password"
                case (2):
                    driver.navigate().refresh();
                    wait.until(ExpectedConditions.titleIs("User Details"));
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails"))).click();

                    //Inputting User Details with mismatched password and confirm password
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("inp_AddUserDetails_UserName"))));  //Wait until modal to enter user details opens
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_UserName"))).click();
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_UserName"))).sendKeys(userName);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_FirstName"))).sendKeys(firstName);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_MiddleName"))).sendKeys(middleName);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_LastName"))).sendKeys(lastName);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_Password"))).sendKeys(password);
                    //Deliberately Mismatching the Passwords
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_ConfirmPassword"))).sendKeys(password+userName);
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_AddNewUser"))).click(); //Submitting user
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_AddUserDetails_ConfirmAddNewUser")))); //Waiting for confirm add user button to be clickable
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_ConfirmAddNewUser"))).click(); //Submitting user

                    x = driver.findElements(By.xpath(userDetails.get("lbl_AddUserDetails_Notif_PasswordsDoNotMatch")));
                    //Asserting that a notification regarding a mismatched password is displayed.
                    softAssert.assertEquals(x.size(), 1, "No Passwords Do Not Match Notification");
                    screenshot(driver, savePath, "User Details | Add user with mismatched password.png");
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_Close_Notif_PasswordsDoNotMatch"))).click(); // Closing Passwords Do Not Match Notif

                    //Closing Add User Modal
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_Cancel"))).click();
                    x = driver.findElements(By.xpath(userDetails.get("btn_AddUserDetails_Cancel")));

                    //Asserting that the modal is closed after clicking cancel
                    softAssert.assertEquals(x.size(), 0, "Add User Modal is still Opened after clicking 'Cancel'");

                    logger.info("[PASS] Unable to Add user with mismatched passwords");
                    break;


                //Username already exists
                case (3):
//                    driver.navigate().refresh();
                    wait.until(ExpectedConditions.titleIs("User Details"));
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails"))).click();

                    //Inputting User Details with mismatched password and confirm password
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("inp_AddUserDetails_UserName"))));  //Wait until modal to enter user details opens
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_UserName"))).click();
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_UserName"))).sendKeys(dupliUserName);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_FirstName"))).sendKeys(firstName);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_MiddleName"))).sendKeys(middleName);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_LastName"))).sendKeys(lastName);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_Password"))).sendKeys(password);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_ConfirmPassword"))).sendKeys(password);
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_AddNewUser"))).click(); //Submitting user
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_AddUserDetails_ConfirmAddNewUser")))); //Waiting for confirm add user button to be clickable
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_ConfirmAddNewUser"))).click(); //Submitting user

                    x = driver.findElements(By.xpath(userDetails.get("lbl_AddUserDetails_Notif_UserNameAlreadyExists")));
                    //Asserting that a notification regarding a mismatched password is displayed.
                    softAssert.assertEquals(x.size(), 1, "No UserName Already Exists Notification");
                    screenshot(driver, savePath, "User Details | Add User with existing username.png");

                    // If true Close UserName Already Exists Notif
                    if (x.size() == 1){
                        driver.findElement(By.xpath(userDetails.get("btn_AssUserDetails_Close_Notif_UserNameAlreadyExists"))).click();
                    }

                    //Closing Add User Modal
                    x = driver.findElements(By.xpath(userDetails.get("btn_AddUserDetails_Cancel")));
                    if(x.size() == 1){
                        driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_Cancel"))).click();
                        x = driver.findElements(By.xpath(userDetails.get("btn_AddUserDetails_Cancel")));
                    }else {
                        driver.findElement(By.xpath("/html/body/div[2]/div/form/section/div/div/div/div[2]/div/div[9]/div/button")).click();
                        x = driver.findElements(By.xpath("/html/body/div[2]/div/form/section/div/div/div/div[2]/div/div[9]/div/button"));
                    }

                    x = driver.findElements(By.xpath(userDetails.get("btn_AddUserDetails_Cancel")));

                    //Asserting that the modal is closed after clicking cancel
                    softAssert.assertEquals(x.size(), 0, "Add User Modal is still Opened after clicking 'Cancel'");

                    logger.info("[PASS] Unable to Add user with mismatched passwords");
                    break;



                //Add user with complete user details
                case (4):
//                    driver.navigate().refresh();

                    wait.until(ExpectedConditions.titleIs("User Details"));
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails"))).click();

                    //Inputting User Details
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("inp_AddUserDetails_UserName"))));  //Wait until modal to enter user details opens
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_UserName"))).click();
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_UserName"))).sendKeys(userName);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_FirstName"))).sendKeys(firstName);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_MiddleName"))).sendKeys(middleName);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_LastName"))).sendKeys(lastName);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_Password"))).sendKeys(password);
                    driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_ConfirmPassword"))).sendKeys(password);
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_AddNewUser"))).click(); //Submitting user
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_AddUserDetails_ConfirmAddNewUser")))); //Waiting for confirm add user button to be clickable
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_ConfirmAddNewUser"))).click(); //Submitting user


                    //Make new tab to login
                    NewDriver = new EdgeDriver();
                    NewDriver.get("http://192.168.2.32:8069");

                    //Waiting for login page to load
                    presenceOfLogInButton = NewDriver.findElement(By.id(login.get("btn_signIn")));
                    wait.until(ExpectedConditions.elementToBeClickable(presenceOfLogInButton));

                    NewDriver.findElement(By.id(login.get("username"))).sendKeys(userName);
                    NewDriver.findElement(By.id(login.get("password"))).sendKeys(password);
                    NewDriver.findElement(By.id(login.get("btn_signIn"))).click();
                    softAssert.assertEquals(NewDriver.getTitle(), "Home Page", "Page title is not as expected: ");
                    Thread.sleep(1000);
                    screenshot(driver, savePath, "Logged in with correct credentials_Dashboard.png");
                    NewDriver.quit();

                    screenshot(driver, savePath, "User Details | Add User with complete details.png");
                    logger.info("[PASS] Able to Add user with complete fields");

                    //Calling function to assert added user details
                    ViewUser(driver, wait, userDetails, login, logger, savePath, softAssert, userName, firstName, middleName, lastName, password);    //Adding New User
                    break;


            }
        }



    } // Add new user


    private static void ViewUser(WebDriver driver, WebDriverWait wait,
                                   Map<String, String> userDetails,
                                   Map<String, String> login, Logger logger, String savePath, SoftAssert softAssert,
                                   String userName, String firstName,
                                   String middleName, String lastName, String password) throws InterruptedException, FileNotFoundException {


        logger.info("Checking if new added user is present on 'Users List'");

        List < WebElement> x;

        //Search newly UserName
        driver.findElement(By.xpath(userDetails.get("inp_SearchUser_Search"))).sendKeys(userName);
        x = driver.findElements(By.xpath(userDetails.get("lbl_SearchUser_UsersTable_UserName")+"[contains(text(),"+userName+")]")); //Asserts if username value is equivalent to added username
        softAssert.assertEquals(x.size(), 1, "Username: "+userName+" does not exist. "); //Asserts that added username exists on table
        softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_SearchUser_UsersTable_UserName"))).getText(), userName, "Search Users: Values Does Not Match. "); //Asserts that found username is equal to added username
        softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_SearchUser_UsersTable_ActiveStatus"))).getText(), "Active", "Active Status Not 'Active'"); //Asserts that found user status is active

        //For instance that the user has no middle name
        if (middleName.isEmpty()){
            softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_SearchUser_UsersTable_Name"))).getText(), firstName+" "+lastName, "Search Users: Values Does Not Match. "); //Asserts that found name is equal to added first, middle and last name
        }else{
            softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_SearchUser_UsersTable_Name"))).getText(), firstName+" "+middleName+" "+lastName, "Search Users: Values Does Not Match. "); //Asserts that found name is equal to added first, middle and last name
        }


        //Opening details of selected user
        driver.findElement(By.xpath(userDetails.get("btn_SearchUser_UsersTable_EditUser"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_ViewedUser_SaveChanges"))));
        Thread.sleep(1000);
        softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_UserName"))).getAttribute("value"), userName, "Added Username does not match. "); //Verifying user Name
        softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_FirstName"))).getAttribute("value"), firstName, "Added First Name does not match. "); //Verifying first Name
        softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_MiddleName"))).getAttribute("value"), middleName, "Added Middle Name does not match. "); //Verifying middle Name
        softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_LastName"))).getAttribute("value"), lastName, "Added Last Name does not match. "); //Verifying last Name
        softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_ActiveStatus"))).getAttribute("aria-checked"), "true", "Active Status not Checked.");

    }//End of ViewUser



    private static void EditUser(WebDriver driver, WebDriverWait wait,
                                 Map<String, String> userDetails,
                                 Map<String, String> login, Logger logger, String savePath, SoftAssert softAssert,
                                 String userName, String firstName,
                                 String middleName, String lastName, String password) throws InterruptedException, FileNotFoundException {









    }//End of Edit User






    }//end of Main Class
