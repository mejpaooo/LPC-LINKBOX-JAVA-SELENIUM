package dev.selenium.test.Modules;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Map;
import java.util.logging.Logger;

import static dev.selenium.test.Customizations.screenshot.screenshot;
public class SapSetup {
    public static void Test_SapSetup(WebDriver driver, WebDriverWait wait, Map<String, String> test, Logger logger, String savePath) throws InterruptedException {
        logger.info("[START] of Test Module 1");
        screenshot(driver, savePath, "Test Module 1.png");
    }
}