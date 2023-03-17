package com.epam.mentoring.taf;

import com.epam.mentoring.taf.api.RestClient;
import com.epam.mentoring.taf.data.UserData;
import com.epam.mentoring.taf.data.UserDataDTO;
import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.ui.config.WebDriverCreate;
import com.epam.mentoring.taf.ui.page.CelebPage;
import com.epam.mentoring.taf.ui.page.HomePage;
import com.epam.mentoring.taf.ui.page.LoginPage;
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

import static com.epam.mentoring.taf.FollowUserTest.AUTH_TOKEN;
import static com.epam.mentoring.taf.mapper.UserDataMapper.*;
import static com.epam.mentoring.taf.util.StorageHelper.rememberThat;

abstract public class AbstractTest {

    protected final static String baseUrl = "https://angular.realworld.io";
    public final static String API_URL = "https://conduit.productionready.io";
    public static final String API_USERS = "https://api.realworld.io/api/users";
    public static final String LOGIN_URL = "/api/users/login";
    public static final String API_LOGIN = API_URL + LOGIN_URL;
    public static final String API_PROFILES = "https://api.realworld.io/api/profiles/";
    public static final String BASE_PATH = "/follow";

    protected WebDriver driver;
    protected WebDriverWait wait;
    protected UserDataDTO defaultUserData;
    private static Logger log = LogManager.getLogger();
    protected static RestClient client = new RestClient(log);

    protected static LoginPage loginPage = new LoginPage(baseUrl, log);
    protected static HomePage homePage = new HomePage(log);
    protected static CelebPage celebPage = new CelebPage(log);

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
        log.info("Username: " + defaultUserData.getUserName());
        log.info("User email: " + defaultUserData.getUserEmail());
        log.info("token: " + authToken);

        rememberThat(AUTH_TOKEN, authToken);
    }

    @BeforeMethod
    public void initialisation() {
        // TODO: Remove after migration to Page Object Pattern.
        driver = WebDriverCreate.getWebDriverInstance();
        wait = WebDriverCreate.getWebDriverWaitInstance();

        driver.get(baseUrl);
    }

    @AfterClass
    public void terminate() {
        driver.quit();
    }
}
