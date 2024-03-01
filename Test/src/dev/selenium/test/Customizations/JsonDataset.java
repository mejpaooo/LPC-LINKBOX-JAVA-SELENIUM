package dev.selenium.test.Customizations;

import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Map;

public class JsonDataset {
    public static Map<String, String> parser(String jsonObj) throws FileNotFoundException {
        // Parsing JSON Dataset
        Gson gson = new Gson();
        FileReader reader = new FileReader("Test/assets/dataset.json");
        Map<String, Object> map = gson.fromJson(reader, Map.class);         // Read the JSON data into a Map<String, Object>

        // Accessing JSON data
        Map<String, String> result = (Map<String, String>) map.get(jsonObj);
        return result;
    }

}

