package com.epam.mentoring.taf;

import com.epam.mentoring.taf.api.RestAPIClient;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.util.DataProviderHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpStatus;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Searching By Tag Tests")
public class SearchingByTagTest extends UiBaseTest {

    public static final String INVALID_TAG = "invalid_tag_name";
    public static final String TAG_LIST_JSON_PATH = "articles.tagList";

    @DataProvider(name = "apiDataProvider")
    public Object[][] apiDataProviderMethod() throws IOException {
        return getTags();
    }

    @Test(description = "UI Search by a valid tag")
    @Severity(SeverityLevel.CRITICAL)
    @Description("UI Search by a valid tag")
    @Story("Add UI and API layers support to SearchByTagTest")
    public void uiSearchByRandomValidTag() {
        String tagName = homePage.getTagFromSidebar().getTagText();
        homePage.clickTag();
        String selectedTag = homePage.getNavLink();
        Assert.assertEquals(selectedTag, tagName);
    }

    @Test(dataProvider = "apiDataProvider", description = "API Search by a valid tag")
    @Severity(SeverityLevel.CRITICAL)
    @Description("API Search by a valid tag")
    @Story("Add UI and API layers support to SearchByTagTest")
    public void apiSearchByValidTag(String tag) {
        RestAPIClient RestAPIClient = new RestAPIClient();
        Response response = RestAPIClient.sendGetTagRequest(tag, log);
        List<String> tagList = response.getBody().jsonPath().get(TAG_LIST_JSON_PATH);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        Assert.assertTrue(tagList.toString().contains(tag));
    }

    @Test(description = "API Search by an invalid tag")
    @Severity(SeverityLevel.MINOR)
    @Description("API Search by an invalid tag")
    @Story("Add UI and API layers support to SearchByTagTest")
    public void apiSearchByInvalidTag() {
        RestAPIClient RestAPIClient = new RestAPIClient();
        Response response = RestAPIClient.sendGetTagRequest(INVALID_TAG, log);
        int articlesCount = response.getBody().jsonPath().get(ARTICLES_COUNT_JSON_PATH);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        Assert.assertEquals(articlesCount, 0);
    }

    private Object[][] getTags() throws IOException {
        try {
            return DataProviderHelper.mapToProviderArray(READER.readTags());
        } catch (IOException e) {
            throw new IOException("Failed to load file.");
        }
    }
}
