package com.epam.mentoring.taf;

import com.epam.mentoring.taf.api.*;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.mapper.ArticleResponseMapper;
import com.epam.mentoring.taf.service.YamlReader;
import com.epam.mentoring.taf.util.DataProviderHelper;
import com.epam.mentoring.taf.util.StorageHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static com.epam.mentoring.taf.util.StorageHelper.rememberThat;
import static com.epam.mentoring.taf.util.StorageHelper.whatIsThe;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Article handling Tests")
public class ArticleTest extends AbstractTest {

    public static final YamlReader READER = new YamlReader();
    private static Logger log = LogManager.getLogger();
    protected static RestClient client = new RestClient(log);
    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    public static final String ADMIN_EMAIL = "ADMIN_EMAIL";
    public static final String ADMIN_PASSWORD = "ADMIN_PASSWORD";
    public static final String SLUG = "SLUG";
    public static final String ERROR_MESSAGE = "title can't be blank";
    public static final String ARTICLES_COUNT_JSON_PATH = "articlesCount";
    public static final String updatedBody = "With two hands";
    public static final String JSON_BODY = "{\"article\":{\"body\":\"%s\"}}";

    @DataProvider(name = "ymlArticleDataProvider")
    public Object[][] dataProviderMethod() throws IOException {
        return getTags();
    }

    private Object[][] getTags() throws IOException {
        try {
            return DataProviderHelper.mapToProviderArray(READER.readArticleTags("article"));
        } catch (IOException e) {
            throw new IOException("Failed to load file.");
        }
    }

    @BeforeClass
    public void getSlug() {
        Response getResponse = client.sendGetRequestWithHeaders(API_ARTICLES, Map.ofEntries(
                Map.entry(org.apache.http.HttpHeaders.AUTHORIZATION, "Token " + StorageHelper.whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")));
        String slug =
                getResponse.getBody().jsonPath().get("articles[0].slug");
        log.info("slug: " + slug);
        rememberThat(SLUG, slug);
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

        Assert.assertEquals(articlePage.getArticleTitle(), articleDTO.getTitle());
        Assert.assertEquals(articlePage.getArticleBody(), articleDTO.getBody());
    }

    @Test(description = "UI: Edit an existing article", priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI: Edit an existing article")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void uiEditExistingArticle() throws IOException {

        loginPage.clickSignInLink()
                .fillInEmail(StorageHelper.whatIsThe(ADMIN_EMAIL))
                .fillInPassword(StorageHelper.whatIsThe(ADMIN_PASSWORD))
                .clickSignInBtn();

        homePage.navToUser();
        userProfilePage.selectArt();

        ArticleDTO articleDTO = ArticleRequest.generateArticle();

        articlePage.clickEditArticleBtn();
        appEditorPage.enterTitle(articleDTO.getTitle())
                .enterBody(articleDTO.getBody())
                .publishArticle();

        Assert.assertEquals(articlePage.getArticleTitle(), articleDTO.getTitle());
        Assert.assertEquals(articlePage.getArticleBody(), articleDTO.getBody());
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

        homePage.navToUser();
        userProfilePage.selectArt();

        String articleTobeDeleted = articlePage.getArticleTitle();

        articlePage.clickDeleteArticleBtn();

        homePage.navToUser();
        userProfilePage.selectArt();

        Assert.assertNotEquals(articlePage.getArticleTitle(), articleTobeDeleted);
    }

    @Test(description = "UI: Add an article with empty fields", priority = 7)
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
        Response response = client.sendPostRequestWithHeaders(API_ARTICLES, articleDTO.articleToString(), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")));
        ArticleResponseMapper articleResponseMapper = new ArticleResponseMapper();
        ArticleResponseDTO articleResponseDTO = articleResponseMapper.articleToDto(response, log);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        Assert.assertEquals(articleResponseDTO.getArticle().getTitle(), articleDTO.getTitle());
        Assert.assertEquals(articleResponseDTO.getArticle().getDescription(), articleDTO.getDescription());
        Assert.assertEquals(articleResponseDTO.getArticle().getBody(), articleDTO.getBody());
        Assert.assertTrue(articleResponseDTO.getArticle().getTagList().containsAll(articleDTO.getTagList()));
    }

    @Test(description = "API: Read all articles with authorization", priority = 4)
    @Severity(SeverityLevel.NORMAL)
    @Description("API: Read all articles with authorization")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void apiReadAllArticles() {

        Response response = client.sendGetRequestWithHeaders(API_ARTICLES, Map.ofEntries(
                Map.entry(org.apache.http.HttpHeaders.AUTHORIZATION, "Token " + StorageHelper.whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")));

        int articlesCount = response.getBody().jsonPath().get(ARTICLES_COUNT_JSON_PATH);

        Assert.assertEquals(response.getStatusCode(), org.apache.http.HttpStatus.SC_OK);
        Assert.assertTrue(articlesCount > 0);
    }

    @Test(description = "API: Update an existing article", priority = 5)
    @Severity(SeverityLevel.BLOCKER)
    @Description("API: Update an existing article")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void apiUpdateArticle() {

        Response response = client.sendPutRequestWithHeaders(API_ARTICLES +  whatIsThe(SLUG), String.format(JSON_BODY, updatedBody), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")));
        ArticleResponseMapper articleResponseMapper = new ArticleResponseMapper();
        ArticleResponseDTO articleResponseDTO = articleResponseMapper.articleToDto(response, log);

        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        Assert.assertEquals(articleResponseDTO.getArticle().getBody(), updatedBody);
    }

    @Test(description = "API: Delete an existing article", priority = 6)
    @Severity(SeverityLevel.CRITICAL)
    @Description("API: Delete an existing article")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void apiDeleteArticle() {

        Response response = client.sendDeleteRequestWithHeaders(API_ARTICLES +  whatIsThe(SLUG), Map.ofEntries(
                Map.entry(org.apache.http.HttpHeaders.AUTHORIZATION, "Token " + StorageHelper.whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")));

        Assert.assertEquals(response.getStatusCode(), org.apache.http.HttpStatus.SC_NO_CONTENT);
    }

}
