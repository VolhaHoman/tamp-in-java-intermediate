package com.epam.mentoring.taf;

import com.epam.mentoring.taf.api.RestClient;
import com.epam.mentoring.taf.data.UserData;
import com.epam.mentoring.taf.data.UserDataDTO;
import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.ui.config.WebDriverCreate;
import com.epam.mentoring.taf.ui.page.*;
import com.epam.mentoring.taf.util.Redirection;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;

import java.io.IOException;

import static com.epam.mentoring.taf.CommentTest.ALL_COMMENT;
import static com.epam.mentoring.taf.CommentTest.SLUG;
import static com.epam.mentoring.taf.FollowUserTest.*;
import static com.epam.mentoring.taf.mapper.UserDataMapper.mapToDTO;
import static com.epam.mentoring.taf.util.StorageHelper.rememberThat;
import static com.epam.mentoring.taf.util.StorageHelper.whatIsThe;

abstract public class AbstractTest {

    protected final static String baseUrl = "https://angular.realworld.io";
    public final static String API_URL = "https://conduit.productionready.io";
    public static final String API_USERS = "https://api.realworld.io/api/users";
    public static final String LOGIN_URL = "/api/users/login";
    public static final String API_LOGIN = API_URL + LOGIN_URL;
    public static final String API_PROFILES = "https://api.realworld.io/api/profiles/";
    public static final String FOLLOW_PATH = "/follow";

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected UserDataDTO defaultUserData;
    private static Logger log = LogManager.getLogger();
    protected static RestClient client = new RestClient(log);

    protected LoginPage loginPage;
    protected HomePage homePage;
    protected CelebPage celebPage;

    Redirection redirection = new Redirection();

    @BeforeClass
    public void authorization() {
        try {
            defaultUserData = UserData.getUserDataFromYaml("adminUser");
        } catch (IOException e) {
            throw new ConfigurationSetupException("Can't load default user data", e);
        }

        Response response = client.sendPostRequest(redirection.getRedirectionUrl(API_LOGIN),
                mapToDTO(defaultUserData).toString());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);

        String authToken =
                response.getBody().jsonPath().get("user.token");
        String adminUserEmail = defaultUserData.getUserEmail();
        String adminUserPassword = defaultUserData.getUserPassword();
        String adminUserName = defaultUserData.getUserName();

        log.info("Username: " + defaultUserData.getUserName());
        log.info("User email: " + defaultUserData.getUserEmail());
        log.info("token: " + authToken);

        rememberThat(AUTH_TOKEN, authToken);
        rememberThat(ADMIN_EMAIL, adminUserEmail);
        rememberThat(ADMIN_PASSWORD, adminUserPassword);
        rememberThat(ADMIN_USERNAME, adminUserName);

        Response getResponse = client.sendGetRequest(API_ARTICLES);
        String slug =
                getResponse.getBody().jsonPath().get("articles[0].slug");
        log.info("slug: " + slug);
        rememberThat(SLUG, slug);

        String allCommentPath = API_ARTICLES + whatIsThe(SLUG) + COMMENT_PATH;
        rememberThat(ALL_COMMENT, allCommentPath);
    }

    @BeforeMethod
    public void initialisation() {
        // TODO: Remove after migration to Page Object Pattern.
        driver = WebDriverCreate.getWebDriverInstance();
        wait = WebDriverCreate.getWebDriverWaitInstance();

        loginPage = new LoginPage(baseUrl, log, driver, wait);
        homePage = new HomePage(log, driver, wait);
        celebPage = new CelebPage(log, driver, wait);

        driver.get(baseUrl);
    }

    @AfterClass
    public void terminate() {
        driver.quit();
    }
}
