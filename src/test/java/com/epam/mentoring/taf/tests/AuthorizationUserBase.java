package com.epam.mentoring.taf.tests;

import com.epam.mentoring.taf.api.RestClient;
import com.epam.mentoring.taf.dataobject.UserData;
import com.epam.mentoring.taf.dataobject.UserDataDTO;
import com.epam.mentoring.taf.exception.ConfigurationSetupException;
import com.epam.mentoring.taf.util.Redirection;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;

import java.io.IOException;

import static com.epam.mentoring.taf.mapper.UserDataMapper.mapToDTO;
import static com.epam.mentoring.taf.util.StorageHelper.rememberThat;

public class AuthorizationUserBase implements IRestClient {
    public final static String BASE_URL = "https://angular.realworld.io";
    public static final String API_USERS = "https://api.realworld.io/api/users";
    public final static String API_URL = "https://conduit.productionready.io";
    public static final String API_PROFILES = "https://api.realworld.io/api/profiles/";
    public static final String FOLLOW_PATH = "/follow";
    public static final String LOGIN_URL = "/api/users/login";
    public static final String API_LOGIN = API_URL + LOGIN_URL;
    public static final String ADMIN_USERNAME = "ADMIN_USERNAME";
    public static final String RESPONSE = "Response";
    public static final String ADMIN_EMAIL = "ADMIN_EMAIL";
    public static final String ADMIN_PASSWORD = "ADMIN_PASSWORD";
    public static final String AUTH_TOKEN = "AUTH_TOKEN";
    private static final Logger log = LogManager.getLogger();

    static {
        CLIENT.set(new RestClient(log));
    }

    private AuthorizationUserBase() {
    }

    public static UserDataDTO authorization() {
        UserDataDTO authorizedUser = null;
        try {
            authorizedUser = UserData.getUserDataFromYaml("adminUser");
        } catch (IOException e) {
            throw new ConfigurationSetupException("Can't load default user data", e);
        }

        Response response = CLIENT.get().sendPostRequest(Redirection.getRedirectionUrl(API_LOGIN),
                mapToDTO(authorizedUser).toString());
        Assert.assertEquals(response.getStatusCode(), HttpStatus.SC_OK);

        String authToken =
                response.getBody().jsonPath().get("user.token");
        String adminUserEmail = authorizedUser.getUserEmail();
        String adminUserPassword = authorizedUser.getUserPassword();
        String adminUserName = authorizedUser.getUserName();

        log.info("Username: " + authorizedUser.getUserName());
        log.info("User email: " + authorizedUser.getUserEmail());
        log.info("token: " + authToken);

        rememberThat(AUTH_TOKEN, authToken);
        rememberThat(ADMIN_EMAIL, adminUserEmail);
        rememberThat(ADMIN_PASSWORD, adminUserPassword);
        rememberThat(ADMIN_USERNAME, adminUserName);
        return authorizedUser;
    }
}
