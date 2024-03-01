package dev.selenium.test.Customizations;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class CustomLogger {
    private Logger logger;
    private FileHandler fileHandler;


    public CustomLogger(String savePath) {
        try {
            // Create a logger with a unique name (e.g., your project name)
            logger = Logger.getLogger("Selenium Automation");

            // Set the logger's log level (e.g., INFO)
            logger.setLevel(Level.INFO);

            // Create a file handler to write logs to a file in the specified folder
            String logFilePath = savePath + "test.log";
            fileHandler = new FileHandler(logFilePath, true); // Append to the log file
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public Logger getLogger(){
        return logger;
    }
}
