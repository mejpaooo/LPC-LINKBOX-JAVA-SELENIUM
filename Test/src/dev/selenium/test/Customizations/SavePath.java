package dev.selenium.test.Customizations;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SavePath {

    public static String path(){
        //      Initiilizing directory for logs and screenshot
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HHmmss");
        String currentDateTime = dateFormat.format(new Date());

        String savePath = "Test/assets/logs/" + currentDateTime + "/";
        String logFileName = "test.log";
        File directory = new File(savePath);         // Create the directory if it doesn't exist
        directory.mkdirs();

        return savePath;
    }


}
