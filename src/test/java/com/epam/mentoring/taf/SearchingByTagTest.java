package com.epam.mentoring.taf;

import com.epam.mentoring.taf.api.RestAPIClient;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.service.YamlReader;
import com.epam.mentoring.taf.ui.page.HomePage;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Searching By Tag Tests")
public class SearchingByTagTest extends AbstractTest {

    public static final YamlReader READER = new YamlReader();
    public static final String INVALID_TAG = "invalid_tag_name";
    public static final String TAG_LIST_JSON_PATH = "articles.tagList";
    public static final String ARTICLES_COUNT_JSON_PATH = "articlesCount";

    @DataProvider(name = "apiDataProvider")
    public Object[][] apiDataProviderMethod() throws IOException {
        return getTags();
    }

    private Object[][] getTags() throws IOException {
        try {
            String[] tags = READER.readTags();
            Object[][] data = new Object[tags.length][1];
            for (int i = 0; i < tags.length; i++) {
                data[i][0] = tags[i];
            }
            return data;
        } catch (IOException e) {
            throw new IOException("Failed to load the file.");
        }
    }

    @Test(description = "UI Search by a valid tag")
    @Severity(SeverityLevel.CRITICAL)
    @Description("UI Search by a valid tag")
    @Story("Add UI and API layers support to SearchByTagTest")
    public void uiSearchByRandomValidTag() {
        HomePage homePage = new HomePage();
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
        Response response = RestAPIClient.sendGetTagRequest(tag);
        List<String> tagList = response.getBody().jsonPath().get(TAG_LIST_JSON_PATH);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertTrue(tagList.toString().contains(tag));
    }

    @Test(description = "API Search by an invalid tag")
    @Severity(SeverityLevel.MINOR)
    @Description("API Search by an invalid tag")
    @Story("Add UI and API layers support to SearchByTagTest")
    public void apiSearchByInvalidTag() {
        RestAPIClient RestAPIClient = new RestAPIClient();
        Response response = RestAPIClient.sendGetTagRequest(INVALID_TAG);
        int articlesCount = response.getBody().jsonPath().get(ARTICLES_COUNT_JSON_PATH);
        Assert.assertEquals(response.getStatusCode(), 200);
        Assert.assertEquals(articlesCount, 0);
    }
}
