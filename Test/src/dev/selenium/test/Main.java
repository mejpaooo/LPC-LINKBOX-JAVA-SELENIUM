// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
package dev.selenium.test;

//imports for selenium automation

import com.google.gson.Gson;
import dev.selenium.test.Customizations.CustomLogger;
import dev.selenium.test.Customizations.JsonDataset;
import dev.selenium.test.Customizations.SavePath;
import dev.selenium.test.Sprint.Sprint1;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.logging.Logger;
import dev.selenium.test.Modules.Login;
import org.testng.asserts.SoftAssert;

public class Main {
    private static java.time.Duration Duration;

    public static void main(String[] args) throws FileNotFoundException, InterruptedException {

    /*  ----Initializations----    */

        //Soft Assert
        SoftAssert softAssert = new SoftAssert();


        //Getting Save Path for Screenshot and Logger file
        String savePath = SavePath.path();

        //Calling Custom Logger
        CustomLogger customLogger = new CustomLogger(savePath);
        Logger logger = customLogger.getLogger();


        //JSON Data Sets
        Map<String, String> login = JsonDataset.parser( "Login");
        Map<String, String> userDetails = JsonDataset.parser( "UserDetails");
        Map<String, String> sapSetup = JsonDataset.parser( "SapSetup");
        Map<String, String> pathSetup = JsonDataset.parser( "PathSetup");
        Map<String, String> emailSetup = JsonDataset.parser( "EmailSetup");
        Map<String, String> fieldMapping = JsonDataset.parser( "FieldMapping");
        Map<String, String> processSetup = JsonDataset.parser( "ProcessSetup");
        Map<String, String> uploadScheduleSetup = JsonDataset.parser( "UploadScheduleSetup");



        /*  ----End of Initializations----    */



    /*  ----Start of Testing Automations----    */

        //Opening Test Web Page
        WebDriver driver = new EdgeDriver();
        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(15));
        driver.manage().window().maximize();

        //Temporary IP for linkbox, this is for MAXI
        //Update this later if linkbox for lee plaza is fixed
        driver.get("http://192.168.2.32:8069");


        //Sample of Single Module Execution
        Login.Test_Login(driver, wait, login, logger, savePath, softAssert);


        //Sample of Sprint or multiple Module Execution
        Sprint1.execute(driver, wait,
                userDetails, sapSetup, pathSetup, emailSetup,
                fieldMapping, processSetup, uploadScheduleSetup
                ,logger, login, savePath, softAssert);


//        driver.quit();
        try {
            softAssert.assertAll();
            logger.info("All assertions passed successfully.");
        } catch (AssertionError e) {
            logger.info("One or more assertions failed: " + e.getMessage());
        }
    }


}