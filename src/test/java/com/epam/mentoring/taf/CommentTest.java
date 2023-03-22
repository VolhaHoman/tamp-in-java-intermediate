package com.epam.mentoring.taf;

import com.epam.mentoring.taf.api.CommentDTO;
import com.epam.mentoring.taf.api.CommentResponseDTO;
import com.epam.mentoring.taf.api.RestAPIClient;
import com.epam.mentoring.taf.api.RestClient;
import com.epam.mentoring.taf.data.UserData;
import com.epam.mentoring.taf.data.UserDataDTO;
import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.ui.page.ArticlePage;
import com.epam.mentoring.taf.ui.page.HomePage;
import com.epam.mentoring.taf.ui.page.LoginPage;
import com.epam.mentoring.taf.util.DataUtil;
import com.epam.mentoring.taf.util.StorageHelper;
import io.qameta.allure.*;
import io.restassured.response.Response;
import org.apache.hc.core5.http.HttpHeaders;
import org.apache.hc.core5.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.Map;

import static com.epam.mentoring.taf.mapper.UserDataMapper.mapToDTO;
import static com.epam.mentoring.taf.util.StorageHelper.rememberThat;

//@Listeners({TestListener.class, ReportPortalTestListener.class})
@Feature("Comments Tests")
public class CommentTest extends AbstractTest {
    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    public static final String COM_ID = "id";
    public static final String SLUG = "slug";
    private UserDataDTO defaultUserData;
    public static final String API_ARTICLES = "https://api.realworld.io/api/articles/";
    public static final String COMMENT_PATH = "/comments";
    private static Logger log = LogManager.getLogger();
    protected static RestClient client = new RestClient(log);
    public static final String ERROR_MESSAGE = "body can't be blank";

    @BeforeMethod(description = "Generate default Comment User")
    public void authorization() {
        try {
            defaultUserData = UserData.getUserDataFromYaml("commentUser");
        } catch (IOException e) {
            throw new ConfigurationSetupException("Can't load default user data", e);
        }
        Response response = client.sendPostRequest(redirection.getRedirectionUrl(API_LOGIN),
                mapToDTO(defaultUserData).toString());
        String authToken =
                response.getBody().jsonPath().get("user.token");
        log.info("Username: " + defaultUserData.getUserName());
        log.info("User email: " + defaultUserData.getUserEmail());
        log.info("token: " + authToken);
        rememberThat(AUTH_TOKEN, authToken);
        /*String path = API_ARTICLES;
        Response response2 = client.sendGetRequest(path);
        log.info("resp: " + response);
        String slug =
                response2.getBody().jsonPath().getString("articles.slug");
        log.info("sl: " + slug);
        rememberThat(SLUG, slug);*/
    }

/*    @BeforeMethod(description = "Generate default Comment User")
    public void getSlug() {
        String path = API_ARTICLES;
        Response response = client.sendGetRequest(path);
        log.info("resp: " + response);
        RestAPIClient restAPIClient1 = new RestAPIClient();
        //ArticleDTO responseDTO1 = restAPIClient1.transformToDtoArt(response);
        //responseDTO1.getArticles().getSlug().toString();
       // log.info("resp2: " + responseDTO1);

        String slug =
                response.getBody().jsonPath().getString("articles.slug");
        log.info("sl: " + slug);
        rememberThat(SLUG, slug);
    }*/

    @Test(description = "API Pre-condition: add multiple comments to article", dataProviderClass = DataUtil.class, dataProvider = "dataProviderForComments", priority = 0)
    @Severity(SeverityLevel.BLOCKER)
    @Description("API add comments to article")
    @Story("Create new tests for comments functionality")
    public void addComment(String data) {
        // Response response1 = client.sendGetRequest(API_ARTICLES);
        // RestAPIClient restAPIClient1 = new RestAPIClient();
        // ArticleDTO responseDTO1 = restAPIClient1.transformToDtoArt(response1);

        String path = API_ARTICLES;
        Response response2 = client.sendGetRequest(path);
        log.info("resp: " + response2);
        String slug =
                response2.getBody().jsonPath().get("articles.slug");
        log.info("sl: " + slug);
        // rememberThat(SLUG, slug);
        String path1 = API_ARTICLES + slug.toString() + COMMENT_PATH;
        CommentResponseDTO commentDTO = new CommentResponseDTO
                .ApiDTOBuilder(data)
                .build();
        Response response = client.sendPostRequestWithHeaders(path1, String.valueOf(commentDTO), Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + StorageHelper.whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")
        ));
        String id =
                response.getBody().jsonPath().getString("comment.id");
        log.info("id: " + id);
        rememberThat(COM_ID, id);
        RestAPIClient restAPIClient = new RestAPIClient();
        CommentDTO responseDTO = restAPIClient.transformToDtoCom(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        Assert.assertEquals(responseDTO.getComment().getBody(), data);
    }

/*    @Test(description = "UI Verify adding comments to article", dataProviderClass = DataUtil.class, dataProvider = "dataProviderForComments", priority = 1)
    @Severity(SeverityLevel.BLOCKER)
    @Description("UI verification of adding comments")
    @Story("Create new tests for comments functionality")
    public void uiSubmittedCommentVerification(String data) throws InterruptedException {
        LoginPage loginPage = new LoginPage(baseUrl, log);
        loginPage.clickSignInLink()
                .fillInEmail(defaultUserData.getUserEmail())
                .fillInPassword(defaultUserData.getUserPassword())
                .clickSignInBtn();
        HomePage homePage = new HomePage(log);
        homePage.navToUser()
                .selectFirstArt();
        ArticlePage articlePage = new ArticlePage(log);
      //  String[] comment = data.split(",");
       // articlePage.enterComment(comment[0])
       //         .sendComment();
        Assert.assertEquals(articlePage.getComment(), data);
    }*/

    @Test(description = "Post-condition: delete comments", dataProviderClass = DataUtil.class, dataProvider = "dataProviderForComments", priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Description("API delete comments")
    @Story("Create new tests for comments functionality")
    public void deleteComment() {
        String path = API_ARTICLES + "Test_Article-149485" + COMMENT_PATH + COM_ID;
        log.info("path" + path);
        Response response = client.sendDeleteRequestWithHeaders(path, "", Map.ofEntries(
                Map.entry(HttpHeaders.AUTHORIZATION, "Token " + StorageHelper.whatIsThe(AUTH_TOKEN)),
                Map.entry("X-Requested-With", "XMLHttpRequest")
        ));
        // RestAPIClient restAPIClient = new RestAPIClient();
        // CommentDTO responseDTO = restAPIClient.transformToDtoCom(response);
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);
        // Assert.assertEquals(responseDTO.getComment().getBody(), "");
    }


    /*@Test(description = "UI delete comments", priority = 2)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Create new tests for comments")
    @Description("UI delete comments")
    public void uiDeleteCommentVerification() {
        ArticlePage articlePage = new ArticlePage();
        articlePage.enterComment("Test5656")
                .sendComment()
                .deleteComment();
        Assert.assertEquals(articlePage.commentIsNotPresent(), true);
    }*/

    @Test(description = "UI add empty comment", priority = 3)
    @Severity(SeverityLevel.CRITICAL)
    @Story("Create new tests for comments")
    @Description("UI add empty comment")
    public void uiEmptyCommentVerification() {
        LoginPage loginPage = new LoginPage(baseUrl, log);
        loginPage.clickSignInLink()
                .fillInEmail(defaultUserData.getUserEmail())
                .fillInPassword(defaultUserData.getUserPassword())
                .clickSignInBtn();
        HomePage homePage = new HomePage(log);
        homePage.navToUser()
                .selectFirstArt();
        ArticlePage articlePage = new ArticlePage(log);
        articlePage.enterComment("")
                .sendComment();
        Assert.assertEquals(articlePage.getError(), ERROR_MESSAGE);
    }


}

