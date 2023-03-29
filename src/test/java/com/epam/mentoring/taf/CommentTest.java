package com.epam.mentoring.taf;

import com.epam.mentoring.taf.api.CommentDTO;
import com.epam.mentoring.taf.api.CommentResponseDTO;
import com.epam.mentoring.taf.api.RestAPIClient;
import com.epam.mentoring.taf.api.RestClient;
import com.epam.mentoring.taf.listeners.ReportPortalTestListener;
import com.epam.mentoring.taf.listeners.TestListener;
import com.epam.mentoring.taf.util.DataUtil;
import com.epam.mentoring.taf.util.StorageHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.epam.mentoring.taf.util.StorageHelper.rememberThat;
import static com.epam.mentoring.taf.util.StorageHelper.whatIsThe;

@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Comments Tests")
public class CommentTest extends AbstractTest {
    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    public static final String SLUG = "SLUG";
    public static final String COM_ID = "ID";
    public static final String ALL_COMMENT = "ALL_COMMENT";
    public static final String ERROR_MESSAGE = "body can't be blank";
    public static final String COMMENT = "Test";
    public static final String ADMIN_EMAIL = "ADMIN_USER";
    public static final String ADMIN_PASSWORD = "ADMIN_PASSWORD";

    private static Logger log = LogManager.getLogger();
    protected static RestClient client = new RestClient(log);

    @Test(description = "API Pre-condition: Add multiple valid comments to article", dataProviderClass = DataUtil.class, dataProvider = "dataProviderForValidComments", priority = 0)
    @Severity(SeverityLevel.BLOCKER)
    @Description("API add comments to article from json file")
    @Story("Create new tests for comments functionality")
    public void apiAddCommentWithValidText(HashMap<String, String> data) {
        CommentResponseDTO commentDTO = new CommentResponseDTO.ApiDTOBuilder(data.get("body")).build();
        Response response = client.sendPostRequestWithHeaders(whatIsThe(ALL_COMMENT), commentDTO.toString(), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")
        ));

        RestAPIClient restAPIClient = new RestAPIClient();
        CommentDTO responseDTO = restAPIClient.transformToDtoCom(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        Assert.assertEquals(responseDTO.getComment().getBody(), data.get("body"));
    }

    @Test(description = "API: add invalid comments to article", dataProviderClass = DataUtil.class, dataProvider = "dataProviderForInvalidComments", priority = 1)
    @Severity(SeverityLevel.NORMAL)
    @Description("API add invalid comments to article from json file")
    @Story("Create new tests for comments functionality")
    public void apiAddCommentWithInvalidText(HashMap<String, String> data) {
        CommentResponseDTO commentDTO = new CommentResponseDTO.ApiDTOBuilder(data.get("invalid_text")).build();
        Response response = client.sendPostRequestWithHeaders(whatIsThe(ALL_COMMENT), String.valueOf(commentDTO), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")
        ));
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_UNPROCESSABLE_ENTITY);
    }

    @Test(description = "UI: add comment to article", priority = 2)
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI verification of adding comments")
    @Story("Create new tests for comments functionality")
    public void uiSubmittedCommentVerification() {

        loginPage.clickSignInLink()
                .fillInEmail(StorageHelper.whatIsThe(ADMIN_EMAIL))
                .fillInPassword(StorageHelper.whatIsThe(ADMIN_PASSWORD))
                .clickSignInBtn();

        homePage.navToUser()
                .selectArt();

        articlePage.enterComment(COMMENT)
                .sendComment();

        Assert.assertEquals(articlePage.getComment(), COMMENT);
    }

    @Test(description = "UI: add empty comment", priority = 4)
    @Severity(SeverityLevel.MINOR)
    @Story("Create new tests for comments")
    @Description("UI add empty comment")
    public void uiEmptyCommentVerification() {

        homePage.navGlobalFeed()
                .selectArt();

        articlePage.enterComment("")
                .sendComment();

        Assert.assertEquals(articlePage.getError(), ERROR_MESSAGE);
    }

    @Test(description = "UI: delete comment from article", priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("UI verification of deleting comments")
    @Story("Create new tests for comments functionality")
    public void uiDeleteCommentVerification() {

        homePage.navToUser()
                .selectArt();

        articlePage.enterComment(COMMENT)
                .sendComment()
                .deleteComment();

        Assert.assertEquals(articlePage.commentIsNotPresent(), true);
    }

    @AfterTest(description = "Post-condition: delete all comments")
    @Severity(SeverityLevel.CRITICAL)
    @Description("API delete comments")
    @Story("Create new tests for comments functionality")
    public void cleanComments() {
        Response getResponse = client.sendGetRequestWithHeaders(whatIsThe(ALL_COMMENT), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")
        ));
        List<String> id =
                getResponse.getBody().jsonPath().getList("comments.id", String.class);
        log.info("id: " + id);
        rememberThat(COM_ID, String.valueOf(id));

        for (int i = 0; i < id.size(); i++) {
            String uniqueCommentPath = whatIsThe(ALL_COMMENT) + "/" + id.get(i);

            Response response = client.sendDeleteRequestWithHeaders(uniqueCommentPath, "", Map.ofEntries(
                    Map.entry(HttpHeaders.AUTHORIZATION, "Token " + whatIsThe(AUTH_TOKEN)),
                    Map.entry("X-Requested-With", "XMLHttpRequest")
            ));

            RestAPIClient restAPIClient = new RestAPIClient();
            CommentDTO responseDTO = restAPIClient.transformToDtoCom(response);
            Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
            Assert.assertEquals(responseDTO.getComment(), null);
        }
    }
}

