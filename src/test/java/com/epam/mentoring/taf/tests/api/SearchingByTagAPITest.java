package com.epam.mentoring.taf.tests.api;

import com.epam.mentoring.taf.api.RestAPIClient;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.tests.IYmlReader;
import com.epam.mentoring.taf.util.DataProviderHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static com.epam.mentoring.taf.tests.AllCommentsBase.ARTICLES_COUNT_JSON_PATH;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Searching By Tag API Tests")
public class SearchingByTagAPITest implements IYmlReader {

    Logger logger = LogManager.getLogger();

    public static final String INVALID_TAG = "invalid_tag_name";
    public static final String TAG_LIST_JSON_PATH = "articles.tagList";

    @DataProvider(name = "apiDataProvider")
    public Object[][] apiDataProviderMethod() throws IOException {
        return getTags();
    }

    @Test(dataProvider = "apiDataProvider", description = "API Search by a valid tag")
    @Severity(SeverityLevel.CRITICAL)
    @Description("API Search by a valid tag")
    @Story("Add UI and API layers support to SearchByTagTest")
    public void apiSearchByValidTag(String tag) {
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendGetTagRequest(tag, logger);
        List<String> tagList = response.getBody().jsonPath().get(TAG_LIST_JSON_PATH);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        Assert.assertTrue(tagList.toString().contains(tag));
    }

    @Test(description = "API Search by an invalid tag")
    @Severity(SeverityLevel.MINOR)
    @Description("API Search by an invalid tag")
    @Story("Add UI and API layers support to SearchByTagTest")
    public void apiSearchByInvalidTag() {
        RestAPIClient restAPIClient = new RestAPIClient();
        Response response = restAPIClient.sendGetTagRequest(INVALID_TAG, logger);
        int articlesCount = response.getBody().jsonPath().get(ARTICLES_COUNT_JSON_PATH);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        Assert.assertEquals(articlesCount, 0);
    }

    private Object[][] getTags() throws IOException {
        try {
            return DataProviderHelper.mapToProviderArray(READER.get().readTags());
        } catch (IOException e) {
            throw new IOException("Failed to load file.");
        }
    }
}
