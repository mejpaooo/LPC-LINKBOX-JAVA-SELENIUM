package dev.selenium.test.Modules;

import dev.selenium.test.Customizations.JsonDatasetArray;
import org.openqa.selenium.*;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.asserts.SoftAssert;

import java.io.FileNotFoundException;
import java.sql.Driver;
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
        String userName = "", firstName = "", middleName = "", lastName = "", password = userDetails.get("default_password");

        //Executing modules
        OpenModule(driver, wait, userDetails, logger, savePath, softAssert);    //Opening module
        userName = AddNewUser(driver, wait, userDetails, login, logger, savePath, softAssert, userName, firstName, middleName, lastName, password);    //Adding New User
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
        softAssert.assertEquals(driver.getTitle(), "User Details", "[ERROR] User Details Open Module - Tab Title - ");

        screenshot(driver, savePath, "User Details Module.png");
        logger.info("[PASS] Openend User Details Module");

    }


    private static String AddNewUser(WebDriver driver, WebDriverWait wait,
                                   Map<String, String> userDetails,
                                   Map<String, String> login, Logger logger, String savePath, SoftAssert softAssert,
                                   String userName, String firstName,
                                   String middleName, String lastName, String password) throws InterruptedException, FileNotFoundException {


        logger.info("[START] Adding New User");

        Map<String, Object> userDetailsNames = JsonDatasetArray.parser("UserDetails");
        List<String> namesArray = (List<String>) userDetailsNames.get("names");
        Random random = new Random();
        //For opening new browser to login
        WebDriver NewDriver;
        String dupliUserName = "";


        //Clicking New Button to create new user
        driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails"))).click();

        //Asserting page title
        softAssert.assertEquals(driver.getTitle(), "New User", "[ERROR] Add User Details - Tab Title - ");
        screenshot(driver, savePath, "User Details_Add New User.png");

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

                    List <WebElement> t1, t2, t3, t4, t5;

                    //No Inputs
                    t1 =driver.findElements(By.xpath(userDetails.get("lbl_AddUserDetails_RequiredNotif_UserName"))); //User Name
                    softAssert.assertEquals(t1.size(), 1, "[MISSING] Add New User - UserName required notification -");
                    t2 =driver.findElements(By.xpath(userDetails.get("lbl_AddUserDetails_RequiredNotif_FirstName"))); //First Name
                    softAssert.assertEquals(t2.size(), 1, "[MISSING] Add New User: First Name required notif");
                    t3 =driver.findElements(By.xpath(userDetails.get("lbl_AddUserDetails_RequiredNotif_LastName"))); //Last Name
                    softAssert.assertEquals(t3.size(), 1,"[MISSING] Add New User - Last Name required notif");
                    t4 =driver.findElements(By.xpath(userDetails.get("lbl_AddUserDetails_RequiredNotif_Password"))); //Password Name
                    softAssert.assertEquals(t4.size(), 1, "[MISSING] Add New User - Password required notif");
                    t5 =driver.findElements(By.xpath(userDetails.get("lbl_AddUserDetails_RequiredNotif_ReTypePassword"))); //Retype Password
                    softAssert.assertEquals(t5.size(), 1, "[MISSING] Add New User - Retype password required notif");

                    screenshot(driver, savePath, "User Details_Add user with No Inputs.png");
                    if(t1.size() == 1 && t2.size() == 1 && t3.size() == 1 && t4.size() == 1 && t5.size() == 1){
                        logger.info("[PASS] Unable to Add user with incomplete required fields_No input");
                    } else {
                        logger.info("[FAIL] Unable to Add user with incomplete required fields_No input");
                    }
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
                    screenshot(driver, savePath, "User Details_Add User with incomplete optional fields.png");
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_AddNewUser"))).click(); //Submitting user
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_AddUserDetails_ConfirmAddNewUser")))); //Waiting for confirm add user button to be clickable
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_ConfirmAddNewUser"))).click(); //Submitting user


                    //Checking if username already exists
                    x = driver.findElements(By.xpath(userDetails.get("lbl_AddUserDetails_Notif_UserNameAlreadyExists")));
                    if(x.size() == 1){
                        i--; //Repearing the process with differente user credentials
                        break;
                    }


                    //Make new tab to login
                    NewDriver = new EdgeDriver();
                    NewDriver.get("http://192.168.2.32:8069");

                    //Waiting for login page to load
                    presenceOfLogInButton = NewDriver.findElement(By.id(login.get("btn_signIn")));
                    wait.until(ExpectedConditions.elementToBeClickable(presenceOfLogInButton));

                    NewDriver.findElement(By.id(login.get("username"))).sendKeys(userName);
                    NewDriver.findElement(By.id(login.get("password"))).sendKeys(password);
                    NewDriver.findElement(By.id(login.get("btn_signIn"))).click();
                    softAssert.assertEquals(NewDriver.getTitle(), "Home Page", "[ERROR] Add User Details - Page title is not as expected -");
                    Thread.sleep(1000);
                    screenshot(driver, savePath, "Logged in with correct credentials_Dashboard (User with no middle name).png");

                    if(NewDriver.getTitle().equals("Home Page")){
                        logger.info("[PASS] Able to Add user with incomplete optional fields");
                    } else{
                        logger.info("[FAIL] Able to Add user with incomplete optional fields");
                    }
                    NewDriver.quit();


                    //Checking if Users Table list contains added user.
                    ViewUser(driver, wait, userDetails, login, logger, savePath, softAssert, userName, firstName, middleName, lastName, password, "Creating user with incomplete optional fields");    //Adding New User
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
                    softAssert.assertEquals(x.size(), 1, "[MISSING] Add User Details - No Passwords Does Not Match Notification");
                    screenshot(driver, savePath, "User Details_Add user with mismatched password.png");
                    if(x.size() == 1){
                        logger.info("[PASS] Unable to Add user with mismatched passwords");
                    } else {
                        logger.info("[FAIL] Unable to Add user with mismatched passwords");
                    }

                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_Close_Notif_PasswordsDoNotMatch"))).click(); // Closing Passwords Do Not Match Notif

                    //Closing Add User Modal
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_Cancel"))).click();
                    x = driver.findElements(By.xpath(userDetails.get("btn_AddUserDetails_Cancel")));

                    //Asserting that the modal is closed after clicking cancel
                    softAssert.assertEquals(x.size(), 0, "[ERROR] Add User Details - Add User Modal is still Opened after clicking 'Cancel' - ");

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
                    softAssert.assertEquals(x.size(), 1, "[MISSING] Add User Detail - No UserName Already Exists Notification - ");
                    screenshot(driver, savePath, "User Details_Add User with existing username.png");

                    // If username already exists error notif exists, Close UserName Already Exists Notif
                    if (x.size() == 1){
                        logger.info("[PASS] Unable to Add user with mismatched passwords");
                        driver.findElement(By.xpath(userDetails.get("btn_AssUserDetails_Close_Notif_UserNameAlreadyExists"))).click();
                    } else {
                        logger.info("[FAIL] Unable to Add user with mismatched passwords");
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
                    softAssert.assertEquals(x.size(), 0, "[ERROR] Add User Details - Add User Modal is still Opened after clicking 'Cancel'");

                    break;



                //Add user with complete user details
                case (4):

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


                    //Checking if password eye is functional
                    String type1 = driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_Password"))).getAttribute("type");
                    String type2 = driver.findElement(By.xpath(userDetails.get("inp_AddUserDetails_ConfirmPassword"))).getAttribute("type");

                    for(int j=0; j<2; j++){
                        //when password field is viewable as text
                        if(!type1.equals("password") && !type2.equals("password")){
                            softAssert.assertNotEquals("password", type1, "[ERROR] Add User Details - View Password field type - ");
                            softAssert.assertNotEquals("password", type2, "[ERROR] Add User Details - View Retyped Password field type -");
                            logger.info("[PASS] Add User Details_Able to DECRYPT password");
                        }else{  //when password field is hidden, assert that string password is not equal ******
                            softAssert.assertEquals("password", type1, "[ERROR] Add User Details - View Password field type - ");
                            softAssert.assertEquals("password", type2, "[ERROR] Add User Details - View Retyped Password field type - ");
                            logger.info("[PASS] Add User Details_Able to ENCRYPT password");
                        }

                        //Click password eyes
                        driver.findElement(By.id(userDetails.get("btn_AddUserDetails_ShowPassword"))).click();
                        driver.findElement(By.id(userDetails.get("btn_AddUserDetails_ShowRetypedPassword"))).click();
                    }

                    //Show password
                    driver.findElement(By.id(userDetails.get("btn_AddUserDetails_ShowPassword"))).click();
                    driver.findElement(By.id(userDetails.get("btn_AddUserDetails_ShowRetypedPassword"))).click();
                    screenshot(driver, savePath, "Saving new user with complete details.png");

                    screenshot(driver, savePath, "User Details_Add User with complete details.png");
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_AddNewUser"))).click(); //Submitting user
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_AddUserDetails_ConfirmAddNewUser")))); //Waiting for confirm add user button to be clickable
                    driver.findElement(By.xpath(userDetails.get("btn_AddUserDetails_ConfirmAddNewUser"))).click(); //Submitting user
                    logger.info("[PASS] Able to Add user with complete fields");


                    //Checking if username already exists
                    x = driver.findElements(By.xpath(userDetails.get("lbl_AddUserDetails_Notif_UserNameAlreadyExists")));
                    if(x.size() == 1){
                        i--; //Repearing the process with differente user credentials
                        break;
                    }


                    //Make new tab to login
                    NewDriver = new EdgeDriver();
                    NewDriver.get("http://192.168.2.32:8069");

                    //Waiting for login page to load
                    presenceOfLogInButton = NewDriver.findElement(By.id(login.get("btn_signIn")));
                    wait.until(ExpectedConditions.elementToBeClickable(presenceOfLogInButton));

                    NewDriver.findElement(By.id(login.get("username"))).sendKeys(userName);
                    NewDriver.findElement(By.id(login.get("password"))).sendKeys(password);
                    NewDriver.findElement(By.id(login.get("btn_signIn"))).click();
                    softAssert.assertEquals(NewDriver.getTitle(), "Home Page", "[ERROR] Add User Detail - Logging in created user - Page title is not as expected - ");
                    Thread.sleep(1000);
                    screenshot(driver, savePath, "Logged in with correct credentials_Dashboard (User with complete Details).png");
                    NewDriver.quit();


                    //Closing Modal
//                    x = driver.findElements(By.xpath(userDetails.get("btn_ViewedUser_CloseViewUser")));
//                    if(x.size() == 1){
//                        driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_CloseViewUser"))).click();
//                    }

                    //Calling function to assert added user details
                    ViewUser(driver, wait, userDetails, login, logger, savePath, softAssert, userName, firstName, middleName, lastName, password, "Creating user with complete fields");    //Adding New User
                    break;

            }

        }

        logger.info("[END] Adding New User");
        return userName;
    } // End of Add new user


    private static void ViewUser(WebDriver driver, WebDriverWait wait,
                                   Map<String, String> userDetails,
                                   Map<String, String> login, Logger logger, String savePath, SoftAssert softAssert,
                                   String userName, String firstName,
                                   String middleName, String lastName, String password, String testCase) throws InterruptedException, FileNotFoundException {


        logger.info("[START] Checking if new added user is present on 'Users List'");

        List < WebElement> x;

        //Search newly UserName
        driver.findElement(By.xpath(userDetails.get("inp_SearchUser_Search"))).sendKeys(userName);
        x = driver.findElements(By.xpath(userDetails.get("lbl_SearchUser_UsersTable_UserName")+"[contains(text(),"+userName+")]")); //Asserts if username value is equivalent to added username
        softAssert.assertEquals(x.size(), 1, "[ERROR] View User - "+testCase+" - Username: "+userName+" does not exist. "); //Asserts that added username exists on table
        softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_SearchUser_UsersTable_UserName")+"[contains(text(),"+userName+")]")).getText(), userName, "[ERROR] View User - "+testCase+" - Username Values Does Not Match -"); //Asserts that found username is equal to added username
        softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_SearchUser_UsersTable_ActiveStatus"))).getText(), "Active", "[ERROR] View User - "+testCase+" - Active Status Not 'Active'"); //Asserts that found user status is active
        screenshot(driver, savePath, "Searching User on User Details Table.png");

        //For instance that the user has no middle name
        if (middleName.isEmpty()){
            softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_SearchUser_UsersTable_Name"))).getText(), firstName+" "+lastName, "[ERROR] View USer - "+testCase+" - Full Name on users table values Does Not Match - "); //Asserts that found name is equal to added first, middle and last name
        }else{
            softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_SearchUser_UsersTable_Name"))).getText(), firstName+" "+middleName+" "+lastName, "[ERROR] View USer - "+testCase+" - Full Name on users table values Does Not Match - "); //Asserts that found name is equal to added first, middle and last name
        }


        //Opening details of selected user
        driver.findElement(By.xpath(userDetails.get("btn_SearchUser_UsersTable_EditUser"))).click();
        wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_ViewedUser_SaveChanges"))));
        Thread.sleep(1000);
        screenshot(driver, savePath, "Selected User Details.png");
        softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_UserName"))).getAttribute("value"), userName, "[ERROR] View User - "+testCase+" - Added Username does not match. "); //Verifying user Name
        softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_FirstName"))).getAttribute("value"), firstName, "[ERROR] View User - "+testCase+" - Added First Name does not match. "); //Verifying first Name
        softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_MiddleName"))).getAttribute("value"), middleName, "[ERROR] View User - "+testCase+" - Added Middle Name does not match. "); //Verifying middle Name
        softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_LastName"))).getAttribute("value"), lastName, "[ERROR] View User - "+testCase+" - Added Last Name does not match. "); //Verifying last Name
        softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_ActiveStatus"))).getAttribute("aria-checked"), "true", "[ERROR] View User - "+testCase+" - Active Status not Checked."); //Verifying that active status is checked


        logger.info("[END] Checking if new added user is present on 'Users List'");

    }//End of ViewUser



    private static void EditUser(WebDriver driver, WebDriverWait wait,
                                 Map<String, String> userDetails,
                                 Map<String, String> login, Logger logger, String savePath, SoftAssert softAssert,
                                 String userName, String firstName,
                                 String middleName, String lastName, String password) throws InterruptedException, FileNotFoundException {


        logger.info("[START] Editing User");
        screenshot(driver, savePath, "Edit User Details_Before User Edit.png");

        Map<String, Object> userDetailsNames = JsonDatasetArray.parser("UserDetails");
        List<String> namesArray = (List<String>) userDetailsNames.get("names");
        Random random = new Random();
        List <WebElement> x;
        String y;


        String NewfirstName = namesArray.get(random.nextInt(151));
        String NewmiddleName = namesArray.get(random.nextInt(151));
        String NewlastName = namesArray.get(random.nextInt(151));
        String Newpassword = userDetails.get("new_password");

        //Asserting that username is not editable
        y = driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_UserName"))).getAttribute("disabled");
        softAssert.assertEquals(y,"true", "[ERROR]: Edit User Details - User Name should not be Editable - ");

        for(int i=0; i<5; i++){
            switch (i){

                //Editing User by deleting first name and last name
                case (0):
                    //Opening existing user
//                    driver.findElement(By.xpath(userDetails.get("inp_SearchUser_Search"))).sendKeys(userName);
//                    driver.findElement(By.xpath(userDetails.get("btn_SearchUser_UsersTable_EditUser"))).click();

                    //Clearing First Name and Last Name fields
                    driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_FirstName"))).clear();
                    driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_LastName"))).clear();

                    //Opening "Update Password" Fields
                    driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_UpdatePassword"))).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_ViewedUser_CurrentPassword_PassEye"))));

                    //Submitting Changes, to check if required notifications are displayed
                    driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_SaveChanges"))).click();
                    Thread.sleep(2000);

                    List <WebElement> t1, t2, t3, t4, t5;

                    //Asserting that first name required notification is displayed
                    t1 = driver.findElements(By.xpath(userDetails.get("lbl_EditUserDetails_FirstName_RequiredNotif")));
                    softAssert.assertEquals(t1.size(), 1, "[MISSING] - Edit User Details - No First Name Required Notification");

                    //Asserting that last name required notification is displayed
                    t2 = driver.findElements(By.xpath(userDetails.get("lbl_EditUserDetails_LastName_RequiredNotif")));
                    softAssert.assertEquals(t2.size(), 1, "[MISSING] - Edit User Details - No Last Name Required Notification");


                    //Asserting that current password required notification is displayed
                    t3 = driver.findElements(By.xpath(userDetails.get("lbl_EditUserDetails_CurrentPassword_RequiredNotif")));
                    softAssert.assertEquals(t3.size(), 1, "[MISSING] - Edit User Details - No Current Password Required Notification");

                    //Asserting that new password required notification is displayed
                    t4 = driver.findElements(By.xpath(userDetails.get("lbl_EditUserDetails_NewPassword_RequiredNotif")));
                    softAssert.assertEquals(t4.size(), 1, "[MISSING] - Edit User Details - No New Password Required Notification");

                    //Asserting that retype password required notification is displayed
                    t5 = driver.findElements(By.xpath(userDetails.get("lbl_EditUserDetails_RetypePassword_RequiredNotif")));
                    softAssert.assertEquals(t5.size(), 1, "[MISSING] - Edit User Details - No Retype Password Required Notification");

                    screenshot(driver, savePath, "Edit User Details without First and Last Name.png");
                    if(t1.size() == 1 && t2.size() == 1 && t3.size() == 1 && t4.size() == 1 && t5.size() == 1){
                        logger.info("[PASS] Unable to update user without Required Fields");
                    }else{
                        logger.info("[FAIL] Unable to update user without FRequired Fields");
                    }

                    //Closing modal
                    driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_CloseViewUser"))).click();
                    break;

                //Updating password with mismatched passwords
                case(1):
                    //Opening existing user
                    driver.findElement(By.xpath(userDetails.get("inp_SearchUser_Search"))).clear();
                    driver.findElement(By.xpath(userDetails.get("inp_SearchUser_Search"))).sendKeys(userName);
                    driver.findElement(By.xpath(userDetails.get("btn_SearchUser_UsersTable_EditUser"))).click();

                    //Opening update password fields
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_ViewedUser_UpdatePassword"))));
                    driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_UpdatePassword"))).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_ViewedUser_CurrentPassword_PassEye"))));

                    //Inputting password
                    driver.findElement(By.xpath(userDetails.get("inp_ViewedUser_CurrentPassword"))).sendKeys(password);
                    driver.findElement(By.xpath(userDetails.get("inp_ViewedUser_NewPassword"))).sendKeys(Newpassword);
                    driver.findElement(By.xpath(userDetails.get("inp_ViewedUser_ConfirmNewPassword"))).sendKeys("wrong password"); // deliberately mismatching the password


                    //Checking if password eye is functional
                    String type1 = driver.findElement(By.xpath(userDetails.get("inp_ViewedUser_CurrentPassword"))).getAttribute("type");
                    String type2 = driver.findElement(By.xpath(userDetails.get("inp_ViewedUser_NewPassword"))).getAttribute("type");
                    String type3 = driver.findElement(By.xpath(userDetails.get("inp_ViewedUser_ConfirmNewPassword"))).getAttribute("type");


                    for(int j=0; j<2; j++){
                        //when password field is viewable as text
                        if(!type1.equals("password") && !type2.equals("password") && !type3.equals("password")){
                            softAssert.assertNotEquals("password", type1, "[ERROR] Edit User Detail - View Current Password field type - ");
                            softAssert.assertNotEquals("password", type2, "[ERROR] Edit User Detail - View New Password field type - ");
                            softAssert.assertNotEquals("password", type3, "[ERROR] Edit User Detail - View Confirm New Password field type - ");
                            logger.info("Add User Details_Able to DECRYPT password");
                        }else{  //when password field is hidden, assert that string password is not equal ******
                            softAssert.assertEquals("password", type1, "[ERROR] Edit User Detail - View Current Password field type - ");
                            softAssert.assertEquals("password", type2, "[ERROR] Edit User Detail - View New Password field type - ");
                            softAssert.assertEquals("password", type3, "[ERROR] Edit User Detail - View Confirm New Password field type - ");
                            logger.info("Add User Details_Able to ENCRYPT password");
                        }

                        //Show password
                        driver.findElement(By.id(userDetails.get("btn_EditUserDetails_ShowCurrentPassword"))).click();
                        driver.findElement(By.id(userDetails.get("btn_EditUserDetails_ShowNewPassword"))).click();
                        driver.findElement(By.id(userDetails.get("btn_EditUserDetails_ShowNewRetypedPassword"))).click();

                    }


                    //Submitting Changes, to check if required notifications are displayed
                    driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_SaveChanges"))).click();
                    Thread.sleep(2000);
                    driver.findElement(By.xpath(userDetails.get("btn_EditUserDetails_ConfirmSave"))).click();
                    screenshot(driver, savePath, "Edit User Details_Mismatched New and Retype Password.png");

                    x = driver.findElements(By.xpath(userDetails.get("btn_EditUserDetails_ConfirmSave")));
                    if(x.size() == 1){
                        driver.findElement(By.xpath(userDetails.get("btn_EditUserDetails_ConfirmSave"))).click(); //Closing the Error Notif Modal
                        logger.info("[PASS] Unable to add new user with Mismatched 'New' and 'Retype' Password");
                    } else{
                        logger.info("[FAIL] Unable to add new user with Mismatched 'New' and 'Retype' Password");
                    }


                    //Checking if cancel works on the "confirm update" modal
                    driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_SaveChanges"))).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_EditUserDetails_Cancel_ConfirmSave"))));
                    driver.findElement(By.xpath(userDetails.get("btn_EditUserDetails_Cancel_ConfirmSave"))).click();
                    Thread.sleep(2000);
                    x = driver.findElements(By.xpath(userDetails.get("btn_EditUserDetails_Cancel_ConfirmSave")));
                    softAssert.assertEquals(x.size(), 0, "[ERROR] - Edit User Details - Cancel button on confirming update of fields is not functional - ");


                    //Making sure that the error notif is closed
                    x = driver.findElements(By.xpath(userDetails.get("btn_EditUserDetails_UponSaving_ErrorNotification")));
                    if(x.size() == 1){
                        driver.findElement(By.xpath(userDetails.get("btn_EditUserDetails_UponSaving_ErrorNotification"))).click();
                    }


                    //Closing modal
                    x = driver.findElements(By.xpath(userDetails.get("btn_ViewedUser_CloseViewUser")));
                    if(x.size() == 1){
                        driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_CloseViewUser"))).click();
                    }

                    break;

                //Updating user with incorrect CURRENT password
                case (2):
                    //Opening existing user
                    driver.findElement(By.xpath(userDetails.get("inp_SearchUser_Search"))).clear();
                    driver.findElement(By.xpath(userDetails.get("inp_SearchUser_Search"))).sendKeys(userName);
                    driver.findElement(By.xpath(userDetails.get("btn_SearchUser_UsersTable_EditUser"))).click();


                    //Opening update password fields
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_ViewedUser_UpdatePassword"))));
                    driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_UpdatePassword"))).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_ViewedUser_CurrentPassword_PassEye"))));

                    //Inputting password
                    driver.findElement(By.xpath(userDetails.get("inp_ViewedUser_CurrentPassword"))).sendKeys("wrong password");// deliberately inputting the wrong password
                    driver.findElement(By.xpath(userDetails.get("inp_ViewedUser_NewPassword"))).sendKeys(Newpassword);
                    driver.findElement(By.xpath(userDetails.get("inp_ViewedUser_ConfirmNewPassword"))).sendKeys(Newpassword);

                    //Submitting Changes, to check if error notification is displayed
                    driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_SaveChanges"))).click();
                    Thread.sleep(2000);
                    driver.findElement(By.xpath(userDetails.get("btn_EditUserDetails_ConfirmSave"))).click(); // Confirming Save
                    x = driver.findElements(By.xpath(userDetails.get("btn_EditUserDetails_UponSaving_ErrorNotification")));
                    softAssert.assertEquals(x.size(), 1, "[MISSING] Edit User Detail - Incorrect Current Password Notification - ");

                    screenshot(driver, savePath, "Edit User Details_Incorrect Current Password.png");
                    if(x.size() == 1){
                        logger.info("[PASS] Unable to update user with incorrect 'Current' Password");
                        screenshot(driver, savePath, "Edit User Details_Incorrect Current Password.png");
                        //Closing Error Notification
                        driver.findElement(By.xpath(userDetails.get("btn_EditUserDetails_UponSaving_ErrorNotification"))).click();
                    } else{
                        logger.info("[FAIL] Unable to update user with incorrect 'Current' Password");
                    }



                    //Closing modal
                    x =driver.findElements(By.xpath(userDetails.get("btn_ViewedUser_CloseViewUser")));
                    if (x.size() == 1){
                        driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_CloseViewUser"))).click();
                    }


                    break;



                //Updating user with proper and correct data then check if still able to login
                case (3):
                    //Opening existing user
                    driver.findElement(By.xpath(userDetails.get("inp_SearchUser_Search"))).clear();
                    driver.findElement(By.xpath(userDetails.get("inp_SearchUser_Search"))).sendKeys(userName);
                    driver.findElement(By.xpath(userDetails.get("btn_SearchUser_UsersTable_EditUser"))).click();

                    //Opening update password fields
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_ViewedUser_UpdatePassword"))));
                    driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_UpdatePassword"))).click();
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_ViewedUser_CurrentPassword_PassEye"))));

                    //Clearing First Name and Last Name fields
                    driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_FirstName"))).clear();
                    driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_MiddleName"))).clear();
                    driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_LastName"))).clear();

                    //Inputting Updated Names
                    driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_FirstName"))).sendKeys(NewfirstName);
                    driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_MiddleName"))).sendKeys(NewmiddleName);
                    driver.findElement(By.xpath(userDetails.get("lbl_ViewedUser_LastName"))).sendKeys(NewlastName);

                    //Inputting password
                    driver.findElement(By.xpath(userDetails.get("inp_ViewedUser_CurrentPassword"))).sendKeys(userDetails.get("default_password"));
                    driver.findElement(By.xpath(userDetails.get("inp_ViewedUser_NewPassword"))).sendKeys(Newpassword);
                    driver.findElement(By.xpath(userDetails.get("inp_ViewedUser_ConfirmNewPassword"))).sendKeys(Newpassword);

                    //Submitting Changes, to check if error notification is displayed
                    driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_SaveChanges"))).click();
                    Thread.sleep(2000);
                    driver.findElement(By.xpath(userDetails.get("btn_EditUserDetails_ConfirmSave"))).click(); // Confirming Save


                    //Checking if user details are updated successfully
                    Thread.sleep(3000);
                    ViewUser(driver, wait, userDetails, login, logger, savePath, softAssert, userName, NewfirstName, NewmiddleName, NewlastName, Newpassword, "Updating user with proper data");


                    //Make new tab to login
                    WebDriver NewDriver;
                    NewDriver = new EdgeDriver();
                    NewDriver.get("http://192.168.2.32:8069");
                    WebElement presenceOfLogInButton;


                    //Waiting for login page to load
                    presenceOfLogInButton = NewDriver.findElement(By.id(login.get("btn_signIn")));
                    wait.until(ExpectedConditions.elementToBeClickable(presenceOfLogInButton));

                    NewDriver.findElement(By.id(login.get("username"))).sendKeys(userName);
                    NewDriver.findElement(By.id(login.get("password"))).sendKeys(Newpassword);
                    NewDriver.findElement(By.id(login.get("btn_signIn"))).click();
                    softAssert.assertEquals(NewDriver.getTitle(), "Home Page", "[ERROR] Edit User Detail - Unable to login with new credentials -");
                    Thread.sleep(1000);

                    if(NewDriver.getTitle().equals("Home Page")){
                        logger.info("[PASS] Able to update user details and still able to login");
                    } else{
                        logger.info("[FAIL] Able to update user details and still able to login");
                    }

                    screenshot(NewDriver, savePath, "Logged in with correct credentials_Dashboard (Updated user details).png");
                    NewDriver.quit();

                    //Closing modal
                    x =driver.findElements(By.xpath(userDetails.get("btn_ViewedUser_CloseViewUser")));
                    if (x.size() == 1){
                        driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_CloseViewUser"))).click();
                    }

                    break;


                //Logging in with "Inactive" User Status
                case (4):
                    //Opening existing user
                    driver.navigate().refresh();
                    driver.findElement(By.xpath(userDetails.get("inp_SearchUser_Search"))).sendKeys(userName);
                    driver.findElement(By.xpath(userDetails.get("btn_SearchUser_UsersTable_EditUser"))).click();

                    //Unchecking Active Status
                    Thread.sleep(1000);
                    driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_ActiveStatus"))).click();

                    //Submitting Changes, to check if error notification is displayed
                    driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_SaveChanges"))).click();
                    Thread.sleep(2000);
                    driver.findElement(By.xpath(userDetails.get("btn_EditUserDetails_ConfirmSave"))).click(); // Confirming Save

                    //Asserting that user status on table is now "Inactive"
                    Thread.sleep(3000);
                    driver.findElement(By.xpath(userDetails.get("inp_SearchUser_Search"))).clear();
                    driver.findElement(By.xpath(userDetails.get("inp_SearchUser_Search"))).sendKeys(userName);
                    softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_SearchUser_UsersTable_ActiveStatus"))).getText(), "Inactive", "[ERROR] Edit User Details - Logging in with 'Inactive' User Status - ");

                    NewDriver = new EdgeDriver();
                    NewDriver.get("http://192.168.2.32:8069");

                    //Waiting for login page to load
                    presenceOfLogInButton = NewDriver.findElement(By.id(login.get("btn_signIn")));
                    wait.until(ExpectedConditions.elementToBeClickable(presenceOfLogInButton));
                    NewDriver.findElement(By.id(login.get("username"))).clear();
                    NewDriver.findElement(By.id(login.get("username"))).sendKeys(userName);
                    NewDriver.findElement(By.id(login.get("password"))).sendKeys(Newpassword);
                    NewDriver.findElement(By.id(login.get("btn_signIn"))).click();
                    Thread.sleep(1000);

                    if(NewDriver.getTitle().equals("Home Page")){
                        logger.info("[FAIL] Able to Login user with inactive user status");
                    } else{
                        logger.info("[PASS] Able to Login user with inactive user status");
                    }

                    screenshot(NewDriver, savePath, "Edit User Details - Logging in with Updated Inactive Status");
                    softAssert.assertNotEquals(NewDriver.getTitle(), "Home Page", "[ERROR] Edit User Details - Able to login with Inactive Status - ");


                    /*
                        Changing user back to "Active" Status then relogging in
                    */


                    //Updating user back to Active and relogging in
                    driver.findElement(By.xpath(userDetails.get("btn_SearchUser_UsersTable_EditUser"))).click();

                    //Unchecking Active Status
                    wait.until(ExpectedConditions.elementToBeClickable(By.xpath(userDetails.get("btn_ViewedUser_ActiveStatus"))));
                    driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_ActiveStatus"))).click();

                    //Submitting Changes, to check if error notification is displayed
                    driver.findElement(By.xpath(userDetails.get("btn_ViewedUser_SaveChanges"))).click();
                    Thread.sleep(2000);
                    driver.findElement(By.xpath(userDetails.get("btn_EditUserDetails_ConfirmSave"))).click(); // Confirming Save

                    //Asserting that user status on table is now "Active"
                    Thread.sleep(3000);
                    driver.findElement(By.xpath(userDetails.get("inp_SearchUser_Search"))).clear();
                    driver.findElement(By.xpath(userDetails.get("inp_SearchUser_Search"))).sendKeys(userName);
                    softAssert.assertEquals(driver.findElement(By.xpath(userDetails.get("lbl_SearchUser_UsersTable_ActiveStatus"))).getText(), "Active", "[ERROR] Edit User Details - Logging in with Inactive User Status updated to Active Status - ");

                    presenceOfLogInButton = NewDriver.findElement(By.id(login.get("btn_signIn")));
                    wait.until(ExpectedConditions.elementToBeClickable(presenceOfLogInButton));
                    NewDriver.findElement(By.id(login.get("username"))).clear();
                    NewDriver.findElement(By.id(login.get("username"))).sendKeys(userName);
                    NewDriver.findElement(By.id(login.get("password"))).sendKeys(Newpassword);
                    NewDriver.findElement(By.id(login.get("btn_signIn"))).click();
                    Thread.sleep(1000);

                    if(NewDriver.getTitle().equals("Home Page")){
                        logger.info("[PASS] Able to Login user with updated status from inactive to active -");
                    } else{
                        logger.info("[FAIL] Unable to Login user with updated status from inactive to active -");
                    }

                    screenshot(NewDriver, savePath, "Edit User Details - Logging in with Updated Active Status");
                    softAssert.assertEquals(NewDriver.getTitle(), "Home Page", "[ERROR] Edit User Details - Able to login upon updating user with Inactive Status back to Active Status");
                    NewDriver.quit();

                    break;

            }
        }

        logger.info("[END] Editing User");


    }//End of Edit User























}//end of Main Class
