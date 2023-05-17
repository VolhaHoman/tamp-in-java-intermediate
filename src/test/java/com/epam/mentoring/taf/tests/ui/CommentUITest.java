package com.epam.mentoring.taf.tests.ui;

import com.epam.mentoring.taf.dataobject.CommentDTO;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.mapper.ResponseDataTransferMapper;
import com.epam.mentoring.taf.model.UserDataModel;
import com.epam.mentoring.taf.tests.*;
import com.epam.mentoring.taf.tests.uihelper.ArticlePageBaseUI;
import com.epam.mentoring.taf.tests.uihelper.LoginBaseUI;
import com.epam.mentoring.taf.tests.uihelper.OpenClose;
import com.epam.mentoring.taf.tests.uihelper.PageLoader;
import com.epam.mentoring.taf.ui.page.ArticlePage;
import com.epam.mentoring.taf.ui.page.HomePage;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import static com.epam.mentoring.taf.util.StorageHelper.rememberThat;
import static com.epam.mentoring.taf.util.StorageHelper.whatIsThe;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("UI: Comments Tests")
public class CommentUITest
        implements IYmlReader, IRestClient, IAuthorizationTest, LoginBaseUI,
        ArticlePageBaseUI, IAllCommentTest, OpenClose, PageLoader {

    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    public static final String COM_ID = "ID";
    public static final String ALL_COMMENT = "ALL_COMMENT";
    public static final String ERROR_MESSAGE = "body can't be blank";
    public static final String COMMENT = "Test";

    private UserDataModel adminUser;

    Logger logger = LogManager.getLogger();

    private final ThreadLocal<ArticleMainBase> articleMainBase =
            ThreadLocal.withInitial(() -> new ArticleMainBase(logger));

    @BeforeMethod
    public void initUser() throws IOException {
        adminUser = READER.get().readUserData("adminUser");
    }

    @AfterMethod(alwaysRun = true)
    public void deleteArticle() {
        articleMainBase.get().cleanArticle();
    }

    @Test(description = "UI: add comment to article")
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI verification of adding comments")
    @Story("Create new tests for comments functionality")
    public void uiSubmittedCommentVerification() throws IOException {
        articleMainBase.get().createArticle();

        HomePage homePage = homePage(logger);
        logIn(adminUser.getUserEmail(), adminUser.getUserPassword(), homePage, loginPage(logger));
        selectArticle(homePage, userProfilePage(logger));
        ArticlePage articlePage = articlePage(logger);
        articlePage.enterComment(COMMENT)
                .clickSendCommentBtn();

        Assert.assertEquals(articlePage.getComment(), COMMENT);
        logOut(homePage, settingsPage(logger));
    }

    @Test(description = "UI: add empty comment")
    @Severity(SeverityLevel.MINOR)
    @Story("Create new tests for comments")
    @Description("UI add empty comment")
    public void uiEmptyCommentVerification() throws IOException {
        articleMainBase.get().createArticle();

        HomePage homePage = homePage(logger);
        logIn(adminUser.getUserEmail(), adminUser.getUserPassword(), homePage, loginPage(logger));
        selectArticle(homePage, userProfilePage(logger));
        ArticlePage articlePage = articlePage(logger);
        articlePage.enterComment("")
                .clickSendCommentBtn();

        Assert.assertEquals(articlePage.getError(), ERROR_MESSAGE);
        logOut(homePage, settingsPage(logger));
    }

    @Test(description = "UI: delete comment from article")
    @Severity(SeverityLevel.CRITICAL)
    @Description("UI verification of deleting comments")
    @Story("Create new tests for comments functionality")
    public void uiDeleteCommentVerification() throws IOException {
        articleMainBase.get().createArticle();

        HomePage homePage = homePage(logger);
        logIn(adminUser.getUserEmail(), adminUser.getUserPassword(), homePage, loginPage(logger));
        selectArticle(homePage, userProfilePage(logger));
        ArticlePage articlePage = articlePage(logger);
        articlePage.enterComment(COMMENT)
                .clickSendCommentBtn();
        articlePage.clickDeleteCommentBtn();

        Assert.assertFalse(articlePage.commentIsNotDisplayed());
        logOut(homePage, settingsPage(logger));
    }

    @AfterTest(description = "Post-condition: delete all comments")
    public void cleanComments() {
        Response getResponse = CLIENT.get().sendGetRequestWithHeaders(whatIsThe(ALL_COMMENT), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")
        ));
        List<String> id =
                getResponse.getBody().jsonPath().getList("comments.id", String.class);
        rememberThat(COM_ID, String.valueOf(id));
        for (int i = 0; i < id.size(); i++) {
            String uniqueCommentPath = whatIsThe(ALL_COMMENT) + "/" + id.get(i);
            Response response = CLIENT.get().sendDeleteRequestWithHeaders(uniqueCommentPath, "", Map.ofEntries(
                    Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                    Map.entry("X-Requested-With", "XMLHttpRequest")
            ));

            ResponseDataTransferMapper restAPIClient = new ResponseDataTransferMapper();
            CommentDTO responseDTO = restAPIClient.transformToDtoCom(response, logger);
            Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
            Assert.assertNull(responseDTO.getComment());
        }
    }

}
