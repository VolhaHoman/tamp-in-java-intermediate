package com.epam.mentoring.taf.tests.ui;

import com.epam.mentoring.taf.UiBaseTest;
import com.epam.mentoring.taf.api.CommentDTO;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.mapper.ResponseDataTransferMapper;
import com.epam.mentoring.taf.util.StorageHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.List;
import java.util.Map;

import static com.epam.mentoring.taf.util.StorageHelper.rememberThat;
import static com.epam.mentoring.taf.util.StorageHelper.whatIsThe;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("UI: Comments Tests")
public class CommentUITest extends UiBaseTest {

    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    public static final String COM_ID = "ID";
    public static final String ALL_COMMENT = "ALL_COMMENT";
    public static final String ERROR_MESSAGE = "body can't be blank";
    public static final String COMMENT = "Test";

    private static Logger log = LogManager.getLogger();

    @Test(description = "UI: add comment to article", priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI verification of adding comments")
    @Story("Create new tests for comments functionality")
    public void uiSubmittedCommentVerification() {
        logIn(StorageHelper.whatIsThe(ADMIN_EMAIL), StorageHelper.whatIsThe(ADMIN_PASSWORD));
        selectArticle();
        articlePage.enterComment(COMMENT)
                .clickSendCommentBtn();

        Assert.assertEquals(articlePage.getComment(), COMMENT);
        logOut();
    }

    @Test(description = "UI: add empty comment", priority = 2)
    @Severity(SeverityLevel.MINOR)
    @Story("Create new tests for comments")
    @Description("UI add empty comment")
    public void uiEmptyCommentVerification() {
        logIn(StorageHelper.whatIsThe(ADMIN_EMAIL), StorageHelper.whatIsThe(ADMIN_PASSWORD));
        selectArticle();
        articlePage.enterComment("")
                .clickSendCommentBtn();


        Assert.assertEquals(articlePage.getError(), ERROR_MESSAGE);
        logOut();
    }

    @Test(description = "UI: delete comment from article", priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("UI verification of deleting comments")
    @Story("Create new tests for comments functionality")
    public void uiDeleteCommentVerification() {
        logIn(StorageHelper.whatIsThe(ADMIN_EMAIL), StorageHelper.whatIsThe(ADMIN_PASSWORD));
        selectArticle();
        articlePage.enterComment(COMMENT)
                .clickSendCommentBtn();
        articlePage.clickDeleteCommentBtn();

        Assert.assertFalse(articlePage.commentIsNotDisplayed());
        logOut();
    }

    @AfterTest(description = "Post-condition: delete all comments")
    public void cleanComments() {
        Response getResponse = client.sendGetRequestWithHeaders(whatIsThe(ALL_COMMENT), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")
        ));
        List<String> id =
                getResponse.getBody().jsonPath().getList("comments.id", String.class);
        rememberThat(COM_ID, String.valueOf(id));
        for (int i = 0; i < id.size(); i++) {
            String uniqueCommentPath = whatIsThe(ALL_COMMENT) + "/" + id.get(i);
            Response response = client.sendDeleteRequestWithHeaders(uniqueCommentPath, "", Map.ofEntries(
                    Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                    Map.entry("X-Requested-With", "XMLHttpRequest")
            ));

            ResponseDataTransferMapper restAPIClient = new ResponseDataTransferMapper();
            CommentDTO responseDTO = restAPIClient.transformToDtoCom(response, log);
            Assert.assertEquals(response.getStatusCode(), org.apache.hc.core5.http.HttpStatus.SC_OK);
            Assert.assertNull(responseDTO.getComment());
        }
    }

}
