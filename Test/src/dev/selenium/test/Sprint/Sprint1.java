package dev.selenium.test.Sprint;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.FileNotFoundException;
import java.util.Map;
import java.util.logging.Logger;

import dev.selenium.test.Modules.UserDetails;
import org.testng.asserts.SoftAssert;

public class Sprint1 {


    public static void execute(WebDriver driver, WebDriverWait wait,
                               Map<String, String> userDetails,
                               Map<String, String> sapSetup,
                               Map<String, String> pathSetup,
                               Map<String, String> emailSetup,
                               Map<String, String> fieldMapping,
                               Map<String, String> processSetup,
                               Map<String, String> uploadScheduleSetup,
                               Logger logger, Map<String, String> login,
                               String savePath, SoftAssert softAssert) throws InterruptedException, FileNotFoundException {



        UserDetails.Test_UserDetails(driver, wait, userDetails, login, logger, savePath, softAssert);
    }


}
