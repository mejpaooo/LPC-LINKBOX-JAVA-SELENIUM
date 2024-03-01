package dev.selenium.test.Customizations;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class screenshot {
    public static void screenshot(WebDriver driver, String savePath, String fileName) {
        try {

            // Take a screenshot using the WebDriver
            File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);

            // Specify the file name for the screenshot
            String screenshotName = fileName;

            // Move the screenshot file to the specified directory
            File destinationFile = new File(savePath + screenshotName);
            FileUtils.copyFile(screenshotFile, destinationFile);
            System.out.println("Screenshot saved at: " + destinationFile.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
