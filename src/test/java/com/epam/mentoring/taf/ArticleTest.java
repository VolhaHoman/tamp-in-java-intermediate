package com.epam.mentoring.taf;

import com.epam.mentoring.taf.api.*;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.service.YamlReader;
import com.epam.mentoring.taf.util.DataProviderHelper;
import com.epam.mentoring.taf.util.StorageHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.epam.mentoring.taf.util.StorageHelper.whatIsThe;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Article handling Tests")
public class ArticleTest extends AbstractTest {

    public static final YamlReader READER = new YamlReader();

    private static Logger log = LogManager.getLogger();

    protected static RestClient client = new RestClient(log);

    public static final String AUTH_TOKEN = "AUTH_TOKEN";

    public static final String ADMIN_EMAIL = "ADMIN_USER";

    public static final String ADMIN_PASSWORD = "ADMIN_PASSWORD";
    public static final String ERROR_MESSAGE = "title can't be blank";

    String title = "How to train your dragon" + RandomStringUtils.randomNumeric(10);
    String description = "Ever wonder how?";
    String body = "Very carefully.";
    List<String> tagList = Arrays.asList("training", "dragons");
    String newTitle = "This title was updated" + RandomStringUtils.randomNumeric(10);
    String newBody = "This body was updated" + RandomStringUtils.randomNumeric(10);

    @DataProvider(name = "ymlArticleDataProvider")
    public Object[][] apiDataProviderMethod() throws IOException {
        return getTags();
    }

    private Object[][] getTags() throws IOException {
        try {
            return DataProviderHelper.mapToProviderArray(READER.readTags("article/tagList"));
        } catch (IOException e) {
            throw new IOException("Failed to load file.");
        }
    }

    @Test(description = "UI: Add a valid article", dataProvider = "ymlArticleDataProvider", priority = 0)
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI: Add a valid article from YAML file")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void uiAddValidArticle(String tag) throws IOException {
        loginPage.clickSignInLink()
                .fillInEmail(StorageHelper.whatIsThe(ADMIN_EMAIL))
                .fillInPassword(StorageHelper.whatIsThe(ADMIN_PASSWORD))
                .clickSignInBtn();

        ArticleDTO articleDTO = ArticleRequest.generateArticle();

        homePage.navToEditorPage();
        appEditorPage.enterTitle(articleDTO.getTitle())
                .enterDescription(articleDTO.getDescription())
                .enterBody(articleDTO.getBody());
        appEditorPage.enterTag(tag);
        appEditorPage.publishArticle();

        Assert.assertEquals(articlePage.getArticleTitle(), title);
        Assert.assertEquals(articlePage.getArticleBody(), body);
    }

    @Test(description = "UI: Edit an existing article", priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI: Edit an existing article")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void uiEditExistingArticle() {
        loginPage.clickSignInLink()
                .fillInEmail(StorageHelper.whatIsThe(ADMIN_EMAIL))
                .fillInPassword(StorageHelper.whatIsThe(ADMIN_PASSWORD))
                .clickSignInBtn();

        homePage.navToUser()
                .selectArt();

        articlePage.clickEditArticleBtn();
        appEditorPage.enterTitle(newTitle)
                .enterBody(newBody)
                .publishArticle();

        Assert.assertEquals(articlePage.getArticleTitle(), newTitle);
        Assert.assertEquals(articlePage.getArticleBody(), newBody);
    }

    @Test(description = "UI: Delete an article", priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Description("UI: Delete an article")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void uiDeleteArticle() {
        loginPage.clickSignInLink()
                .fillInEmail(StorageHelper.whatIsThe(ADMIN_EMAIL))
                .fillInPassword(StorageHelper.whatIsThe(ADMIN_PASSWORD))
                .clickSignInBtn();

        homePage.navToUser()
                .selectArt();

        String articleTobeDeleted = articlePage.getArticleTitle();

        articlePage.clickDeleteArticleBtn();

        homePage.navToUser()
                .selectArt();

        Assert.assertNotEquals(articlePage.getArticleTitle(), articleTobeDeleted);
    }

    @Test(description = "UI: Add an article with empty fields", priority = 6)
    @Severity(SeverityLevel.MINOR)
    @Description("UI: Add an article with empty fields")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void uiAddEmptyArticle() {
        loginPage.clickSignInLink()
                .fillInEmail(StorageHelper.whatIsThe(ADMIN_EMAIL))
                .fillInPassword(StorageHelper.whatIsThe(ADMIN_PASSWORD))
                .clickSignInBtn();

        homePage.navToEditorPage();
        appEditorPage.publishArticle();

        Assert.assertEquals(appEditorPage.getError(), ERROR_MESSAGE);
    }

    @Test(description = "API: Add a valid article", priority = 3)
    @Severity(SeverityLevel.BLOCKER)
    @Description("API: Add a valid article from YAML file")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void apiAddValidArticle() throws IOException {

        ArticleDTO articleDTO = ArticleRequest.generateArticle();
        Response response = client.sendPostRequestWithHeaders(API_ARTICLES + "/articles", String.valueOf(articleDTO), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")));
        RestAPIClient restAPIClient = new RestAPIClient();
        ArticleResponseDTO articleResponseDTO = restAPIClient.transformToDtoArticle(response, log);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        Assert.assertEquals(articleResponseDTO.getArticle().getTitle(), articleDTO.getTitle());
        Assert.assertEquals(articleResponseDTO.getArticle().getDescription(), articleDTO.getDescription());
        Assert.assertEquals(articleResponseDTO.getArticle().getBody(), articleDTO.getBody());
        Assert.assertEquals(articleResponseDTO.getArticle().getTagList(), articleDTO.getTagList());

    }
}
