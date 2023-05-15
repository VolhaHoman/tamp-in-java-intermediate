package com.epam.mentoring.taf.tests.ui;

import com.epam.mentoring.taf.dataobject.ArticleDTO;
import com.epam.mentoring.taf.dataobject.ArticleRequest;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.model.ArticleModel;
import com.epam.mentoring.taf.model.UserDataModel;
import com.epam.mentoring.taf.tests.*;
import com.epam.mentoring.taf.tests.uihelper.ArticlePageBaseUI;
import com.epam.mentoring.taf.tests.uihelper.LoginBaseUI;
import com.epam.mentoring.taf.tests.uihelper.OpenClose;
import com.epam.mentoring.taf.tests.uihelper.PageLoader;
import com.epam.mentoring.taf.ui.page.AppEditorPage;
import com.epam.mentoring.taf.ui.page.ArticlePage;
import com.epam.mentoring.taf.ui.page.HomePage;
import com.epam.mentoring.taf.ui.page.UserProfilePage;
import com.epam.mentoring.taf.util.DataProviderHelper;
import io.qameta.allure.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Article handling UI Tests")
public class ArticleUITest
        implements IYmlReader, IRestClient, ILoggerTest, IAuthorizationTest, OpenClose,
        LoginBaseUI, ArticlePageBaseUI, PageLoader {

    public static final String ERROR_MESSAGE_TITLE_BLANK = "title can't be blank";
    public static final String ERROR_MESSAGE_DESCRIPTION_BLANK = "description can't be blank";
    public static final String ERROR_MESSAGE_BODY_BLANK = "body can't be blank";

    private UserDataModel adminUser;

    @BeforeMethod
    public void initUser() throws IOException {
        adminUser = READER.get().readUserData("adminUser");
    }

    @DataProvider(name = "ymlArticleDataProvider")
    public Object[][] dataProviderMethod() throws IOException {
        return getTags();
    }

    @Test(description = "UI: Add a valid article", dataProvider = "ymlArticleDataProvider", priority = 0)
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI: Add a valid article from YAML file")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void uiAddValidArticle(String tag) throws IOException {
        ArticleMainBase articleMainBase = new ArticleMainBase(LOGGER.get());
        try {
            HomePage homePage = homePage();

            logIn(adminUser.getUserEmail(), adminUser.getUserPassword(), homePage, loginPage());

            ArticleDTO articleDTO = ArticleRequest.generateArticle();
            ArticlePage articlePage = articlePage();

            homePage.navToEditorPage();
            appEditorPage().enterTitle(articleDTO.getTitle())
                    .enterDescription(articleDTO.getDescription())
                    .enterBody(articleDTO.getBody())
                    .enterTag(tag)
                    .publishArticle();

            articleMainBase.getCreatedArticles().add(articlePage.getSlugFromUrl());

            Assert.assertEquals(articlePage.getArticleTitle(), articleDTO.getTitle());
            Assert.assertEquals(articlePage.getArticleBody(), articleDTO.getBody());
            Assert.assertEquals(articlePage.getArticleTag(), tag);
        } finally {
            articleMainBase.cleanArticle();
        }

    }

    @Test(description = "UI: Edit an existing article", priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI: Edit an existing article")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void uiEditArticle() throws IOException {
        ArticleMainBase articleMainBase = new ArticleMainBase(LOGGER.get());
        try {
            articleMainBase.createArticle();
            HomePage homePage = homePage();
            logIn(adminUser.getUserEmail(), adminUser.getUserPassword(), homePage, loginPage());
            selectArticle(homePage, userProfilePage());

            ArticleDTO articleDTO = ArticleRequest.generateArticle();

            ArticlePage articlePage = articlePage();

            articlePage.clickEditArticleBtn();
            appEditorPage().enterTitle(articleDTO.getTitle())
                    .enterBody(articleDTO.getBody())
                    .publishArticle();

            Assert.assertEquals(articlePage.getArticleTitle(), articleDTO.getTitle());
            Assert.assertEquals(articlePage.getArticleBody(), articleDTO.getBody());
        } finally {
            articleMainBase.cleanArticle();
        }
    }

    @Test(description = "UI: Delete an article", priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Description("UI: Delete an article")
    @Story("Create new tests for articles handling functionality using Annotations and Data Providers")
    public void uiDeleteArticle() throws IOException {
        ArticleMainBase articleMainBase = new ArticleMainBase(LOGGER.get());
        try {
            articleMainBase.createArticle();
            HomePage homePage = homePage();

            logIn(adminUser.getUserEmail(), adminUser.getUserPassword(), homePage, loginPage());
            UserProfilePage userProfilePage = userProfilePage();
            selectArticle(homePage, userProfilePage);
            ArticlePage articlePage = articlePage();
            String articleTobeDeleted = articlePage.getArticleTitle();
            articlePage.clickDeleteArticleBtn();
            homePage.navToUser();
            boolean noArticleMessagePresent = userProfilePage.checkNoArticleMessage();
            boolean isDeletedArticlePresent = userProfilePage
                    .isPresentByXpath("//h1[contains(text(),'" + articleTobeDeleted + "')]");
            Assert.assertTrue(noArticleMessagePresent || !isDeletedArticlePresent);
        } finally {
            articleMainBase.cleanArticle();
        }
    }

    @Test(description = "UI: Add an article with no title", priority = 3)
    @Severity(SeverityLevel.MINOR)
    @Description("UI: Add an article with no title")
    @Story("Organise “Search By Tag” and “Articles Handling” tests into test suites")
    public void uiAddArticleWithoutTitle() throws IOException {

        HomePage homePage = homePage();

        logIn(adminUser.getUserEmail(), adminUser.getUserPassword(), homePage, loginPage());

        ArticleDTO articleDTO = ArticleRequest.generateArticle();

        homePage.navToEditorPage();
        AppEditorPage appEditorPage = appEditorPage();
        appEditorPage
                .enterDescription(articleDTO.getDescription())
                .enterBody(articleDTO.getBody())
                .publishArticle();

        Assert.assertEquals(appEditorPage.getError(), ERROR_MESSAGE_TITLE_BLANK);
    }

    @Test(description = "UI: Add an article with no description", priority = 4)
    @Severity(SeverityLevel.MINOR)
    @Description("UI: Add an article with no description")
    @Story("Organise “Search By Tag” and “Articles Handling” tests into test suites")
    public void uiAddArticleWithoutDescription() throws IOException {

        HomePage homePage = homePage();
        logIn(adminUser.getUserEmail(), adminUser.getUserPassword(), homePage, loginPage());

        ArticleDTO articleDTO = ArticleRequest.generateArticle();

        homePage.navToEditorPage();
        AppEditorPage appEditorPage = appEditorPage();
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
        HomePage homePage = homePage();
        logIn(adminUser.getUserEmail(), adminUser.getUserPassword(), homePage, loginPage());

        ArticleDTO articleDTO = ArticleRequest.generateArticle();

        homePage.navToEditorPage();
        AppEditorPage appEditorPage = appEditorPage();
        appEditorPage.enterTitle(articleDTO.getTitle())
                .enterDescription(articleDTO.getDescription())
                .publishArticle();

        Assert.assertEquals(appEditorPage.getError(), ERROR_MESSAGE_BODY_BLANK);
    }

    @AfterMethod(description = "Post-condition: log out")
    public void articleLogOut() {
        HomePage homePage = homePage();
        if (homePage.userIconIsDisplayed()) {
            logOut(homePage, settingsPage());
        }
    }

    private Object[][] getTags() throws IOException {
        try {
            ArticleModel articleModel = READER.get().readArticle("article");
            return DataProviderHelper.mapToProviderArray(articleModel.getTagList());
        } catch (IOException e) {
            throw new IOException("Failed to load file.");
        }
    }
}
