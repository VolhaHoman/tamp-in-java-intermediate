package com.epam.mentoring.taf.tests.ui;

import com.epam.mentoring.taf.dataobject.ArticleDTO;
import com.epam.mentoring.taf.dataobject.ArticleRequest;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.model.ArticleModel;
import com.epam.mentoring.taf.tests.ArticleBaseTest;
import com.epam.mentoring.taf.util.DataProviderHelper;
import com.epam.mentoring.taf.util.StorageHelper;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.Map;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Article handling UI Tests")
public class ArticleUITest extends ArticleBaseTest {

    public static final String ERROR_MESSAGE_TITLE_BLANK = "title can't be blank";
    public static final String ERROR_MESSAGE_DESCRIPTION_BLANK = "description can't be blank";
    public static final String ERROR_MESSAGE_BODY_BLANK = "body can't be blank";

    @DataProvider(name = "ymlArticleDataProvider")
    public Object[][] dataProviderMethod() throws IOException {
        return getTags();
    }

    private Object[][] getTags() throws IOException {
        try {
            ArticleModel articleModel = READER.readArticle("article");
            return DataProviderHelper.mapToProviderArray(articleModel.getTagList());
        } catch (IOException e) {
            throw new IOException("Failed to load file.");
        }
    }

    @Test(description = "UI: Add a valid article", dataProvider = "ymlArticleDataProvider", priority = 0)
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI: Add a valid article from YAML file")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void uiAddValidArticle(String tag) throws IOException {

        logIn(StorageHelper.whatIsThe(ADMIN_EMAIL), StorageHelper.whatIsThe(ADMIN_PASSWORD));

        ArticleDTO articleDTO = ArticleRequest.generateArticle();

        homePage.navToEditorPage();
        appEditorPage.enterTitle(articleDTO.getTitle())
                     .enterDescription(articleDTO.getDescription())
                     .enterBody(articleDTO.getBody())
                     .enterTag(tag)
                     .publishArticle();

        createdArticles.add(articlePage.getSlugFromUrl());

        Assert.assertEquals(articlePage.getArticleTitle(), articleDTO.getTitle());
        Assert.assertEquals(articlePage.getArticleBody(), articleDTO.getBody());
        Assert.assertEquals(articlePage.getArticleTag(), tag);

    }

    @Test(description = "UI: Edit an existing article", priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI: Edit an existing article")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void uiEditArticle() throws IOException {

        String slug = createArticle();
        createdArticles.add(slug);

        logIn(StorageHelper.whatIsThe(ADMIN_EMAIL), StorageHelper.whatIsThe(ADMIN_PASSWORD));
        selectArticle();

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
    public void uiDeleteArticle() throws IOException {

        String articleSlugToBeDeleted = createArticle();
        createdArticles.add(articleSlugToBeDeleted);

        logIn(StorageHelper.whatIsThe(ADMIN_EMAIL), StorageHelper.whatIsThe(ADMIN_PASSWORD));
        selectArticle();
        String articleTobeDeleted = articlePage.getArticleTitle();
        articlePage.clickDeleteArticleBtn();

        selectArticle();

        Assert.assertNotEquals(articlePage.getArticleTitle(), articleTobeDeleted);
        Assert.assertEquals(client.sendGetRequestWithHeaders(API_ARTICLES + articleSlugToBeDeleted, Map.ofEntries(
                Map.entry(org.apache.http.HttpHeaders.AUTHORIZATION, "Token " + StorageHelper.whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest"))).getStatusCode(),
                    org.apache.http.HttpStatus.SC_NOT_FOUND);

    }

    @Test(description = "UI: Add an article with no title", priority = 3)
    @Severity(SeverityLevel.MINOR)
    @Description("UI: Add an article with no title")
    @Story("Organise “Search By Tag” and “Articles Handling” tests into test suites")
    public void uiAddArticleWithoutTitle() throws IOException {

        logIn(StorageHelper.whatIsThe(ADMIN_EMAIL), StorageHelper.whatIsThe(ADMIN_PASSWORD));

        ArticleDTO articleDTO = ArticleRequest.generateArticle();

        homePage.navToEditorPage();
        appEditorPage.enterDescription(articleDTO.getDescription())
                .enterBody(articleDTO.getBody())
                .publishArticle();

        Assert.assertEquals(appEditorPage.getError(), ERROR_MESSAGE_TITLE_BLANK);
    }

    @Test(description = "UI: Add an article with no description", priority = 4)
    @Severity(SeverityLevel.MINOR)
    @Description("UI: Add an article with no description")
    @Story("Organise “Search By Tag” and “Articles Handling” tests into test suites")
    public void uiAddArticleWithoutDescription() throws IOException {

        logIn(StorageHelper.whatIsThe(ADMIN_EMAIL), StorageHelper.whatIsThe(ADMIN_PASSWORD));

        ArticleDTO articleDTO = ArticleRequest.generateArticle();

        homePage.navToEditorPage();
        appEditorPage.enterTitle(articleDTO.getTitle())
                .enterBody(articleDTO.getBody())
                .publishArticle();

        Assert.assertEquals(appEditorPage.getError(), ERROR_MESSAGE_DESCRIPTION_BLANK);
    }

    @Test(description = "UI: Add an article with no body", priority = 5)
    @Severity(SeverityLevel.MINOR)
    @Description("UI: Add an article with no body")
    @Story("Organise “Search By Tag” and “Articles Handling” tests into test suites")
    public void uiAddArticleWithoutBody() throws IOException {

        logIn(StorageHelper.whatIsThe(ADMIN_EMAIL), StorageHelper.whatIsThe(ADMIN_PASSWORD));

        ArticleDTO articleDTO = ArticleRequest.generateArticle();

        homePage.navToEditorPage();
        appEditorPage.enterTitle(articleDTO.getTitle())
                .enterDescription(articleDTO.getDescription())
                .publishArticle();

        Assert.assertEquals(appEditorPage.getError(), ERROR_MESSAGE_BODY_BLANK);
    }

    @AfterMethod(description = "Post-condition: log out")
    public void articleLogOut() {
        if (homePage.userIconIsDisplayed()) {
            logOut();
        }
    }

}
