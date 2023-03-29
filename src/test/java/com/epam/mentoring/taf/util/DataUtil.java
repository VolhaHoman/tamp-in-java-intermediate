package com.epam.mentoring.taf.util;

import com.epam.mentoring.taf.ui.page.BasePage;
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

public class DataUtil extends BasePage {

    public static final String TEST_DATA_JSON = "src/test/resources/testData.json";

    @DataProvider(name = "dataProviderForValidComments")
    public Object[] dataProviderForValidComments() {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;

        //Read JSON file
        try {
            Object obj = parser.parse(new FileReader(TEST_DATA_JSON));
            jsonObject = (JSONObject) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        if (jsonObject == null) {
            logger.error("Error retrieving JSON data");
            throw new RuntimeException();
        }

        //Create an array of HashMap objects, one for each test run
        JSONArray validTextArray = (JSONArray) jsonObject.get("valid_text");
        int numTests = validTextArray.size();
        Object[] data = new Object[numTests];
        for (int i = 0; i < numTests; i++) {
            String body = (String) ((JSONObject) validTextArray.get(i)).get("body");
            HashMap<String, Object> hashMap = new HashMap<>();
            hashMap.put("body", body);
            data[i] = hashMap;
        }

        return data;
    }

    @DataProvider(name = "dataProviderForInvalidComments")
    public Object[] dataProviderForInvalidComments() {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;

        //Read JSON file
        try {
            Object obj = parser.parse(new FileReader(TEST_DATA_JSON));
            jsonObject = (JSONObject) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        List<String> invalidTexts = new ArrayList<>();
        if (jsonObject != null) {
            JSONArray invalidTextArray = (JSONArray) jsonObject.get("invalid_text");
            for (Object invalidTextObj : invalidTextArray) {
                invalidTexts.add((String) invalidTextObj);
            }
        } else {
            logger.error("Error retrieving JSON data");
            throw new RuntimeException();
        }

        Object[] data = new Object[invalidTexts.size()];
        for (int i = 0; i < invalidTexts.size(); i++) {
            HashMap<String, String> hashMap = new HashMap<>();
            hashMap.put("invalid_text", invalidTexts.get(i));
            data[i] = hashMap;
        }
        return data;
    }
}




