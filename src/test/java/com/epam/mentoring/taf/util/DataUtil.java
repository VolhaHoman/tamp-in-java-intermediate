package com.epam.mentoring.taf.util;

import com.epam.mentoring.taf.exception.DataUtilException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.DataProvider;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class DataUtil {

    public static final String TEST_DATA_JSON = "src/test/resources/testData.json";
    private static final Logger log = LogManager.getLogger();

    @DataProvider(name = "dataProviderForValidComments")
    public Object[][] dataProviderForValidComments() {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;

        try {
            Object obj = parser.parse(new FileReader(TEST_DATA_JSON));
            jsonObject = (JSONObject) obj;
        } catch (IOException | ParseException e) {
            log.error("Error reading test data from JSON file");
            throw new DataUtilException("Error reading test data from JSON file", e);
        }

        if (jsonObject == null) {
            log.error("Error retrieving JSON data");
            throw new DataUtilException("Error retrieving JSON data");
        }

        JSONArray validTextArray = null;
        try {
            validTextArray = (JSONArray) jsonObject.get("valid_text");
        } catch (NullPointerException e) {
            log.error("JSON data is null or does not contain valid_text array");
            throw new DataUtilException("JSON data is null or does not contain valid_text array", e);
        }
        int numTests = validTextArray.size();
        Object[][] data = new Object[numTests][1];
        for (int i = 0; i < numTests; i++) {
            String body = (String) ((JSONObject) validTextArray.get(i)).get("body");
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("body", body);
            data[i][0] = hashMap;
        }

        return data;
    }

    @DataProvider(name = "dataProviderForInvalidComments")
    public Object[][] dataProviderForInvalidComments() {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;

        try {
            Object obj = parser.parse(new FileReader(TEST_DATA_JSON));
            jsonObject = (JSONObject) obj;
        } catch (IOException | ParseException e) {
            throw new DataUtilException("Error reading test data from JSON file", e);
        }

        List<String> invalidTexts = new ArrayList<>();
        if (jsonObject != null) {
            JSONArray invalidTextArray = null;
            try {
                invalidTextArray = (JSONArray) jsonObject.get("invalid_text");
            } catch (NullPointerException e) {
                log.error("JSON data is null or does not contain valid_text array");
                throw new DataUtilException("JSON data is null or does not contain valid_text array", e);
            }
            for (Object invalidTextObj : invalidTextArray) {
                invalidTexts.add((String) invalidTextObj);
            }
        } else {
            log.error("Error retrieving JSON data");
            throw new DataUtilException("Error retrieving JSON data");
        }

        Object[][] data = new Object[invalidTexts.size()][1];
        for (int i = 0; i < invalidTexts.size(); i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("invalid_text", invalidTexts.get(i));
            data[i][0] = hashMap;
        }
        return data;
    }
}