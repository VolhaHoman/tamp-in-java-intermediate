package com.epam.mentoring.taf.tests;

import com.epam.mentoring.taf.api.RestClient;
import com.epam.mentoring.taf.dataobject.UserData;
import com.epam.mentoring.taf.dataobject.UserDataDTO;
import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.service.YamlReader;
import com.epam.mentoring.taf.util.Redirection;
import com.epam.mentoring.taf.util.StorageHelper;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;

import java.io.IOException;
import java.util.Map;

import static com.epam.mentoring.taf.tests.ui.FollowUserTest.ADMIN_USERNAME;
import static com.epam.mentoring.taf.mapper.UserDataMapper.mapToDTO;
import static com.epam.mentoring.taf.tests.api.CommentAPITest.ALL_COMMENT;
import static com.epam.mentoring.taf.util.StorageHelper.rememberThat;
import static com.epam.mentoring.taf.util.StorageHelper.whatIsThe;

public abstract class AbstractTest {

    protected final static String baseUrl = "https://angular.realworld.io";
    public final static String API_URL = "https://conduit.productionready.io";
    public static final String API_USERS = "https://api.realworld.io/api/users";
    public static final String LOGIN_URL = "/api/users/login";
    public static final String API_LOGIN = API_URL + LOGIN_URL;
    public static final String API_PROFILES = "https://api.realworld.io/api/profiles/";
    public static final String FOLLOW_PATH = "/follow";
    public static final String API_ARTICLES = "https://api.realworld.io/api/articles/";
    public static final String COMMENT_PATH = "/comments";
    public static final String ADMIN_EMAIL = "ADMIN_EMAIL";
    public static final String ADMIN_PASSWORD = "ADMIN_PASSWORD";
    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    public static final String SLUG = "SLUG";
    public static final String ARTICLES_COUNT_JSON_PATH = "articlesCount";

    protected UserDataDTO defaultUserData;
    protected static Logger log = LogManager.getLogger();
    protected static RestClient client = new RestClient(log);
    protected static final YamlReader READER = new YamlReader();

    protected Redirection redirection = new Redirection();

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
        String allCommentPath = API_ARTICLES + whatIsThe(SLUG) + COMMENT_PATH;
        rememberThat(ALL_COMMENT, allCommentPath);
    }

}
