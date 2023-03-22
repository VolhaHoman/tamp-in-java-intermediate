package com.epam.mentoring.taf.util;

import com.epam.mentoring.taf.ui.page.BasePage;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.testng.annotations.DataProvider;

import java.io.FileReader;
import java.io.IOException;

public class DataUtil extends BasePage {

    @DataProvider
    public Object[] dataProviderForComments() {
        JSONParser parser = new JSONParser();
        JSONObject jsonObject = null;

        //Read JSON file
        try {
            Object obj = parser.parse(new FileReader("src/test/resources/testData.json"));
            jsonObject = (JSONObject) obj;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        JSONArray comment = (JSONArray) jsonObject.get("comment");

        //Array to store JSON data
        String[] dataArray = new String[comment.size()];

        JSONObject commentData;
        String body;

        for (int i = 0; i < comment.size(); i++) {
            commentData = (JSONObject) comment.get(i);
            body = (String) commentData.get("body");
            dataArray[i] = body;
        }
        return dataArray;

        //Store JSON data as key/value paris in a hashMap
/*        HashMap<String, String> hashMap = new LinkedHashMap<>();
        if (jsonpObject != null) {
            Set<String> jsonObjKeys = jsonpObject.keySet();
            for (String jsonObjKey : jsonObjKeys)  {
                hashMap.put(jsonObjKey, (String) jsonpObject.get(jsonObjKey))
            }

        } else {
            logger.error("Error retrieving JSON data");
            throw new RuntimeException();
        }

        //Store HashMap in array and return array
        data[0] = hashMap;
        return data;*/
    }

}

