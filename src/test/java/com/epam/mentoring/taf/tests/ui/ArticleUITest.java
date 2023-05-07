package com.epam.mentoring.taf.tests.ui;

import com.epam.mentoring.taf.api.ArticleDTO;
import com.epam.mentoring.taf.api.ArticleRequest;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.model.ArticleModel;
import com.epam.mentoring.taf.tests.ArticleBaseTest;
import com.epam.mentoring.taf.util.DataProviderHelper;
import com.epam.mentoring.taf.util.StorageHelper;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Article handling UI Tests")
public class ArticleUITest extends ArticleBaseTest {

    public static final String ERROR_MESSAGE_TITLE_BLANK = "title can't be blank";

    List<String> createdArticles = new ArrayList<>();

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
    public void uiEditExistingArticle() throws IOException {

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

    @Test(description = "UI: Add an article with empty fields", priority = 3)
    @Severity(SeverityLevel.MINOR)
    @Description("UI: Add an article with empty fields")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void uiAddEmptyArticle() {

        logIn(StorageHelper.whatIsThe(ADMIN_EMAIL), StorageHelper.whatIsThe(ADMIN_PASSWORD));
        homePage.navToEditorPage();
        appEditorPage.publishArticle();

        Assert.assertEquals(appEditorPage.getError(), ERROR_MESSAGE_TITLE_BLANK);
    }

    @AfterMethod(description = "Post-condition: log out")
    public void articleLogOut() {
        if (homePage.userIconIsDisplayed()) {
            logOut();
        }
    }

}
